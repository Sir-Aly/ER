package com.example.easyreach;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SeekerFillActivity extends AppCompatActivity {

    EditText firstName, age, eAddress, eField, userMail;
    MaterialButton RegisterBtn, SeekerUploadBtn;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "SeekerFillActivity";
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
        RegisterBtn = findViewById(R.id.btnRegister);
        SeekerUploadBtn = findViewById(R.id.seekerBtnRegisterPic);
        mAuth = FirebaseAuth.getInstance();
        CollectionReference graphicDesignerRef = db.collection("JS").document("Field").collection("Graphic Designer");

        SeekerUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeekerFillActivity.this, SeekerUploadActivity.class);
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
                String Field = eField.getText().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("name",Firstname);
                user.put("email", Email);
                user.put("age",Age);
                user.put("location", Address);
                user.put("skills", Field);
                user.put("UID", userID);

                graphicDesignerRef.whereEqualTo("UID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()) {

                                        graphicDesignerRef.document("GD_ID").get().addOnCompleteListener(tasks -> {
                                            if (tasks.isSuccessful()) {
                                                DocumentSnapshot document = tasks.getResult();
                                                if (document.exists()) {
                                                    long lastSeekerId = document.getLong("gd_last_id");
                                                    long newSeekerId = lastSeekerId + 1;

                                                    Map<String, Object> Seeker = new HashMap<>();
                                                    Seeker.put("seeker_id", newSeekerId);
                                                    Seeker.put("name",Firstname);
                                                    Seeker.put("UID", userID);
                                                    Seeker.put("email", Email);
                                                    Seeker.put("age",Age);
                                                    Seeker.put("location", Address);
                                                    Seeker.put("skills", Field);
                                                    graphicDesignerRef.document(String.valueOf(newSeekerId)).set(Seeker);
                                                    graphicDesignerRef.document("GD_ID").update("gd_last_id", newSeekerId);
                                                }
                                            }
                                        });
                                    } else {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                        String documentId = documentSnapshot.getId();
                                        graphicDesignerRef.document(documentId)
                                                .set(user, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot updated with ID: " + documentId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

//                graphicDesignerRef.document(userID)
//                        .set(user)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(SeekerFillActivity.this,"Successful",Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull @NotNull Exception e) {
//
//                                Toast.makeText(SeekerFillActivity.this, e.getMessage() ,Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        });

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