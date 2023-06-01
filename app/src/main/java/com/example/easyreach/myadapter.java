package com.example.easyreach;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;


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
        holder.seekerName.setText(datalist.get(position).getsName());
        holder.seekerEmail.setText(datalist.get(position).getsEmail());
        holder.seekerAge.setText(datalist.get(position).getsAge());
        holder.seekerField.setText(datalist.get(position).getsField());
        Glide.with(holder.getImage()).load(datalist.get(position).getsImageUrl()).into(holder.seekerImage);
        holder.seekerLocation.setText(datalist.get(position).getsLocation());
        holder.seekerYoE.setText(datalist.get(position).getsYoE());
        holder.UID.setText(datalist.get(position).getUID());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String name = holder.UID.getText().toString();
                Intent intent = new Intent(view.getContext(), Message_Field.class);
                intent.putExtra(EXTRA_NAME,name);
                view.getContext().startActivity(intent);
            }
        });

    }

//    public void deleteItem(int position) {
//     getSnapshots().getSnapshot(position).getReference().delete();
//    }



    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView seekerName, seekerAge, seekerField, seekerLocation, seekerYoE, seekerEmail, UID;
        private ImageView seekerImage;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            seekerName =itemView.findViewById(R.id.sName);
            seekerAge =itemView.findViewById(R.id.seeker_age);
            seekerField =itemView.findViewById(R.id.s_field);
            seekerImage =itemView.findViewById(R.id.imageRound);
            seekerLocation =itemView.findViewById(R.id.s_location);
            UID = itemView.findViewById(R.id.UID);
            seekerYoE =itemView.findViewById(R.id.sYoe);
            seekerEmail = itemView.findViewById(R.id.s_email);
        }
        public ImageView getImage() {
            return seekerImage;
        }

    }
}
