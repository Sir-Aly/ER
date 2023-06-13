package com.example.easyreach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<MyUser> userList;
    private OnItemClickListener listener;

    public UserAdapter(List<String> userIds, OnItemClickListener onItemClickListener) {

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public UserAdapter(List<MyUser> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        MyUser user = userList.get(position);
        String chatRoomId = user.getChatRoomId();
        holder.bind(user);
        holder.itemView.setTag(chatRoomId);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUsername, textViewChatRoomId;

        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.tvUserEmail);
            textViewChatRoomId = itemView.findViewById(R.id.tvUserID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(MyUser user) {
            textViewUsername.setText(user.getSenderEmail());
            textViewChatRoomId.setText(user.getChatRoomId());
        }
    }
}
