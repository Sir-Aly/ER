package com.example.easyreach;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageReplyActivity extends AppCompatActivity {

    private String messageId, oldContent, oldSender, oldSenderID;
    private DocumentReference messageRef;
    private CollectionReference  newMessageRef;
    TextView oldMsgTv, oldTime, oldMsgSender;
    EditText reply;
    private FirebaseAuth mAuth;
    Button sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_reply);
        messageId = getIntent().getStringExtra("messageId");
        oldMsgTv = findViewById(R.id.oldMsgTv);
        oldTime = findViewById(R.id.oldMsgTime);
        oldMsgSender = findViewById(R.id.senderMailTv);
        reply = findViewById(R.id.replyMessage);
        sendMsg = findViewById(R.id.sendMsgBtn);

        messageRef = FirebaseFirestore.getInstance().collection("Messages").document(messageId);
        messageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                oldContent = documentSnapshot.getString("content");
                oldSender = documentSnapshot.getString("senderEmail");
                oldSenderID = documentSnapshot.getString("senderID");
                Timestamp oldtime = documentSnapshot.getTimestamp("timestamp");
                String formattedTimestamp = formatDate(documentSnapshot.getTimestamp("timestamp"));
                oldMsgTv.setText(oldContent);
                oldMsgSender.setText(oldSender);
                oldTime.setText(formattedTimestamp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String replies =  reply.getText().toString();
                String current_Id =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                String current_email =  FirebaseAuth.getInstance().getCurrentUser().getEmail();
                long currentTimeMillis = System.currentTimeMillis();
                Timestamp timestamp = new Timestamp(currentTimeMillis / 1000, (int) (currentTimeMillis % 1000) * 1000000);
                Map<String, Object> updates = new HashMap<>();
                updates.put("content", replies);
                updates.put("recipientEmail", oldSender);
                updates.put("recipientID", oldSenderID);
                updates.put("senderEmail", current_email);
                updates.put("senderID", current_Id);
                updates.put("timestamp", timestamp);
                updates.put("accepted", false);

                if (!replies.equals("")){
                newMessageRef = FirebaseFirestore.getInstance().collection("Messages");
newMessageRef.add(updates).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
    @Override
    public void onSuccess(DocumentReference documentReference) {
        Toast.makeText(MessageReplyActivity.this, "Reply added successfully", Toast.LENGTH_SHORT).show();
        reply.setText("");
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(MessageReplyActivity.this, "Failed to add reply", Toast.LENGTH_SHORT).show();
    }
});
            }else {
                Toast.makeText(MessageReplyActivity.this, "Please type your message", Toast.LENGTH_SHORT).show();
            }
            }
        });



    }
    private String formatDate(Timestamp timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return dateFormat.format(timestamp.toDate());
    }
}