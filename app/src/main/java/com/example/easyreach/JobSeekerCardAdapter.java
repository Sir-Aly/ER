package com.example.easyreach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.List;

public class JobSeekerCardAdapter extends CardStackView.Adapter<JobSeekerCardAdapter.ViewHolder> {
    private List<JobSeeker> jobSeekers;

    public JobSeekerCardAdapter(List<JobSeeker> jobSeekers) {
        this.jobSeekers = jobSeekers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_job_seeker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JobSeeker jobSeeker = jobSeekers.get(position);
        holder.nameTextView.setText(jobSeeker.getName());
        holder.skillsTextView.setText(jobSeeker.getSkills());
        holder.locationTextView.setText(jobSeeker.getLocation());
        holder.emailTextView.setText(jobSeeker.getEmail());
        holder.IDTextView.setText(jobSeeker.getUID());
        Glide.with(holder.photoImageView.getContext())
                .load(jobSeeker.getProfileUrl())
                .into(holder.photoImageView);

        holder.addToInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add your code to handle adding job seekers to the interested list
                // For example, you can call a method from the MainActivity passing the job seeker information
                // MainActivity.addToInterestedList(jobSeeker);

                // Example implementation:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userID = user.getUid();
                    String sID = jobSeeker.getUID().toString();
                    Add add = new Add(jobSeeker.getName(), jobSeeker.getSkills(), jobSeeker.getLocation(), jobSeeker.getEmail(), jobSeeker.getUID(), jobSeeker.getProfileUrl());
                    FirebaseFirestore.getInstance().collection("user").document(userID).collection("Likes").document(sID).set(add).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(view.getContext(), "Job Seeker added to Interest list Successfully", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error handling
                                    Toast.makeText(view.getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobSeekers.size();
    }

    public static class ViewHolder extends CardStackView.ViewHolder {
        ImageView photoImageView;
        TextView nameTextView;
        TextView skillsTextView;
        TextView locationTextView;
        TextView emailTextView;
        TextView IDTextView;

        ImageButton addToInterestButton;

        public ViewHolder(View view) {
            super(view);
            photoImageView = view.findViewById(R.id.photoImageView);
            nameTextView = view.findViewById(R.id.nameTextView);
            skillsTextView = view.findViewById(R.id.skillsTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            IDTextView = view.findViewById(R.id.IDTEXT);

            addToInterestButton = view.findViewById(R.id.add_to_int);
        }
    }
}
