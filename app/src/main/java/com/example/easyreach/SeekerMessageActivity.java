package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SeekerMessageActivity extends AppCompatActivity {
    TextView view_id,view_id2, rEmail;
    FirebaseAuth mAuth;
    EditText messageEditText;
    Button sendMsg;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Usersref = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_field);
        sendMsg = (Button) findViewById(R.id.sendMsgBtn);
        view_id = findViewById(R.id.id_view);
        rEmail = findViewById(R.id.rEmail);
        String USERID = view_id.getText().toString();
        view_id2 =findViewById(R.id.testo);

        Intent intent = getIntent();
        String new_id = intent.getStringExtra(myadapter_jobs.EXTRA_NAME);
        String receiverEmail = intent.getStringExtra(myadapter_jobs.EXTRA_EMAIL);
        rEmail.setText(receiverEmail);
        view_id.setText(new_id);
        messageEditText = findViewById(R.id.message);

        mAuth = FirebaseAuth.getInstance();


        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipient = view_id.getText().toString();
                String content = messageEditText.getText().toString();
                String recipientEmail= rEmail.getText().toString();
                String senderEmail = mAuth.getCurrentUser().getEmail();
                Messages message = new Messages();
                String senderUID = mAuth.getCurrentUser().getUid();
                message.setSenderID(senderUID);
                message.setRecipientID(recipient);
                message.setContent(content);
                message.setRecipientEmail(recipientEmail);
                message.setSenderEmail(senderEmail);
                message.setAccepted(false);

                // Get the current timestamp
                long currentTimeMillis = System.currentTimeMillis();
                Timestamp timestamp = new Timestamp(currentTimeMillis / 1000, (int) (currentTimeMillis % 1000) * 1000000);
                message.setTimestamp(timestamp);
                CollectionReference messagesRef = db.collection("Messages");

                // Save the message to Firestore
                messagesRef.add(message)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Message sent successfully
                                Toast.makeText(SeekerMessageActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error sending message
                                Toast.makeText(SeekerMessageActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                            }
                        });
                messageEditText.setText("");

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SeekerMessageActivity.this, SeekerMainActivity.class);
        startActivity(i);

    }
}