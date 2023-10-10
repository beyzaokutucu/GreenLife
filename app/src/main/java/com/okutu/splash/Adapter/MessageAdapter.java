package com.okutu.splash.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.okutu.splash.Models.Message;
import com.okutu.splash.R;

import java.util.List;


public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, R.layout.message, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message, parent, false);
        }

        TextView messageText = convertView.findViewById(R.id.messageTextView);
        messageText.setText(message.getMessage());
        if (message.getSenderId().equals(uuid)) {
            messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            messageText.setBackgroundColor(Color.WHITE);
        } else {
            messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            messageText.setBackgroundColor(Color.GREEN);
        }

        return convertView;
    }
}
