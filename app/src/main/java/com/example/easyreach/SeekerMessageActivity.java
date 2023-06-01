package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SeekerMessageActivity extends AppCompatActivity {
    TextView view_id,view_id2;
    FirebaseAuth mAuth;
    EditText message;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Usersref = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_message);
        view_id = findViewById(R.id.id_view);
        String USERID = view_id.getText().toString();
        view_id2 =findViewById(R.id.testo);
        Intent intent = getIntent();
        String new_id = intent.getStringExtra(myadapter_jobs.EXTRA_NAME);
        view_id.setText(new_id);
        message = findViewById(R.id.message);

        mAuth = FirebaseAuth.getInstance();


    }
    public void sendmessage(View view){

        String userMail = mAuth.getCurrentUser().getEmail();
        String Message = this.message.getText().toString();
        String Description = view_id2.getText().toString();

        String USERID = view_id.getText().toString();
        ADD_Message add = new ADD_Message(Message,userMail);
        Usersref.document(USERID).collection("Messages").add(add);
        Toast.makeText(this, "Message Sent Successfully!", Toast.LENGTH_SHORT).show();
        message.setText("");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    Intent i = new Intent(SeekerMessageActivity.this, SeekerMainActivity.class);
    startActivity(i);
    }
}