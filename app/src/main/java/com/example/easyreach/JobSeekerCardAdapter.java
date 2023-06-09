package com.example.easyreach;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
        holder.nameTextView.setText(jobSeeker.getsName());
        holder.skillsTextView.setText(jobSeeker.getsField());
        holder.locationTextView.setText(jobSeeker.getsLocation());
        holder.emailTextView.setText(jobSeeker.getsEmail());
        holder.ageTextView.setText(jobSeeker.getsAge());
        holder.IDTextView.setText(jobSeeker.getUID());
        holder.CVImageButton.setTag(jobSeeker.getCvUrl());
        holder.YoETextView.setText(jobSeeker.getsYoE() + " years" );
        Glide.with(holder.photoImageView.getContext())
                .load(jobSeeker.getsImageUrl())
                .into(holder.photoImageView);

        holder.addToInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userID = user.getUid();
                    String sID = jobSeeker.getUID().toString();
                    JobSeeker add = new JobSeeker(jobSeeker.getsName(), jobSeeker.getsField(),jobSeeker.getsAge(),jobSeeker.getsYoE(), jobSeeker.getsLocation(), jobSeeker.getsImageUrl(),jobSeeker.getsEmail() , jobSeeker.getUID(), jobSeeker.getCvUrl());
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
        holder.CVImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cvUrl = (String) view.getTag();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(cvUrl), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(view.getContext(), "No PDF viewer found", Toast.LENGTH_SHORT).show();
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
        TextView ageTextView;
        TextView YoETextView;
        TextView IDTextView;

        ImageButton addToInterestButton, CVImageButton;

        public ViewHolder(View view) {
            super(view);
            photoImageView = view.findViewById(R.id.photoImageView);
            nameTextView = view.findViewById(R.id.nameTextView);
            ageTextView = view.findViewById(R.id.AgeTextView);
            YoETextView = view.findViewById(R.id.YoETextField);
            skillsTextView = view.findViewById(R.id.skillsTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            IDTextView = view.findViewById(R.id.IDTEXT);

            addToInterestButton = view.findViewById(R.id.add_to_int);
            CVImageButton = view.findViewById(R.id.CV);
        }
    }
}
