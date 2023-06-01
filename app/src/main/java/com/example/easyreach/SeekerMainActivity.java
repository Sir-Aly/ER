package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.List;

public class SeekerMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private SwipeAdapter adapter;
    private List<Integer> list;

    private DrawerLayout drawerLayout;
    ImageView profileView;
    private NavigationView navigationView;
    String text;
    private static final String TAG = "SeekerMainActivity";
    private CardStackView cardStackView;
    private JobsCardAdapter jAdapter;
    public ImageButton seeker_int;
    private RecyclerView recyclerView;
    private List<JobOffer> jobOffers = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_main);

        Spinner spinner = findViewById(R.id.jobs_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Fields, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
            seeker_int = (ImageButton) findViewById(R.id.seeker_interested_list);
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

        seeker_int.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interest = new Intent(SeekerMainActivity.this, JobsInterestedListActivity.class);
                startActivity(interest);
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("JS").document("Field").collection(text);

        Query query = db.collection("Jobs").whereEqualTo("jField", text);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        jobOffers.clear();
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
                        Log.w(TAG, "Error getting documents.", e);
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    private void setupCardStackView() {
        // Create a CardStackView instance
        cardStackView = findViewById(R.id.cardStackView);
        jAdapter = new JobsCardAdapter(jobOffers);
        // Set the adapter on the CardStackView
        cardStackView.setAdapter(jAdapter);
    }

}