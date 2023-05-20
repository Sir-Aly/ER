package com.example.easyreach;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SwipeAdapter adapter;
    private List<Integer> list;


private DrawerLayout drawerLayout;
    int index = 1;
ImageView photoImageView;
String jobSeekerId;
private NavigationView navigationView;
ImageView leftArrow, rightArrow;

TextView nameTextView, skillsTextView, locationTextView,emailTextView,ImageUrlTextview;
//    private TextView mSignOut;
    private FirebaseAuth mAuth;


//last update
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        mSignOut = (TextView) findViewById(R.id.signOut);
        navigationView=findViewById(R.id.navigationView);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("Job Seekers");
        CollectionReference Usersref = db.collection("user");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String jobProviderUid = user.getUid();
// Get the job seeker's document reference
        DocumentReference jobSeekerDocRef = jobSeekersRef.document("jobSeeker" + index);

// Get references to the left and right buttons
        leftArrow = findViewById(R.id.left_arrow);
        rightArrow = findViewById(R.id.right_arrow);

// Get references to UI elements that display the job seeker's data
        nameTextView = findViewById(R.id.name_text);
        skillsTextView = findViewById(R.id.skills_text);
        locationTextView = findViewById(R.id.location_text);
        emailTextView = findViewById(R.id.email_text);
        ImageUrlTextview = findViewById(R.id.Image_url);
        photoImageView = findViewById(R.id.profile_image);

        Button add_to_int = findViewById(R.id.add_to_int);
        String userID =  mAuth.getCurrentUser().getUid();

        add_to_int.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = nameTextView.getText().toString();
                String Skills= skillsTextView.getText().toString();
                String Location = locationTextView.getText().toString();
                String email = emailTextView.getText().toString();
                String Imageurl = ImageUrlTextview.getText().toString();
                Add add = new Add(Name,Skills,Location,email,Imageurl);
                Usersref.document(userID).collection("Likes").add(add);


            }
        });



// Retrieve the job seeker's data using the document reference
        jobSeekerDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the job seeker's data from the document snapshot
                    String name = documentSnapshot.getString("name");
                    String skills = documentSnapshot.getString("skills");
                    String photoUrl = documentSnapshot.getString("photoUrl");
                    String email = documentSnapshot.getString("email");

                    // Update the UI with the job seeker's data
                    nameTextView.setText(name);
                    skillsTextView.setText(skills);
                    Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
                    emailTextView.setText(email);
                    ImageUrlTextview.setText(photoUrl);

                    // Assign the function to the left button's click event
                    leftArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addSwipeDocument(jobSeekerDocRef, jobProviderUid, "left");
                            // Update the UI with the next job seeker's data
                            index++;
                            System.out.println(index);
                            getNextJobSeeker();


                            String Name = nameTextView.getText().toString();
                            String Skills= skillsTextView.getText().toString();
                            String Location = locationTextView.getText().toString();
                            String email = emailTextView.getText().toString();
                            String Imageurl = ImageUrlTextview.getText().toString();
                            Add add = new Add(Name,Skills,Location,email,Imageurl);
                            Usersref.document(userID).collection("Likes").add(add);
                        }
                    });







                    // Assign the function to the right button's click event
                    rightArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addSwipeDocument(jobSeekerDocRef, jobProviderUid, "right");
                            // Update the UI with the next job seeker's data
                            index++;
                            System.out.println(index);

                            getNextJobSeeker();
                        }
                    });
                }
            }
        });




        // Listen for changes to the job seeker's document and update the UI accordingly
        jobSeekerDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Get the job seeker's data from the document snapshot
                    String name = documentSnapshot.getString("name");
                    String skills = documentSnapshot.getString("skills");
                    String location = documentSnapshot.getString("location");
                    String photoUrl = documentSnapshot.getString("photoUrl");
                    String email = documentSnapshot.getString("email");

                    // Update the UI with the job seeker's data
                    nameTextView.setText(name);
                    skillsTextView.setText(skills);
                    locationTextView.setText(location);
                    Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
                    emailTextView.setText(email);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.menuSignOut:
                        mAuth.signOut();
                        Intent i = new Intent(MainActivity.this, Choose_Login_And_Reg.class);
                        startActivity(i);
                        finish();
                        return false;
                    case R.id.menuProfile:
