package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessageDetailsActivity extends AppCompatActivity {

    private TextView senderTextView;
    private TextView contentTextView;
    private Button acceptButton;
    private Button declineButton;

    private String messageId;
    private DocumentReference messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        // Get the message ID from the intent
        messageId = getIntent().getStringExtra("messageId");

        // Initialize views
        senderTextView = findViewById(R.id.senderTextView);
        contentTextView = findViewById(R.id.contentTextView);
        acceptButton = findViewById(R.id.acceptButton);
        declineButton = findViewById(R.id.declineButton);

        // Set up Firestore reference for the message
        messageRef = FirebaseFirestore.getInstance().collection("Messages").document(messageId);

        // Set click listeners for accept and decline buttons
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptMessage();
                recreate();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage();
            }
        });

        // Fetch and display the message details
        fetchMessageDetails();
    }

    private void fetchMessageDetails() {
        messageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()) {
                    Messages message = snapshot.toObject(Messages.class);
                    displayMessageDetails(message);
                } else {
                    // Handle message not found
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
            }
        });
    }

    private void displayMessageDetails(Messages message) {
        senderTextView.setText(message.getSenderEmail());
        contentTextView.setText(message.getContent());

        // Hide or show views based on message acceptance status
        if (message.isAccepted()) {
            acceptButton.setVisibility(View.GONE);
        } else {
            acceptButton.setVisibility(View.VISIBLE);
            declineButton.setVisibility(View.VISIBLE);
        }
    }

    private void acceptMessage() {
        // Update the message acceptance status to true
        messageRef.update("accepted", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MessageDetailsActivity.this, "Offer Accepted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle acceptance failure
                    }
                });
    }

    private void deleteMessage() {
        // Delete the message from Firestore
        messageRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MessageDetailsActivity.this, "Message is Deleted Succesfully", Toast.LENGTH_SHORT).show();
                        Intent back = new Intent(MessageDetailsActivity.this, InboxActivity.class);
                        startActivity(back);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle deletion failure
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent b = new Intent(MessageDetailsActivity.this, InboxActivity.class);
        startActivity(b);
    }
}