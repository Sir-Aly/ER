package com.example.easyreach;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etMessageInput;
    private Button btnSendMessage;

    private FirebaseFirestore firestore;
    private CollectionReference messagesRef;
    private MessageAdapter adapter;
    private String adminUID = "TedGNfWtDUSHJF4bmuPHkvjrTM33";

    private List<Message> messageList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();
        messagesRef = firestore.collection("chats");
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        etMessageInput = findViewById(R.id.etMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        // Set up RecyclerView
        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Button click listener to send a message
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

String currentUserID = mAuth.getCurrentUser().getUid();
        // Set up Firestore listener to retrieve new messages in real-time
// Query for messages where the current user is the sender
        Query senderQuery = messagesRef.whereEqualTo("senderID", currentUserID)
                .orderBy("timestamp", Query.Direction.ASCENDING);

// Query for messages where the current user is the receiver
        Query receiverQuery = messagesRef.whereEqualTo("receiverID", currentUserID)
                .orderBy("timestamp", Query.Direction.ASCENDING);

// Create a combined query with OR condition
        Query combinedQuery = messagesRef.whereEqualTo("adminUID", "TedGNfWtDUSHJF4bmuPHkvjrTM33")
                .whereEqualTo("senderID", currentUserID)
                .whereIn("receiverID", Arrays.asList(currentUserID))
                .orderBy("timestamp", Query.Direction.ASCENDING);

        combinedQuery.addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                // Handle error
                return;
            }

            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                messageList.clear();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    Message message = document.toObject(Message.class);
                    messageList.add(message);
                }
                adapter.notifyDataSetChanged();
                scrollToLatestMessage();
            }
        });
    }

    private void sendMessage() {
        String messageContent = etMessageInput.getText().toString().trim();
        if (!messageContent.isEmpty()) {
            // Create a new Message object

            String senderName = mAuth.getCurrentUser().getEmail(); // Get the user's name from Firebase Firestore
            String timestamp = new Date().toString();
            String receiverID = "TedGNfWtDUSHJF4bmuPHkvjrTM33";
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String senderID = mAuth.getCurrentUser().getUid();
            Message message = new Message(receiverID, adminUID, senderID, senderName, messageContent, timestamp);

            // Save the message to Firestore
            messagesRef.document().set(message)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            etMessageInput.setText(""); // Clear the input field after sending
                            scrollToLatestMessage();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }
    }

    private void scrollToLatestMessage() {
        if (messageList.size() > 0) {
            recyclerView.smoothScrollToPosition(messageList.size() - 1);
        }
    }
}
