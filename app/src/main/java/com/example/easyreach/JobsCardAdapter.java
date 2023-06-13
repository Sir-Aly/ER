package com.example.easyreach;

import android.content.Context;
import android.content.Intent;
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

public class JobsCardAdapter extends CardStackView.Adapter<JobsCardAdapter.ViewHolder> {
    private List<JobOffer> jobOffers;
    private Context context;

    public JobsCardAdapter(List<JobOffer> jobOffers,  Context context) {
        this.jobOffers = jobOffers;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobs_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JobOffer jobOffer = jobOffers.get(position);
        holder.nameTextView.setText(jobOffer.getjTitle());
        holder.skillsTextView.setText(jobOffer.getjRequirements());
        holder.locationTextView.setText(jobOffer.getjLocation());
        holder.emailTextView.setText(jobOffer.getpEmail());
        holder.IDTextView.setText(jobOffer.getpUid());
        Long jID = jobOffer.getJob_id();
        String sID = String.valueOf(jID);
        holder.JobIDTextView.setText(sID);
        Glide.with(holder.photoImageView.getContext())
                .load(jobOffer.getJobImage())
                .into(holder.photoImageView);

        holder.addToInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userID = user.getUid();

                    JobOffer add = new JobOffer(jobOffer.getjTitle(), jobOffer.getjRequirements(), jobOffer.getjLocation(), jobOffer.getJobImage(), jobOffer.getpEmail(),jobOffer.getpUid(),jobOffer.getjDescription(), jobOffer.getJob_id(),jobOffer.getjSalary());
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the jobId
                long jobId = jobOffer.getJob_id();
                String jobID = String.valueOf(jobId);

                // Start the activity to show the detailed view of the job
                Intent intent = new Intent(context, JobDetailActivity.class);
                intent.putExtra("jobID", jobID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobOffers.size();
    }

    public static class ViewHolder extends CardStackView.ViewHolder {
        ImageView photoImageView;
        TextView nameTextView;
        TextView skillsTextView;
        TextView locationTextView;
        TextView emailTextView;
        TextView IDTextView;
        TextView JobIDTextView;
        ImageButton addToInterestButton;

        public ViewHolder(View view) {
            super(view);
            photoImageView = view.findViewById(R.id.photoImageView);
            nameTextView = view.findViewById(R.id.nameTextView);
            skillsTextView = view.findViewById(R.id.skillsTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            IDTextView = view.findViewById(R.id.IDTEXT);
            JobIDTextView = view.findViewById(R.id.JobID);
            addToInterestButton = view.findViewById(R.id.add_to_int);
        }
    }
}