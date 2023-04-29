package com.example.easyreach;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SeekerMainActivity extends AppCompatActivity {
    private SwipeAdapter adapter;
    private List<Integer> list;

    private DrawerLayout drawerLayout;
    ImageView profileView;
    private NavigationView navigationView;


    //    private TextView mSignOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_main);



//        mSignOut = (TextView) findViewById(R.id.signOut);
        profileView = findViewById(R.id.profile_image);
        navigationView=findViewById(R.id.navigationSeekerView);
        mAuth = FirebaseAuth.getInstance();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.menuSeekerSignOut:
                        mAuth.signOut();
                        Intent i = new Intent(SeekerMainActivity.this, Choose_Login_And_Reg.class);
                        startActivity(i);
                        finish();
                        return false;
                    case R.id.menuSeekerProfile:
//                        mAuth.signOut();
                        Intent r = new Intent(SeekerMainActivity.this, SeekerProfileActivity.class);
                        startActivity(r);
                        finish();
                        return false;
                    case R.id.menuSeekerSettings:
                        Intent s = new Intent(SeekerMainActivity.this, SeekerFillActivity.class);
                        startActivity(s);
                        finish();
                        return false;
                    case R.id.menuSeekerAboutUs:
                        Intent a = new Intent(SeekerMainActivity.this, SeekerAboutUsActivity.class);
                        startActivity(a);
                        finish();
                        return false;
                }

                return false;
            }
        });
        drawerLayout=findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("job_offerer");
        DocumentReference jobSeekerDocRef = jobSeekersRef.document("YsPMkdQqwgcWd2jOkYLYGOgwZPu1");
        final String TAG = "problem";
        jobSeekerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the job seeker's data from the document snapshot
                        String name = document.getString("name");
                        String title = document.getString("job_title");
                        String location = document.getString("Location");
                        String description = document.getString("job_description");



                        // Update the card view with the job seeker's data
                        TextView nameText = findViewById(R.id.org_name_text);
                        TextView titleText = findViewById(R.id.job_title_text);
                        TextView locationText = findViewById(R.id.org_location_text);
                        TextView descriptionText = findViewById(R.id.job_description);

                        nameText.setText(name);
                        titleText.setText(title);
                        locationText.setText(location);
                        descriptionText.setText(description);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        String uMail = user.getEmail();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("job_offerer").document("YsPMkdQqwgcWd2jOkYLYGOgwZPu1");
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the image URL from the document
                    String imageUrl = documentSnapshot.getString("profilePictureUrl");
                    Context context = getApplicationContext();

                    if (imageUrl != null) {
                        // Here's an example of how to use Glide:
                        Glide.with(context)
                                .load(imageUrl)
                                .into(profileView);
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

//        mSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            mAuth.signOut();
//                Intent i = new Intent(MainActivity.this, Choose_Login_And_Reg.class);
//                startActivity(i);
//                finish();
//                return;
//            }
//        });

        NavigationView navigationView = findViewById(R.id.navigationSeekerView);
        navigationView.setItemIconTintList(null);


    }

}