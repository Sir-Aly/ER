package com.example.easyreach;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class myadapter_jobs extends RecyclerView.Adapter<myadapter_jobs.myviewholder>{

    ArrayList<model_jobs> datalist_jobs;
    public static final String EXTRA_NAME ="name";
    public static final String EXTRA_EMAIL ="Email";

    public myadapter_jobs(ArrayList<model_jobs> datalist_jobs) {
        this.datalist_jobs = datalist_jobs;
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
        Glide.with(holder.jobImage.getContext())
                .load(datalist_jobs.get(position).getJobImage())
                .into(holder.jobImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    }
    @Override
    public int getItemCount() {
        return datalist_jobs.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView j_description,j_location,j_requirment, jSalary, jTitle, pUID, pEmail;
        RoundedImageView jobImage;

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
        }
        public ImageView getImage() {
            return jobImage;
        }
    }
}




