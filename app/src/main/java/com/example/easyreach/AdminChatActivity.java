package com.example.easyreach;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdminChatActivity extends AppCompatActivity {
    private static final String TAG = "MessagingActivity";

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button btnSendMessage;

    private FirebaseFirestore db;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private FirebaseAuth mAuth;
    private String chatRoomId; // The unique identifier for the chat room

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        db = FirebaseFirestore.getInstance();
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        mAuth = FirebaseAuth.getInstance();
        String userId1 = mAuth.getCurrentUser().getUid();
        String userId2 = getIntent().getStringExtra("documentId");

        chatRoomId = generateChatRoomId(userId1, userId2);

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                onBackPressed();
            }
        });

        // Load existing messages from the chat room
        loadMessages();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadMessages() {
        String userId1 = mAuth.getCurrentUser().getUid();
        db.collection("chatRooms").document(chatRoomId).collection("messages")
                .orderBy("timestamp") // Sort messages by timestamp
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Error retrieving messages: ", error);
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    // New message added to the chat room
                                    Message message = dc.getDocument().toObject(Message.class);
                                    messages.add(message);
                                    messageAdapter.notifyItemInserted(messages.size() - 1);
                                    recyclerView.scrollToPosition(messages.size() - 1);
                                    break;
                                case MODIFIED:
                                    // Existing message modified (if applicable)
                                    break;
                                case REMOVED:
                                    // Existing message removed (if applicable)
                                    break;
                            }
                        }
                    }
                });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            String userId1 = mAuth.getCurrentUser().getUid();
            String userId3 = mAuth.getCurrentUser().getEmail();
            String userId2 = "TedGNfWtDUSHJF4bmuPHkvjrTM33";
            // Create a new message object
            String timestamp = new Date().toString();
            Message message = new Message(messageText, userId2, userId2, userId1, userId3, timestamp);

            // Save the message to the chat room
            db.collection("chatRooms").document(chatRoomId).collection("messages")
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Message sent successfully");
                            editTextMessage.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error sending message: ", e);
                        }
                    });
        }
    }

    public String generateChatRoomId(String userId1, String userId2) {
        // Sort the user IDs alphabetically
        String sortedUserIds = Stream.of(userId1, userId2)
                .sorted()
                .collect(Collectors.joining());
        // Generate a unique hash for the sorted user IDs
        String chatRoomId = Hashing.sha256()
                .hashString(sortedUserIds, StandardCharsets.UTF_8)
                .toString();

        return chatRoomId;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
