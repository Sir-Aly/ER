package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class inbox_viewer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recview;
    int messageCount;

    TextView message_size;
    FirebaseFirestore db;
    MessagesAdapter myadapter_inbox;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_viewer);

        refreshLayout = findViewById(R.id.refreshlayout);
        message_size = findViewById(R.id.messages_size);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                onBackPressed();
            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreateData();
            }
        });
        // Set up Firestore query to retrieve the user's received messages
        FirestoreRecyclerOptions<Messages> options = new FirestoreRecyclerOptions.Builder<Messages>()
                .setQuery(getMessagesQuery(), Messages.class)
                .build();
        getMessageCount();
        // Initialize and set up the adapter
        adapter = new MessagesAdapter(options);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot) {
                // Handle item click, open message details activity
                showMessageDetails(snapshot);
            }
        });
    }

    private Query getMessagesQuery() {
        // Customize this method to retrieve the received messages for the current user from Firestore
        // You can use Firestore queries to filter and order the messages as needed
        return FirebaseFirestore.getInstance()
                .collection("Messages")
                .whereEqualTo("recipientID", getCurrentUserId());
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            // Handle the case when the current user is not available
            return null;
        }
    }

    private void showMessageDetails(DocumentSnapshot snapshot) {
        // Extract the message ID from the snapshot
        String messageId = snapshot.getId();

        // Create an intent to open the message details activity
        Intent intent = new Intent(this, MessageReplyActivity.class);
        intent.putExtra("messageId", messageId);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    private void getMessageCount() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Messages")
                .whereEqualTo("recipientID", getCurrentUserId());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = task.getResult().size();
                messageCount = count;
                message_size.setText(String.valueOf(count +" "+"NEW MESSAGES"));
            } else {
                // Handle the case when the query fails
            }
        });
    }

    private void recreateData() {
        // Recreate your data or update the RecyclerView here
        recreate();
getMessagesQuery();


        // Call setRefreshing(false) to stop the refreshing animation
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}