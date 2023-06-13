package com.example.easyreach;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class SeekerMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private SwipeAdapter adapter;
    private List<Integer> list;

    private DrawerLayout drawerLayout;
    ImageView profileView;
    private NavigationView navigationView;
    String text;
    private String cvUrl;
    private boolean doubleBackToExitPressedOnce = false;
    private static final String TAG = "SeekerMainActivity";
    private CardStackView cardStackView;
    private JobsCardAdapter jAdapter;
    private ImageButton btnMain, btnListedJobs;
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
                R.array.Fields, R.layout.custom_simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
            seeker_int = (ImageButton) findViewById(R.id.seeker_interested_list);
//        mSignOut = (TextView) findViewById(R.id.signOut);

        navigationView=findViewById(R.id.navigationSeekerView);
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImageView = headerView.findViewById(R.id.profileImageView);
        Menu menu = navigationView.getMenu();
        MenuItem menuCvItem = menu.findItem(R.id.menuCV);
        ImageView profileIconImageView = headerView.findViewById(R.id.profileIconImageView);
        TextView nameTextView = headerView.findViewById(R.id.nameTextView);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userRef = firestore.collection("Job Seekers").document(currentUserId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("sName");
                String profileImageUri = documentSnapshot.getString("sImageUrl");

                if (name != null && !name.isEmpty()) {
                    nameTextView.setText(name);
                } else {
                    // Handle the case where the name field is empty
                    nameTextView.setText("Name");
                }

                if (profileImageUri != null && !profileImageUri.isEmpty()) {
                    // Load the profile image using your preferred image loading library (e.g., Picasso, Glide, etc.)
                    // For example, using Picasso:
                    Picasso.get().load(profileImageUri).into(profileImageView);
                } else {
                    // Handle the case where the profile image URL is empty
                    // You can set a default placeholder image or hide the profile image view
                    profileImageView.setImageResource(R.drawable.ali);
                }
            }
        }).addOnFailureListener(e -> {
            // Handle the failure to retrieve the user data from Firestore
        });
        profileIconImageView.setOnClickListener(v -> {
            // Open the ProfileActivity
            Intent intent = new Intent(SeekerMainActivity.this, SeekerProfileActivity.class);
            startActivity(intent);
        });
        menuCvItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DocumentReference userRef = firestore.collection("Job Seekers").document(currentUserId);
                userRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("sName");
                        String cvUrl = documentSnapshot.getString("cvUrl");
                        String profileImageUri = documentSnapshot.getString("sImageUrl");

                        if (cvUrl != null && !cvUrl.isEmpty()) {
                            // Open the CV using an appropriate PDF viewer application
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(cvUrl), "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // Handle the case where a PDF viewer application is not available
                                Toast.makeText(SeekerMainActivity.this, "No PDF viewer found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SeekerMainActivity.this, "You didn't upload your CV", Toast.LENGTH_SHORT).show();
                        }

                        nameTextView.setText(name);
                        // Load the profile image using your preferred image loading library (e.g., Picasso, Glide, etc.)
                        // For example, using Picasso:
                        Picasso.get().load(profileImageUri).into(profileImageView);
                    }
                }).addOnFailureListener(e -> {
                    // Handle the failure to retrieve the user data from Firestore
                });

                return true;
            }
        });



        btnMain = findViewById(R.id.btnMain);
        btnListedJobs = findViewById(R.id.btnListedJobs);


        btnListedJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interested = new Intent(SeekerMainActivity.this, jobs_viewer.class);
                startActivity(interested);
                CustomIntent.customType(SeekerMainActivity.this,"left-to-right");
            }
        });
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SeekerMainActivity.this, "You are currently at Swipes", Toast.LENGTH_SHORT).show();

            }
        });

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

                    case R.id.sentMsgs:
                        Intent sent = new Intent(SeekerMainActivity.this, SentMessagesActivity.class);
                        startActivity(sent);
                        return false;

                    case R.id.menuSeekerInbox:
                        Intent inbox = new Intent(SeekerMainActivity.this , InboxActivity.class);
                        startActivity(inbox);
                        return false;
                    case R.id.menuSeekerSettings:
                        Intent s = new Intent(SeekerMainActivity.this, SeekerFillActivity.class);
                        startActivity(s);
                        return false;
                    case R.id.menuSeekerAboutUs:
                        Intent a = new Intent(SeekerMainActivity.this, SeekerAboutUsActivity.class);
                        startActivity(a);
                        return false;
                    case R.id.menuSeekerSupport:
                        Intent c = new Intent(SeekerMainActivity.this, MessagingActivity.class);
                        startActivity(c);
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

        Query query = db.collection("Jobs").whereEqualTo("jField", text);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        jobOffers.clear(); // Clear the existing job offers

                        // Iterate through the documents and add each matching job offer to the jobOffers list
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                JobOffer jobOffer = documentSnapshot.toObject(JobOffer.class);
                                if (jobOffer != null) {
                                    jobOffers.add(jobOffer);
                                }
                            }
                        }

                        // Call the method to set up the CardStackView with the job offers list
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
        jAdapter = new JobsCardAdapter(jobOffers, this);
        // Set the adapter on the CardStackView
        cardStackView.setAdapter(jAdapter);
        jAdapter.notifyDataSetChanged();
    }
}