package com.example.easyreach;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class SeekerFillActivity extends AppCompatActivity {

    EditText firstName, age, eAddress, eField, userMail;
    MaterialButton Registerbtn, SeekerUploadbtn;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_fill);

        db = FirebaseFirestore.getInstance();
        firstName = findViewById(R.id.firstName);
        userMail = findViewById(R.id.userMail);
        age = findViewById(R.id.age);
        eAddress = findViewById(R.id.address);
        eField = findViewById(R.id.field);
        Registerbtn = findViewById(R.id.btnRegister);
        SeekerUploadbtn = findViewById(R.id.seekerBtnRegisterPic);
        mAuth = FirebaseAuth.getInstance();
        SeekerUploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeekerFillActivity.this, SeekerUploadActivity.class);
                startActivity(i);
                finish();
            }
        });
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID =  mAuth.getCurrentUser().getUid();
                String Firstname = firstName.getText().toString();
                String Email = userMail.getText().toString();
                String Age = age.getText().toString();
                String Address = eAddress.getText().toString();
                String Field = eField.getText().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("name",Firstname);
                user.put("email", Email);
                user.put("age",Age);
                user.put("location", Address);
                user.put("skills", Field);

                db.collection("job_seeker").document(userID)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SeekerFillActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                                Toast.makeText(SeekerFillActivity.this, e.getMessage() ,Toast.LENGTH_SHORT).show();


                            }
                        });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent btnClick = new Intent(SeekerFillActivity.this, SeekerMainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }
}