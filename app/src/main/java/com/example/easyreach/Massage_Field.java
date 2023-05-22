package com.example.easyreach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.CollectionReference;

public class Massage_Field extends AppCompatActivity {
    TextView view_id,view_id2;
    EditText massage;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Usersref = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage_field);
        view_id = findViewById(R.id.id_view);
        String USERID = view_id.getText().toString();
        view_id2 =findViewById(R.id.testo);
        Intent intent = getIntent();
        String new_id = intent.getStringExtra(myadapter.EXTRA_NAME);
        view_id.setText(new_id);
        massage = findViewById(R.id.massage);


    }
    public void sendmassage(View view){

        String massage = this.massage.getText().toString();
        String description = view_id2.getText().toString();

        String USERID = view_id.getText().toString();
        ADD_Massage add = new ADD_Massage(massage,description);
        Usersref.document(USERID).collection("Massages").add(add);

    }

}