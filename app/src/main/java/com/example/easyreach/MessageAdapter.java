package com.example.easyreach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContent;
        private TextView textViewTimestamp, textViewMail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.tvMessageContent);
            textViewTimestamp = itemView.findViewById(R.id.tvTimestamp);
            textViewMail = itemView.findViewById(R.id.usermail);
        }

        public void bind(Message message) {
            textViewContent.setText(message.getMessageContent());
            String timestamp = new Date().toString();
            textViewMail.setText(message.getSenderEmail());
            textViewTimestamp.setText(timestamp);
        }

        private String formatDate(long timestamp) {
            // Implement your own date/time formatting logic here
            // Example: return DateFormat.format("yyyy-MM-dd HH:mm", timestamp).toString();
            return "";
        }
    }
}
