package com.example.easyreach;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

public class ProviderInfoActivity extends AppCompatActivity {

    TextView pNameTv;
    TextView addressTv;
    TextView ageTv;
    ImageButton editProfile;
    TextView fieldTv;
    TextView emailTv;
    RoundedImageView profilePictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_info);

        profilePictureImageView = findViewById(R.id.ProfileImage);
        pNameTv= (TextView) findViewById(R.id.sName);
        addressTv= (TextView) findViewById(R.id.address);
        ageTv= (TextView) findViewById(R.id.age);
        fieldTv= (TextView) findViewById(R.id.field);
        emailTv= (TextView) findViewById(R.id.email);

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backAnimationView.playAnimation();
                onBackPressed();
            }
        });
        String pInfo = getIntent().getStringExtra("providerInfo");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query dbref = firestore.collection("Job Providers").whereEqualTo("pEmail", pInfo);

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
                                String orgName = document.getString("pName");
                                String uaddress = document.getString("pLocation");
                                String uage = document.getString("pFoundationYear");
                                String umail = document.getString("pEmail");
                                String ImageUrl = document.getString("profileUrl");

                                Context context = getApplicationContext();
                            if (ImageUrl != null) {
                                Glide.with(context)
                                        .load(ImageUrl)
                                        .into(profilePictureImageView);
                            } else {
                            }

                                pNameTv.setText(orgName);
                                addressTv.setText(uaddress);
                                ageTv.setText(uage);
                                fieldTv.setText(ufield);
                                emailTv.setText(umail);
                            }
                        }
                    }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}