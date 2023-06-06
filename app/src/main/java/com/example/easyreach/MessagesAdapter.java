package com.example.easyreach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Messages, MessagesAdapter.MessageViewHolder> {
    private OnItemClickListener listener;
    private List<DocumentSnapshot> messageSnapshots;

    public MessagesAdapter(@NonNull FirestoreRecyclerOptions<Messages> options) {
        super(options);
        messageSnapshots = new ArrayList<>();
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Messages model) {
        // Bind message data to the view holder
        holder.bind(model);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public List<DocumentSnapshot> getMessageSnapshots() {
        return messageSnapshots;
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot snapshot);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView contentTextView;
        private TextView messageTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            messageTime = itemView.findViewById(R.id.timestampTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                        listener.onItemClick(snapshot);
                    }
                }
            });
        }

        public void bind(Messages message) {
            // Bind message data to the view
            senderTextView.setText(message.getSenderEmail());
            contentTextView.setText(message.getContent());
            String formattedTimestamp = formatDate(message.getTimestamp());
            messageTime.setText(formattedTimestamp);
            messageTime.setText(formattedTimestamp);

        }
    }
    private String formatDate(Timestamp timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return dateFormat.format(timestamp.toDate());
    }
}