//                        mAuth.signOut();
                        Intent r = new Intent(MainActivity.this, ProfilePage.class);
                        startActivity(r);
                        finish();
                        return false;
                    case R.id.menuSettings:
                        Intent s = new Intent(MainActivity.this, OrgFillActivity.class);
                        startActivity(s);
                        finish();
                        return false;
                    case R.id.menuAboutUs:
                        Intent a = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(a);
                        finish();
                        return false;
                    case R.id.jobAdding:
                        Intent add = new Intent(MainActivity.this, JobPostingActivity.class);
                        startActivity(add);
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

    NavigationView navigationView = findViewById(R.id.navigationView);
    navigationView.setItemIconTintList(null);


    }




    private void addSwipeDocument(DocumentReference jobSeekerDocRef, String jobProviderUid, String direction) {
        // Create a new swipe document
        Map<String, Object> swipeData = new HashMap<>();
        swipeData.put("jobProviderUid", jobProviderUid);
        swipeData.put("direction", direction);
        swipeData.put("timestamp", FieldValue.serverTimestamp());

        // Add the swipe document to the job seeker's "swipes" subcollection
        jobSeekerDocRef.collection("swipes").add(swipeData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Swipe document added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding swipe document", e);
                    }
                });
    }
    private void getNextJobSeeker() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("Job Seekers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String jobProviderUid = user.getUid();
        // Get the job seeker's document reference
        DocumentReference jobSeekerDocRef = jobSeekersRef.document("jobSeeker" +index);
        jobSeekerDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the job seeker's data from the document snapshot
                    String name = documentSnapshot.getString("name");
                    String skills = documentSnapshot.getString("skills");
                    String location = documentSnapshot.getString("location");
                    String photoUrl = documentSnapshot.getString("photoUrl");
                    String email = documentSnapshot.getString("email");

                    // Update the UI with the job seeker's data
                    nameTextView.setText(name);
                    skillsTextView.setText(skills);
                    locationTextView.setText(location);
                    Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
                    emailTextView.setText(email);
                    ImageUrlTextview.setText(photoUrl);

                    // Assign the function to the left button's click event

                    // Assign the function to the right button's click event
                }
            }
        });

        // Listen for changes to the job seeker's document and update the UI accordingly
        jobSeekerDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Get the job seeker's data from the document snapshot
                    String name = documentSnapshot.getString("name");
                    String skills = documentSnapshot.getString("skills");
                    String location = documentSnapshot.getString("location");
                    String photoUrl = documentSnapshot.getString("photoUrl");
                    String email = documentSnapshot.getString("email");


                    // Update the UI with the job seeker's data
                    nameTextView.setText(name);
                    skillsTextView.setText(skills);
                    locationTextView.setText(location);
                    emailTextView.setText(email);
                    Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
                }
            }
        });

        // Get a reference to the current job seeker's document
        DocumentReference currentJobSeekerDocRef = jobSeekersRef.document("jobSeeker" +index);
        // Query for the next job seeker based on the current job seeker's "swipes" subcollection
        Query nextJobSeekerQuery = jobSeekersRef.whereNotEqualTo("uid", "+index"  )
                .whereEqualTo("swipes" + jobProviderUid, null)
                .orderBy("uid")
                .limit(1);

        nextJobSeekerQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Get the next job seeker's document reference
                    DocumentSnapshot nextJobSeekerSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    DocumentReference nextJobSeekerDocRef = nextJobSeekerSnapshot.getReference();

                    // Update the current job seeker's document with the next job seeker's UID
                    Map<String, Object> swipeData = new HashMap<>();
                    swipeData.put(jobProviderUid, null);
                    currentJobSeekerDocRef.update("swipes", swipeData);

                    // Update the next job seeker's document with the current job seeker's UID
                    swipeData.put(jobProviderUid, "pending");
                    nextJobSeekerDocRef.update("swipes", swipeData);

                    // Update job seeker ID to the next job seeker's ID
                    jobSeekerId = nextJobSeekerSnapshot.getString("uid");
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle database error
                Log.w(TAG, "getNextJobSeeker:onFailure", e);
            }
        });

    }

    public void hi2(View view){
        Intent intent = new Intent(this,interested_list.class);
        startActivity(intent);
    }

}
