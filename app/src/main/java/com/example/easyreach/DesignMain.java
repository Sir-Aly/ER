package com.example.easyreach;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;
public class DesignMain extends AppCompatActivity {
    String jobSeekerId;
    Button leftButton, rightButton;
    // 1 / 5/ 2023
    // 22 Ali
    TextView nameTextView, skillsTextView;
    ImageView photoImageView;
    int index = 1;
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_design_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference jobSeekersRef = db.collection("Job Seekers");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String jobProviderUid = user.getUid();
// Get the job seeker's document reference
        DocumentReference jobSeekerDocRef = jobSeekersRef.document("jobSeeker" + index);

// Get references to the left and right buttons
         leftButton = findViewById(R.id.left_button);
         rightButton = findViewById(R.id.right_button);

// Get references to UI elements that display the job seeker's data
        nameTextView = findViewById(R.id.name_text_view);
         skillsTextView = findViewById(R.id.skills_text_view);
         photoImageView = findViewById(R.id.photo_image_view);

// Retrieve the job seeker's data using the document reference
        jobSeekerDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the job seeker's data from the document snapshot
                    String name = documentSnapshot.getString("name");
                    String skills = documentSnapshot.getString("skills");
                    String photoUrl = documentSnapshot.getString("photoUrl");

                    // Update the UI with the job seeker's data
                    nameTextView.setText(name);
                    skillsTextView.setText(skills);
                    Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);

                    // Assign the function to the left button's click event
                    leftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addSwipeDocument(jobSeekerDocRef, jobProviderUid, "left");
                            // Update the UI with the next job seeker's data
                           index--;
                            System.out.println(index);
                            getNextJobSeeker();
                        }
                    });

                    // Assign the function to the right button's click event
                    rightButton.setOnClickListener(new View.OnClickListener() {
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
                    String photoUrl = documentSnapshot.getString("photoUrl");

                    // Update the UI with the job seeker's data
                    nameTextView.setText(name);
                    skillsTextView.setText(skills);
                    Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
                }
            }
        });


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
                        String photoUrl = documentSnapshot.getString("photoUrl");

                        // Update the UI with the job seeker's data
                        nameTextView.setText(name);
                        skillsTextView.setText(skills);
                        Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);

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
                        String photoUrl = documentSnapshot.getString("photoUrl");

                        // Update the UI with the job seeker's data
                        nameTextView.setText(name);
                        skillsTextView.setText(skills);
                        Glide.with(photoImageView.getContext()).load(photoUrl).into(photoImageView);
                    }
                }
            });

            // Get a reference to the current job seeker's document
            DocumentReference currentJobSeekerDocRef = jobSeekersRef.document("1" );


            // Query for the next job seeker based on the current job seeker's "swipes" subcollection
            Query nextJobSeekerQuery = jobSeekersRef.whereNotEqualTo("seeker_id", "1")
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
                        Toast.makeText(DesignMain.this, "No more Job Seekers", Toast.LENGTH_SHORT).show();
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
}