package com.example.easyreach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myadapter_jobs extends RecyclerView.Adapter<myadapter_jobs.myviewholder>{

    ArrayList<model_jobs> datalist_jobs;
    public static final String EXTRA_NAME ="name";

    public myadapter_jobs(ArrayList<model_jobs> datalist_jobs) {
        this.datalist_jobs = datalist_jobs;
    }
    @NonNull
    @Override
    public myadapter_jobs.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_jobs,parent,false);
        return new myviewholder(view);
    }

    public void onBindViewHolder(@NonNull myadapter_jobs.myviewholder holder, int position) {
        holder.title.setText(datalist_jobs.get(position).getjTitle() + " " +"Job");
        holder.j_description.setText(datalist_jobs.get(position).getjDescription());
        holder.j_location.setText(datalist_jobs.get(position).getjLocation());
        holder.j_requirment.setText(datalist_jobs.get(position).getjRequirements());
        holder.salary.setText(datalist_jobs.get(position).getjSalary()+"LE");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here
            }
        });

    }
    @Override
    public int getItemCount() {
        return datalist_jobs.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView j_description,j_location,j_requirment,salary,title;
        private ImageView t4;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            j_description=itemView.findViewById(R.id.j_description);
            j_location=itemView.findViewById(R.id.j_location);
            j_requirment=itemView.findViewById(R.id.j_requirment);
            t4=itemView.findViewById(R.id.holderImage);
            salary=itemView.findViewById(R.id.salary);
            title=itemView.findViewById(R.id.title);
        }
        public ImageView getImage() {
            return t4;
        }
    }
}




