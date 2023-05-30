package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.List;

public class SeekerMainActivity extends AppCompatActivity {
    private SwipeAdapter adapter;
    private List<Integer> list;

    private DrawerLayout drawerLayout;
    ImageView profileView;
    private NavigationView navigationView;
    private CardStackView cardStackView;
    private JobsCardAdapter jAdapter;
    private RecyclerView recyclerView;
    private List<JobOffer> jobOffers = new ArrayList<>();
    //    private TextView mSignOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_main);



//        mSignOut = (TextView) findViewById(R.id.signOut);
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
                    case R.id.menuSeekerNotifications:
                        Intent NoTi = new Intent(SeekerMainActivity.this,jobs_viewer.class);
                        startActivity(NoTi);
                        finish();
                        return false;

                    case R.id.menuSeekerInbox:
                        Intent inbox = new Intent(SeekerMainActivity.this , inbox_viewer.class);
                        startActivity(inbox);
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
        CollectionReference jobSeekersRef = db.collection("Jobs");

        jobSeekersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Iterate through the documents and add each job seeker to the jobSeekers list
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                JobOffer jobOffer = documentSnapshot.toObject(JobOffer.class);
                                if (jobOffer != null) {
                                    jobOffers.add(jobOffer);
                                }
                            }
                        }

                        // Call the method to set up the CardStackView with the job seekers list
                        setupCardStackView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeekerMainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });

        NavigationView navigationView = findViewById(R.id.navigationSeekerView);
        navigationView.setItemIconTintList(null);


    }
    private void setupCardStackView() {
        // Create a CardStackView instance
        cardStackView = findViewById(R.id.cardStackView);
        jAdapter = new JobsCardAdapter(jobOffers);
        // Set the adapter on the CardStackView
        cardStackView.setAdapter(jAdapter);
    }

}