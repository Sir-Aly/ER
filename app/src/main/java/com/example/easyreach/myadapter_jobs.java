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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class myadapter_jobs extends RecyclerView.Adapter<myadapter_jobs.myviewholder>{

    ArrayList<model_jobs> datalist_jobs;
    public static final String EXTRA_NAME ="name";
    public static final String EXTRA_EMAIL ="Email";
    private Context context;
    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public myadapter_jobs(Context context, ArrayList<model_jobs> datalist_jobs) {
        this.context = context;
        this.datalist_jobs = datalist_jobs;
        mAuth = FirebaseAuth.getInstance();
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.jobs_interest_item,parent,false);
        return new myviewholder(view);
    }

    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.jTitle.setText(datalist_jobs.get(position).getjTitle() + " " +"Job");
        holder.j_description.setText(datalist_jobs.get(position).getjDescription());
        holder.j_location.setText(datalist_jobs.get(position).getjLocation());
        holder.j_requirment.setText(datalist_jobs.get(position).getjRequirements());
        holder.jSalary.setText(datalist_jobs.get(position).getjSalary()+"LE");
        holder.pUID.setText(datalist_jobs.get(position).getpUid());
        holder.pEmail.setText(datalist_jobs.get(position).getpEmail());
        Long Job_ID = datalist_jobs.get(position).getJob_id();
        long jobId = datalist_jobs.get(position).getJob_id();
        String jobID = String.valueOf(jobId);
        String JobID = String.valueOf(Job_ID);
        holder.jID.setText(JobID);
        Glide.with(holder.jobImage.getContext())
                .load(datalist_jobs.get(position).getJobImage())
                .into(holder.jobImage);

        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = holder.pUID.getText().toString();
                String pEmail = holder.pEmail.getText().toString();
                Intent intent = new Intent(view.getContext(), SeekerMessageActivity.class);
                intent.putExtra(EXTRA_NAME,name);
                intent.putExtra(EXTRA_EMAIL,pEmail);
                view.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the jobId

                // Start the activity to show the detailed view of the job
                Intent intent = new Intent(context, JobDetailActivity.class);
                intent.putExtra("jobID", jobID);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return datalist_jobs.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView j_description,j_location,j_requirment, jSalary, jTitle, pUID, pEmail, jID;
        RoundedImageView jobImage;
        ImageButton sendMessage;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            j_description=itemView.findViewById(R.id.j_description);
            j_location=itemView.findViewById(R.id.j_location);
            j_requirment=itemView.findViewById(R.id.j_requirment);
            jSalary =itemView.findViewById(R.id.j_salary);
            jTitle =itemView.findViewById(R.id.title);
            jobImage =itemView.findViewById(R.id.imageRound);
            pUID = itemView.findViewById(R.id.pUID);
            pEmail = itemView.findViewById(R.id.p_email);
            jID = itemView.findViewById(R.id.jobId);
            sendMessage = itemView.findViewById(R.id.sendMessage);
        }
        public ImageView getImage() {
            return jobImage;
        }
    }
    public void deleteItem(int position) {
        String userID = mAuth.getCurrentUser().getUid();
        Long Job_ID = datalist_jobs.get(position).getJob_id();
        String documentID = String.valueOf(Job_ID);

        db.collection("user").document(userID).collection("Likes").document(documentID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        datalist_jobs.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while deleting the document
                        Toast.makeText(context, "Failed to delete document", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
    }
}




