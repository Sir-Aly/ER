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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SwipeAdapter adapter;
    private List<Integer> list;


private DrawerLayout drawerLayout;
    int index = 1;
    private static final String TAG = "MainActivity";
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

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Fields, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String selectedCategory = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(this);
//        mSignOut = (TextView) findViewById(R.id.signOut);
        navigationView=findViewById(R.id.navigationView);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("JS");
        CollectionReference Usersref = db.collection("user");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String jobProviderUid = user.getUid();
// Get the job seeker's document reference
        DocumentReference jobSeekerDocRef = jobSeekersRef.document("Field").collection(selectedCategory).document("1" +index);

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


// Get a reference to the Job Seekers subcollection based on the selected category


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





    public void hi2(View view){
        Intent intent = new Intent(this,interested_list.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

//        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("JS");
        CollectionReference selectedCategoryRef = jobSeekersRef.document("Field").collection(text);
        DocumentReference documentReference = selectedCategoryRef.document("1");


        Query query = selectedCategoryRef;



        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the job seeker's data from the document snapshot
                    String name = documentSnapshot.getString("name");
                    String skills = documentSnapshot.getString("skills");
                    String location = documentSnapshot.getString("location");
                    String photoUrl = documentSnapshot.getString("profileUrl");
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
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        String name = document.getString("name");
//                        String skills = document.getString("skills");
//                        String location = document.getString("location");
//                        String photoUrl = document.getString("photoUrl");
//                        String email = document.getString("email");
//
//                        // Update the UI with the job seeker's data
//                        nameTextView.setText(name);
//                        skillsTextView.setText(skills);
//                        locationTextView.setText(location);
//                        Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
//                        emailTextView.setText(email);
//                        ImageUrlTextview.setText(photoUrl);
//                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
