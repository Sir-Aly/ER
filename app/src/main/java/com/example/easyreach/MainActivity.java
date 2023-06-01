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
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.view.GravityCompat;
    import androidx.drawerlayout.widget.DrawerLayout;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.material.navigation.NavigationView;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.CollectionReference;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.Query;
    import com.google.firebase.firestore.QuerySnapshot;
    import com.yuyakaido.android.cardstackview.CardStackView;

    import java.util.ArrayList;
    import java.util.List;

    import maes.tech.intentanim.CustomIntent;

    public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

        private List<Integer> list;


        private DrawerLayout drawerLayout;
        int index = 1;
        private static final String TAG = "MainActivity";
        ImageView photoImageView;
        String jobSeekerId, text;
        private ImageButton btnMain;
        private ImageButton btnAddJob;
        private ImageButton btnInterestedList, orgInterest;
        private NavigationView navigationView;
        private DocumentSnapshot currentJobSeeker;
        private CardStackView cardStackView;
        private JobSeekerCardAdapter sAdapter;
        private RecyclerView recyclerView;
        private List<JobSeeker> jobSeekers = new ArrayList<>();
        private int currentIndex;
        TextView nameTextView, skillsTextView, locationTextView, emailTextView, ImageUrlTextview,IDTEXT;
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

            btnMain = findViewById(R.id.btnMain);
            btnAddJob = findViewById(R.id.btnAddJob);
            btnInterestedList = findViewById(R.id.btnInterestedList);
            orgInterest = findViewById(R.id.interest_org);
            btnMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent main = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(main);
                }
            });

//            btnAddJob.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent addJob = new Intent(MainActivity.this, JobPostingActivity.class);
//                    startActivity(addJob);
//                }
//            });

            btnInterestedList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent interested = new Intent(MainActivity.this, jobs_viewer.class);
                    startActivity(interested);
                    CustomIntent.customType(MainActivity.this,"left-to-right");
                }
            });
            orgInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent orgInt = new Intent(MainActivity.this, interested_list.class);
                    startActivity(orgInt);
                }
            });
    //        mSignOut = (TextView) findViewById(R.id.signOut);
            navigationView = findViewById(R.id.navigationView);

            mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference jobSeekersRef = db.collection("JS").document("Field").collection("AI Developer");
            CollectionReference Usersref = db.collection("user");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




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
                            Intent r = new Intent(MainActivity.this, ProfilePage.class);
                            startActivity(r);
                            finish();
                            return false;
                        case R.id.menuInbox:
                            Intent inbox = new Intent(MainActivity.this, inbox_viewer.class);
                            startActivity(inbox);
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
        public void floating_method(View view){
            Intent intent = new Intent(this,JobPostingActivity.class);
            startActivity(intent);
            CustomIntent.customType(MainActivity.this,"bottom-to-up");
        }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            text = parent.getItemAtPosition(position).toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("Job Seekers").whereEqualTo("sField", text);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            jobSeekers.clear();
                            // Iterate through the documents and add each job seeker to the jobSeekers list
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    JobSeeker jobSeeker = documentSnapshot.toObject(JobSeeker.class);
                                    if (jobSeeker != null) {
                                        jobSeekers.add(jobSeeker);
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
            sAdapter = new JobSeekerCardAdapter(jobSeekers);
            // Set the adapter on the CardStackView
            cardStackView.setAdapter(sAdapter);
        }


    }
