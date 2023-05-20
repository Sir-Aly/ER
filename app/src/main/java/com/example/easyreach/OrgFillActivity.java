package com.example.easyreach;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OrgFillActivity extends AppCompatActivity {

    TextView anis;
    String skills;
    EditText firstName, age, eAddress, eField, userMail;
    MaterialButton RegisterBtn, UploadBtn;
    FirebaseFirestore db;
private FirebaseAuth mAuth;

    String[] item = {"Web Developer", "AI Developer", "Data Scientist", "Accountant"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_fill);


        autoCompleteTextView = findViewById(R.id.auto_complete_textview);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                anis = findViewById(R.id.anis);
                anis.setText(item);
                skills = anis.getText().toString();
            }
        });

        db = FirebaseFirestore.getInstance();
        firstName = findViewById(R.id.firstName);
        userMail = findViewById(R.id.userMail);
        age = findViewById(R.id.age);
        eAddress = findViewById(R.id.address);
//        eField = findViewById(R.id.field);
        RegisterBtn = findViewById(R.id.btnRegister);
        UploadBtn = findViewById(R.id.btnRegisterPic);
        mAuth = FirebaseAuth.getInstance();
        UploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrgFillActivity.this, UploadActivity.class);
                startActivity(i);
                finish();
            }
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String userID =  mAuth.getCurrentUser().getUid();
                String Firstname = firstName.getText().toString();
                String Email = userMail.getText().toString();
                String Age = age.getText().toString();
                String Address = eAddress.getText().toString();
//                String Field = eField.getText().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("name",Firstname);
                user.put("email", Email);
                user.put("age",Age);
                user.put("location", Address);
                user.put("skills", skills);

                db.collection("job_seeker").document(userID)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(OrgFillActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                                Toast.makeText(OrgFillActivity.this, e.getMessage() ,Toast.LENGTH_SHORT).show();


                            }
                        });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent btnClick = new Intent(OrgFillActivity.this, MainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }
}