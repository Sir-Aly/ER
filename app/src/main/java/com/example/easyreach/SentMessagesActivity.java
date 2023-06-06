package com.example.easyreach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SentMessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_messages);

        recyclerView = findViewById(R.id.recyclerView);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        setUpRecyclerView();
        loadSentMessages();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Customize the item divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new MessagesAdapter(new FirestoreRecyclerOptions.Builder<Messages>()
                .setQuery(getQuery(), Messages.class)
                .build());

        recyclerView.setAdapter(adapter);
    }

    private Query getQuery() {
        // Construct the query to retrieve sent messages for the current provider
        return firestore.collection("Messages")
                .whereEqualTo("senderID", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING);
    }

    private void loadSentMessages() {
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class MessagesAdapter extends FirestoreRecyclerAdapter<Messages, MessagesAdapter.MessageViewHolder> {
        public MessagesAdapter(@NonNull FirestoreRecyclerOptions<Messages> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Messages model) {
            holder.bind(model);
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
            return new MessageViewHolder(view);
        }

        public class MessageViewHolder extends RecyclerView.ViewHolder {
            private TextView recipientTextView;
            private TextView contentTextView;
            private TextView timestampTextView;

            public MessageViewHolder(@NonNull View itemView) {
                super(itemView);
                recipientTextView = itemView.findViewById(R.id.senderTextView);
                contentTextView = itemView.findViewById(R.id.contentTextView);
                timestampTextView = itemView.findViewById(R.id.timestampTextView);
            }

            public void bind(Messages message) {
                recipientTextView.setText(message.getRecipientEmail());
                contentTextView.setText(message.getContent());
                timestampTextView.setText(formatTimestamp(message.getTimestamp()));

                // Check if the offer has been accepted
                if (message.isAccepted()) {
                    // Set background color to green for accepted messages
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.color_bubble));
                } else {
                    // Set default background color for non-accepted messages
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.grey));
                }
            }
        }
    }

    private String formatTimestamp(Timestamp timestamp) {
        // Format the timestamp into the desired format
        // Implement your own logic here, such as converting the timestamp to a string representation
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(timestamp.toDate());
    }
}