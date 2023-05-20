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

public class OrgFillActivity extends AppCompatActivity {

    EditText firstName, pDescription, eAddress, eField, userMail;
    MaterialButton Registerbtn, Uploadbtn;
    FirebaseFirestore db;
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_fill);

        db = FirebaseFirestore.getInstance();
        firstName = findViewById(R.id.firstName);
        userMail = findViewById(R.id.userMail);
        pDescription = findViewById(R.id.provider_description);
        eAddress = findViewById(R.id.address);
        eField = findViewById(R.id.field);
        Registerbtn = findViewById(R.id.btnRegister);
        Uploadbtn = findViewById(R.id.btnRegisterPic);
        mAuth = FirebaseAuth.getInstance();
        Uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrgFillActivity.this, UploadActivity.class);
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
                String Description = pDescription.getText().toString();
                String Address = eAddress.getText().toString();
                String Field = eField.getText().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("pName",Firstname);
                user.put("pEmail", Email);
                user.put("pDescription",Description);
                user.put("pLocation", Address);
                user.put("pField", Field);

                db.collection("Job Providers").document(userID)
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