package com.example.easyreach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeAdapter adapter;
    private List<Integer> list;
    Koloda koloda;

private DrawerLayout drawerLayout;
private NavigationView navigationView;

    private TextView mSignOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            koloda = findViewById(R.id.koloda);
            list = new ArrayList<>();
            adapter = new SwipeAdapter(this, list);
            koloda.setAdapter(adapter);


        mSignOut = (TextView) findViewById(R.id.signOut);

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


        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mAuth.signOut();
                Intent i = new Intent(MainActivity.this, Choose_Login_And_Reg.class);
                startActivity(i);
                finish();
                return;
            }
        });

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