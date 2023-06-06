package com.example.easyreach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.myviewholder>{
    public ImageButton addToInterestButton;
    ArrayList<JobOffer> jobOffers;

    public JobsAdapter(ArrayList<JobOffer> jobOffers) {
        this.jobOffers = jobOffers;
    }
    @NonNull
    @Override
    public JobsAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_jobs,parent,false);
        return new JobsAdapter.myviewholder(view);    }

    public void onBindViewHolder(@NonNull JobsAdapter.myviewholder holder, int position) {
        JobOffer jobOffer = jobOffers.get(position);
        holder.jTitle.setText(jobOffers.get(position).getjTitle() + " " +"Job");
        holder.jDesc.setText(jobOffer.getjDescription());
        holder.j_location.setText(jobOffers.get(position).getjLocation());
        holder.j_requirment.setText(jobOffers.get(position).getjRequirements());
        holder.jSalary.setText(jobOffers.get(position).getjSalary()+"LE");
        holder.pUID.setText(jobOffers.get(position).getpUid());
        holder.pEmail.setText(jobOffer.getpEmail());
        Long jID = jobOffer.getJob_id();
        String sID = String.valueOf(jID);
        Glide.with(holder.jobImage.getContext())
                .load(jobOffers.get(position).getJobImage())
                .into(holder.jobImage);

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



    }
    @Override
    public int getItemCount() {
        return jobOffers.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView j_location,j_requirment, jSalary, jTitle, pUID, pEmail, jDesc;
        RoundedImageView jobImage;
        ImageButton addToInterestButton;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            j_location=itemView.findViewById(R.id.j_location);
            j_requirment=itemView.findViewById(R.id.j_requirment);
            jSalary =itemView.findViewById(R.id.j_salary);
            jTitle =itemView.findViewById(R.id.title);
            jDesc = itemView.findViewById(R.id.j_description);
            pEmail = itemView.findViewById(R.id.p_email);
            jobImage =itemView.findViewById(R.id.imageRound);
            pUID = itemView.findViewById(R.id.pUID);
            addToInterestButton = itemView.findViewById(R.id.add_to_interest);
        }
        public ImageView getImage() {
            return jobImage;
        }
    }
}
