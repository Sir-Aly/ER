package com.example.easyreach;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>  {
    private Context context;
    public static final String EXTRA_NAME ="name";
    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String EXTRA_Email ="Email";
    private void composeEmail(String receiverEmail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + receiverEmail));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiverEmail});
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    ArrayList<model> datalist;

    public myadapter(Context context, ArrayList<model> datalist) {
        this.context = context;
        this.datalist = datalist;
        mAuth = FirebaseAuth.getInstance();
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
                String sEmail = holder.seekerEmail.getText().toString();
                String name = holder.UID.getText().toString();
                Intent intent = new Intent(view.getContext(), Message_Field.class);
                intent.putExtra(EXTRA_NAME,name);
                intent.putExtra(EXTRA_Email,sEmail);
                view.getContext().startActivity(intent);
            }
        });
        holder.seekerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverEmail = holder.seekerEmail.getText().toString();
                composeEmail(receiverEmail);

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

    static class myviewholder extends RecyclerView.ViewHolder
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
    public void deleteItem(int position) {
        String userID = mAuth.getCurrentUser().getUid();
        String documentID = datalist.get(position).getUID();

        db.collection("user").document(userID).collection("Likes").document(documentID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        datalist.remove(position);
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
