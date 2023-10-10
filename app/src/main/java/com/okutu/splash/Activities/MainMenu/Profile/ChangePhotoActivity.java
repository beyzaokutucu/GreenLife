package com.okutu.splash.Activities.MainMenu.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.okutu.splash.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePhotoActivity extends AppCompatActivity {
    private CircleImageView profileImageView ;
    private Button closebutton, savebutton;
    private TextView profileChangeBtn;
    private DatabaseReference databaseReference ;
    private FirebaseAuth firebaseAuth;

    private Uri imageUri;

    private String myUri="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changephoto);

        firebaseAuth =FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        profileImageView = findViewById(R.id.profile_image);
        closebutton = findViewById(R.id.btnClose);
        savebutton = findViewById(R.id.btnSave);
        profileChangeBtn = findViewById(R.id.change_profile_btn);
        closebutton.setOnClickListener(v -> startActivity(new Intent(ChangePhotoActivity.this, EditProfileActivity.class)));
        savebutton.setOnClickListener(v -> uploadProfileImage());
        profileChangeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);

        });

        getUserinfo();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }
    private void getUserinfo() {

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    if (dataSnapshot.hasChild("image")) {
                        String image = dataSnapshot.child("image").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }

            @Override

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void uploadProfileImage() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data ");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicsRef
                    .child(firebaseAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);


            uploadTask.continueWithTask(task -> {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {

                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    myUri = downloadUrl.toString();
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("image").setValue(myUri);
                    progressDialog.dismiss();
                }

            });
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText( this, "Image not selected", Toast.LENGTH_SHORT).show();


        }
    }
}
