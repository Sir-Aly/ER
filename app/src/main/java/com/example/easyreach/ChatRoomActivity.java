package com.example.easyreach;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button btnSendMessage;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private String chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        chatRoomId = getIntent().getStringExtra("chatRoomId");

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        loadMessages();
    }

    private void loadMessages() {
        db.collection("chatRooms").document(chatRoomId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@NonNull QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(ChatRoomActivity.this, "Error retrieving messages", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Message message = dc.getDocument().toObject(Message.class);
                                    messages.add(message);
                                    messageAdapter.notifyItemInserted(messages.size() - 1);
                                    recyclerView.scrollToPosition(messages.size() - 1);
                                    break;
                            }
                        }
                    }
                });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            String userId = mAuth.getCurrentUser().getUid();
            String userEmail = mAuth.getCurrentUser().getEmail();
            String timestamp = new Date().toString();
            String adminId= "TedGNfWtDUSHJF4bmuPHkvjrTM33";

            Message message = new Message(messageText, chatRoomId, userId, userEmail, timestamp, adminId);

            db.collection("chatRooms").document(chatRoomId).collection("messages")
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            editTextMessage.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatRoomActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
