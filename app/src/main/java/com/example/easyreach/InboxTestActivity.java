package com.example.easyreach;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class InboxTestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_test);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference messagesRef = db.collection("messages");

        Query query = messagesRef.orderBy("timestamp", Query.Direction.DESCENDING);
        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                onBackPressed();
            }
        });


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize the RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerViewMessages);
        adapter = new MessagesAdapter(new FirestoreRecyclerOptions.Builder<Messages>()
                .setQuery(query, Messages.class)
                .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Retrieve messages from Firestore
        retrieveMessages();

        // Set click listener for each message item in the list
        adapter.setOnItemClickListener(new MessagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot) {
                showMessageDetails(snapshot);
            }
        });
    }

    private void retrieveMessages() {
        // Query Firestore for messages collection
        db.collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                            adapter.getMessageSnapshots();
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle error
                        }
                    }
                });
    }

    private void showMessageDetails(DocumentSnapshot snapshot) {
        Messages message = snapshot.toObject(Messages.class);
        if (message != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Message Details");
            builder.setMessage("Sender: " + message.getSenderID() + "\nContent: " + message.getContent());
            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle accept action
                }
            });
            builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle decline action
                }
            });
            builder.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}