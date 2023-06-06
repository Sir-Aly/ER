package com.example.easyreach;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class SeekerProfileActivity extends AppCompatActivity {
    TextView pNameTv, nameTv, addressTv, ageTv, fieldTv, emailTv,YoETv;
    RoundedImageView profilePictureImageView;
//    ImageButton cvImage;
//    private Button mForgetPassword;
    private boolean loginBtnClicked;
    private FirebaseAuth mAuth;
    ImageButton editProfile;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_profile);
        final String TAG = "problem";

        editProfile = findViewById(R.id.editProfile);

        profilePictureImageView = findViewById(R.id.ProfileImage);
        nameTv= (TextView) findViewById(R.id.sName);
        addressTv= (TextView) findViewById(R.id.address);
        ageTv= (TextView) findViewById(R.id.age);
        fieldTv= (TextView) findViewById(R.id.field);
        emailTv= (TextView) findViewById(R.id.email);
        YoETv = (TextView) findViewById(R.id.sYoE);
//        cvImage = (ImageButton) findViewById(R.id.profileCV);
//        mForgetPassword = (Button) findViewById(R.id.forgetPasswordButton);
//        cvImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Get the CV URL from the ImageButton's tag
//                String cvUrl = (String) cvImage.getTag();
//
//                // Open the CV using an appropriate PDF viewer application
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(cvUrl), "application/pdf");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                try {
//                    view.getContext().startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    // Handle the case where a PDF viewer application is not available
//                    Toast.makeText(view.getContext(), "No PDF viewer found", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fill = new Intent(SeekerProfileActivity.this, SeekerFillActivity.class);
                startActivity(fill);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        String uMail = user.getEmail();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Job Seekers").document(currentid);
        Query dbref = firestore.collection("Job Seekers").whereEqualTo("sEmail", uMail);
        dbref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final String TAG = "problem";
                if (task.isSuccessful()) {
                    boolean isProfileDataFilled = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        if (document.contains("sField") && document.contains("sLocation") && document.contains("sDescription") && document.contains("sImageUrl")) {
                            String sField = document.get("sField").toString();
                            if (!sField.isEmpty()) {
                                isProfileDataFilled = true;
                                String sName = document.get("sName").toString();
                                String sLocation = document.get("sLocation").toString();
                                String sAge = document.get("sAge").toString();
                                String cvUrl = document.get("cvUrl").toString();

                                String sEmail = document.get("sEmail").toString();
                                String sYoE = document.get("sYoE").toString();

                                nameTv.setText(sName);
                                addressTv.setText(sLocation);
                                ageTv.setText(sAge);
                                YoETv.setText(sYoE);
                                fieldTv.setText(sField);
                                emailTv.setText(sEmail);
//                                cvImage.setTag(cvUrl);

                            }
                        }
                    }
                    if (!isProfileDataFilled) {

                        Toast.makeText(SeekerProfileActivity.this, "Please Fill your data first", Toast.LENGTH_SHORT).show();
                        // Redirect the user to the FillActivity to fill the profile data
                        Intent intent = new Intent(SeekerProfileActivity.this, SeekerFillActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the image URL from the document
                    String imageUrl = documentSnapshot.getString("sImageUrl");
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
//        mForgetPassword.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent i = new Intent(SeekerProfileActivity.this, ForgetPasswordActivity.class  );
//                startActivity(i);
//                finish();
//            }
//        });

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
        Intent btnClick = new Intent(SeekerProfileActivity.this, SeekerMainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }

}
