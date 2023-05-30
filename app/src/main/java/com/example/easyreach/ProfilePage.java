package com.example.easyreach;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

public class ProfilePage extends AppCompatActivity {
    TextView pNameTv;
    TextView addressTv;
    TextView ageTv;
    TextView fieldTv;
    TextView emailTv;
   RoundedImageView profilePictureImageView;
    private Button mForgetPassword;
    private boolean loginBtnClicked;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        final String TAG = "problem";


        profilePictureImageView = findViewById(R.id.ProfileImage);
        pNameTv= (TextView) findViewById(R.id.sName);
        addressTv= (TextView) findViewById(R.id.address);
        ageTv= (TextView) findViewById(R.id.age);
        fieldTv= (TextView) findViewById(R.id.field);
        emailTv= (TextView) findViewById(R.id.email);
        mForgetPassword = (Button) findViewById(R.id.forgetPasswordButton);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ProfilePage.this, ForgetPasswordActivity.class  );
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        String uMail = user.getEmail();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Job Providers").document(currentid);
        Query dbref = firestore.collection("Job Providers").whereEqualTo("pEmail", uMail);
        dbref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final String TAG = "problem";
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String orgName = document.get("pName").toString();
                        String uaddress = document.get("pLocation").toString();
                        String uage = document.get("pFoundationYear").toString();
                        String ufield = document.get("pField").toString();
                        String umail = document.get("pEmail").toString();

                        pNameTv.setText(orgName);
                        addressTv.setText(uaddress);
                        ageTv.setText(uage);
                        fieldTv.setText(ufield);
                        emailTv.setText(umail);

                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

//        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                final String TAG = "problem";
//                if (task.getResult().exists()){
//                    String fullName= task.getResult().getString("Fname");
//                    String uaddress = task.getResult().getString("Address");
//                    String uage = task.getResult().getString("Age");
//                    String ufield = task.getResult().getString("Field");
//                    String umail = task.getResult().getString("Semail");
//
//                    pNameTv.setText(fullName);
//                    nameTv.setText(fullName);
//                    addressTv.setText(uaddress);
//                    ageTv.setText(uage);
//                    fieldTv.setText(ufield);
//                    emailTv.setText(umail);
//                }
//                else {
//                    Log.w(TAG, "Error getting documents.", task.getException());
//                }
//            }
//        });
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the image URL from the document
                    String imageUrl = documentSnapshot.getString("profileUrl");
                    Context context = getApplicationContext();

                    if (imageUrl != null) {
                        // Here's an example of how to use Glide:
                        Glide.with(context)
                                .load(imageUrl)
                                .into(profilePictureImageView);
                    } else {
                        // Handle the case where the image URL is null
                    }
                } else {
                    // Handle the case where the document does not exist
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
//    Context context = getApplicationContext();
//    // Get the image URL from the document
//    String imageUrl = document.get("profileImageUrl").toString();
//
//
//
//                        Glide.with(context)
//            .load(imageUrl)
//                                .into(profilePictureImageView);
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent btnClick = new Intent(ProfilePage.this, MainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }

}
