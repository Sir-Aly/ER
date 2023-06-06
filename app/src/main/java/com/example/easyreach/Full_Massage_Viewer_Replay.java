package com.example.easyreach;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Full_Massage_Viewer_Replay extends AppCompatActivity {

    TextView fullmessage;
    TextView checkmessage;
    TextView signature;
    EditText message;
    FirebaseAuth mAuth;
    Button btnaccept;
    String messageaccepted = "OFFER ACCEPTED";
    TextView FROM_ID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Usersref = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_massage_viewer_replay);
        Intent intent = getIntent();
        String From_USER_ID = intent.getStringExtra(myadapter_inbox.EXTRA_USER_ID);
        String Fullmessage = intent.getStringExtra(myadapter_inbox.EXTRA_MESSAGE);
        fullmessage = findViewById(R.id.fullmassage);
//        message = findViewById(R.id.message_replay);
        FROM_ID = findViewById(R.id.id_From_viewer);
        FROM_ID.setText(From_USER_ID);
        fullmessage.setText(Fullmessage);
        mAuth = FirebaseAuth.getInstance();
        btnaccept = findViewById(R.id.btnaccept);
        checkmessage = findViewById(R.id.checkmessage);
        signature = findViewById(R.id.signature);


        if (Fullmessage.equals(messageaccepted)){
            btnaccept.setVisibility(View.GONE);
            checkmessage.setVisibility(View.VISIBLE);
            signature.setVisibility(View.VISIBLE);
            fullmessage.setTextColor(getColor(R.color.green));
            fullmessage.setTextSize(40);
            fullmessage.setTypeface(fullmessage.getTypeface(), Typeface.BOLD);
        }
    }
    public void replymessage(View view){
        String userMail = mAuth.getCurrentUser().getEmail();
        String Message = this.messageaccepted;
        String Uid = mAuth.getCurrentUser().getUid();
        String USERID = FROM_ID.getText().toString();
        ADD_Message add = new ADD_Message(Message,userMail,Uid);
        Usersref.document(USERID).collection("com.example.easyreach.Messages").add(add);
        Toast.makeText(this, "Offer Answer Sent Successfully!", Toast.LENGTH_SHORT).show();
//        message.setText("");
        btnaccept.setVisibility(View.GONE);
    }
}