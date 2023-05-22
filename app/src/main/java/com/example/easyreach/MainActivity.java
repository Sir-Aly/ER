package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SwipeAdapter adapter;
    private List<Integer> list;


    private DrawerLayout drawerLayout;
    int index = 1;
    private static final String TAG = "MainActivity";
    ImageView photoImageView;
    String jobSeekerId, text;
    private NavigationView navigationView;
    ImageView leftArrow, rightArrow;
    private DocumentSnapshot currentJobSeeker;


    TextView nameTextView, skillsTextView, locationTextView, emailTextView, ImageUrlTextview,IDTEXT;
    //    private TextView mSignOut;
    private FirebaseAuth mAuth;


    //last update
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Fields, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String selectedCategory = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(this);
//        mSignOut = (TextView) findViewById(R.id.signOut);
        navigationView = findViewById(R.id.navigationView);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("JS");
        CollectionReference Usersref = db.collection("user");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String jobProviderUid = user.getUid();
// Get the job seeker's document reference
        DocumentReference jobSeekerDocRef = jobSeekersRef.document("Field").collection(selectedCategory).document("1" + index);

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
        IDTEXT = findViewById(R.id.Text_id);

        Button add_to_int = findViewById(R.id.add_to_int);
        String userID = mAuth.getCurrentUser().getUid();
//        CollectionReference selectedCategoryRef = jobSeekersRef.document("Field").collection("AI Developer");
//        Query query = selectedCategoryRef.orderBy(FieldPath.documentId());

// Get a reference to the Job Seekers subcollection based on the selected category


        add_to_int.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = nameTextView.getText().toString();
                String Skills = skillsTextView.getText().toString();
                String Location = locationTextView.getText().toString();
                String email = emailTextView.getText().toString();
                String Imageurl = ImageUrlTextview.getText().toString();
                String UID = IDTEXT.getText().toString();
                Add add = new Add(Name, Skills, Location, email, Imageurl,UID);
                Usersref.document(userID).collection("Likes").add(add);


            }
        });


// Retrieve the job seeker's data using the document reference


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
        drawerLayout = findViewById(R.id.drawerLayout);


        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);


    }


    public void hi2(View view) {
        Intent intent = new Intent(this, interested_list.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();

//        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("JS");
        CollectionReference selectedCategoryRef = jobSeekersRef.document("Field").collection(text);

        Query query = selectedCategoryRef.orderBy(FieldPath.documentId());
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> jobSeekers = queryDocumentSnapshots.getDocuments();
                        // Display the first job seeker in the list
                        showJobSeeker(jobSeekers.get(0));

                        // Set up the click listener for the right arrow button
                        rightArrow = findViewById(R.id.right_arrow);
                        rightArrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Check if currentJobSeeker is null
                                if (currentJobSeeker == null) {
                                    // If it is, get the first job seeker
                                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> jobSeekers = queryDocumentSnapshots.getDocuments();
                                            if (!jobSeekers.isEmpty()) {
                                                currentJobSeeker = jobSeekers.get(1);
                                                showJobSeeker(currentJobSeeker);
                                            }
                                        }
                                    });
                                } else {
                                    // If it is not null, get the next job seeker
                                    int currentIndex = jobSeekers.indexOf(currentJobSeeker);
                                    int nextIndex = currentIndex + 1;
                                    if (nextIndex < jobSeekers.size()) {
                                        currentJobSeeker = jobSeekers.get(nextIndex);
                                        showJobSeeker(currentJobSeeker);
                                    } else {
                                        Toast.makeText(MainActivity.this, "No more job seekers", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        leftArrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Check if currentJobSeeker is null or if it is the first job seeker
                                if (currentJobSeeker == null || jobSeekers.indexOf(currentJobSeeker) == 0) {
                                    // Show a message indicating that there are no previous job seekers
                                    Toast.makeText(MainActivity.this, "No more job seekers", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Get the previous job seeker
                                    int currentIndex = jobSeekers.indexOf(currentJobSeeker);
                                    int previousIndex = currentIndex - 1;
                                    currentJobSeeker = jobSeekers.get(previousIndex);
                                    showJobSeeker(currentJobSeeker);
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors here
                    }
                });
        //22/5



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void showJobSeeker(DocumentSnapshot jobSeekerDoc) {
        // Get the views to display the job seeker data
        String name = jobSeekerDoc.getString("name");
        String skills = jobSeekerDoc.getString("skills");
        String location = jobSeekerDoc.getString("location");
        String photoUrl = jobSeekerDoc.getString("profileUrl");
        String email = jobSeekerDoc.getString("email");
        String ID = jobSeekerDoc.getString("UID");

        // Update the UI with the job seeker's data
        nameTextView.setText(name);
        skillsTextView.setText(skills);
        locationTextView.setText(location);
        Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
        emailTextView.setText(email);
        ImageUrlTextview.setText(photoUrl);
        IDTEXT.setText(ID);
    }




}
