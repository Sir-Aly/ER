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

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>
{

    public static final String EXTRA_NAME ="name";
    ArrayList<model> datalist;

    public myadapter(ArrayList<model> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.t1.setText(datalist.get(position).getName());
        holder.t2.setText(datalist.get(position).getEmail());
        holder.t3.setText(datalist.get(position).getSkills());
        Glide.with(holder.getImage()).load(datalist.get(position).getPhotoUrl()).into(holder.t4);
        holder.t6.setText(datalist.get(position).getUid());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String name = holder.t6.getText().toString();
                Intent intent = new Intent(view.getContext(),Massage_Field.class);
                intent.putExtra(EXTRA_NAME,name);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
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
