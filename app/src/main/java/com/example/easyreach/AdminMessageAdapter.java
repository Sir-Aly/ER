package com.example.easyreach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AdminMessageAdapter extends ArrayAdapter<Users> {

    public AdminMessageAdapter(Context context, List<Users> userList) {
        super(context, 0, userList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        Users user = getItem(position);
        if (user != null) {
            TextView tvUserName = itemView.findViewById(R.id.tvUserName);
            TextView tvUserID = itemView.findViewById(R.id.tvUserID);
            TextView tvUserEmail = itemView.findViewById(R.id.tvUserEmail);

            tvUserName.setText(user.getSenderName());
            tvUserID.setText(user.getId());
            tvUserEmail.setText(user.getSenderName());
        }

        return itemView;
    }
}
