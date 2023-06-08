package com.example.easyreach;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ProfilePage extends AppCompatActivity {
    TextView pNameTv;
    TextView addressTv;
    TextView ageTv;
    ImageView backBtn;
    ImageButton editProfile;
    TextView fieldTv;
    TextView emailTv;
   RoundedImageView profilePictureImageView;
//    private Button mForgetPassword;
    private boolean loginBtnClicked;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        final String TAG = "problem";

        editProfile = findViewById(R.id.editProfile);
        profilePictureImageView = findViewById(R.id.ProfileImage);
        pNameTv= (TextView) findViewById(R.id.sName);
        addressTv= (TextView) findViewById(R.id.address);
        ageTv= (TextView) findViewById(R.id.age);
        fieldTv= (TextView) findViewById(R.id.field);
        emailTv= (TextView) findViewById(R.id.email);
        backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        mForgetPassword = (Button) findViewById(R.id.forgetPasswordButton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        String uMail = user.getEmail();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("Job Providers").document(currentid);
        Query dbref = firestore.collection("Job Providers").whereEqualTo("pEmail", uMail);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fill = new Intent(ProfilePage.this, OrgFillActivity.class);
                startActivity(fill);
            }
        });
        dbref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final String TAG = "problem";
                if (task.isSuccessful()) {
                    boolean isProfileDataFilled = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        if (document.contains("pField") && document.contains("pLocation") && document.contains("pFoundationYear") && document.contains("profileUrl")) {
                            String ufield = document.getString("pField");
                            if (!ufield.isEmpty()) {
                                isProfileDataFilled = true;
                                String orgName = document.getString("pName");
                                String uaddress = document.getString("pLocation");
                                String uage = document.getString("pFoundationYear");
                                String umail = document.getString("pEmail");

                                pNameTv.setText(orgName);
                                addressTv.setText(uaddress);
                                ageTv.setText(uage);
                                fieldTv.setText(ufield);
                                emailTv.setText(umail);
                            }
                        }
                    }
                    if (!isProfileDataFilled) {

                        Toast.makeText(ProfilePage.this, "Please Fill your data first", Toast.LENGTH_SHORT).show();
                        // Redirect the user to the FillActivity to fill the profile data
                        Intent intent = new Intent(ProfilePage.this, OrgFillActivity.class);
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
//        mForgetPassword.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent i = new Intent(ProfilePage.this, ForgetPasswordActivity.class  );
//                startActivity(i);
//                finish();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent btnClick = new Intent(ProfilePage.this, MainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }

}
