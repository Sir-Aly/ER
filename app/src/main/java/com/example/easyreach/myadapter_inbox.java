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

import java.util.ArrayList;

public class myadapter_inbox extends RecyclerView.Adapter<myadapter_inbox.myviewholder>{

    ArrayList<model_inbox> datalist_inbox;
    public static final String EXTRA_NAME ="name";

    public myadapter_inbox(ArrayList<model_inbox> datalist_inbox) {
        this.datalist_inbox = datalist_inbox;
    }
    @NonNull
    @Override
    public myadapter_inbox.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_inbox,parent,false);
        return new myviewholder(view);
    }

    public void onBindViewHolder(@NonNull myadapter_inbox.myviewholder holder, int position) {
        holder.t1.setText(datalist_inbox.get(position).getmessage());
        holder.t2.setText(datalist_inbox.get(position).getfrom());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here
            }
        });

    }
    @Override
    public int getItemCount() {
        return datalist_inbox.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView t1,t2,t3,t5,t6;
        private ImageView t4;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.t1);
            t2=itemView.findViewById(R.id.t2);
            t3=itemView.findViewById(R.id.t3);
            t4=itemView.findViewById(R.id.holderImage);
            t5=itemView.findViewById(R.id.t5);
            t6=itemView.findViewById(R.id.t6);
        }
        public ImageView getImage() {
            return t4;
        }
    }
}




