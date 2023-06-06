package com.example.easyreach;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myadapter_inbox extends RecyclerView.Adapter<myadapter_inbox.myviewholder>{

    ArrayList<model_inbox> datalist_inbox;
    public static final String EXTRA_USER_ID ="name";
    public static final String EXTRA_MESSAGE="message";

    String messageaccepted = "OFFER ACCEPTED";

    public myadapter_inbox(ArrayList<model_inbox> datalist_inbox) {
        this.datalist_inbox = datalist_inbox;
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_inbox,parent,false);
        return new myviewholder(view);
    }

    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.t1.setText(datalist_inbox.get(position).getmessage());
        holder.t2.setText(datalist_inbox.get(position).getfrom());
        holder.t3.setText(datalist_inbox.get(position).getfuid());

        if (holder.t1.getText().equals(messageaccepted)){
            holder.cardview_rec.setCardBackgroundColor(Color.GREEN);
            holder.t1.setTextColor(Color.BLACK);

        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String Fullmessage =holder.t1.getText().toString();
                String From_USER_iD = holder.t3.getText().toString();
                Intent intent = new Intent(view.getContext(), Full_Massage_Viewer_Replay.class);
                intent.putExtra(EXTRA_USER_ID,From_USER_iD);
                intent.putExtra(EXTRA_MESSAGE,Fullmessage);
                view.getContext().startActivity(intent);

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
        CardView cardview_rec;
        private ImageView t4;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.t1);
            t2=itemView.findViewById(R.id.t2);
            t3=itemView.findViewById(R.id.t3);
            cardview_rec = itemView.findViewById(R.id.cardview_rec);
//            t4=itemView.findViewById(R.id.holderImage);
//            t5=itemView.findViewById(R.id.t5);
//            t6=itemView.findViewById(R.id.t6);
        }
        public ImageView getImage() {
            return t4;
        }
    }
}




