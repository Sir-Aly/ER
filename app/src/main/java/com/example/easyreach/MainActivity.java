package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yalantis.library.Koloda;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeAdapter adapter;
    private List<Integer> list;
    Koloda koloda;

private DrawerLayout drawerLayout;
private NavigationView navigationView;


//    private TextView mSignOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//            koloda = findViewById(R.id.koloda);
//            list = new ArrayList<>();
//            adapter = new SwipeAdapter(this, list);
//            koloda.setAdapter(adapter);


//        mSignOut = (TextView) findViewById(R.id.signOut);

        navigationView=findViewById(R.id.navigationView);
        mAuth = FirebaseAuth.getInstance();
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
                        Intent s = new Intent(MainActivity.this, AdminLogin.class);
                        startActivity(s);
                        finish();
                        return false;
                    case R.id.menuAboutUs:
                        Intent a = new Intent(MainActivity.this, AboutUsActivity.class);
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
        CollectionReference jobSeekersRef = db.collection("job_seeker");
        DocumentReference jobSeekerDocRef = jobSeekersRef.document("uAth9bVwFO4XjZ7V6QlO");
        final String TAG = "problem";
        jobSeekerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the job seeker's data from the document snapshot
                        String name = document.getString("name");
                        String skills = document.getString("skills");
                        String location = document.getString("location");
                        String age = document.getString("age");


                        // Update the card view with the job seeker's data
                        TextView nameText = findViewById(R.id.name_text);
                        TextView skillsText = findViewById(R.id.skills_text);
                        TextView locationText = findViewById(R.id.location_text);
                        TextView ageText = findViewById(R.id.age_text);

                        nameText.setText(name);
                        skillsText.setText(skills);
                        locationText.setText(location);
                        ageText.setText(age);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
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

    NavigationView navigationView = findViewById(R.id.navigationView);
    navigationView.setItemIconTintList(null);

//        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        TextView textTitle = findViewById(R.id.textTitle);
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
//                textTitle.setText(navDestination.getLabel());
//            }
//        });
    }

}