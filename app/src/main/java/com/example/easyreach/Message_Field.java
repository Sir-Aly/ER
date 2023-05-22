package com.example.easyreach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Message_Field extends AppCompatActivity {
    TextView view_id,view_id2;
    EditText message;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Usersref = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_field);
        view_id = findViewById(R.id.id_view);
        String USERID = view_id.getText().toString();
        view_id2 =findViewById(R.id.testo);
        Intent intent = getIntent();
        String new_id = intent.getStringExtra(myadapter.EXTRA_NAME);
        view_id.setText(new_id);
        message = findViewById(R.id.message);


    }
    public void sendmessage(View view){

        String message = this.message.getText().toString();
        String description = view_id2.getText().toString();

        String USERID = view_id.getText().toString();
        ADD_Message add = new ADD_Message(message,description);
        Usersref.document(USERID).collection("Messages").add(add);

    }

}