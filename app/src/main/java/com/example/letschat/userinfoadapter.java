package com.example.letschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class userinfoadapter extends RecyclerView.Adapter<userinfoadapter.viewholder> {
    private Context context;
    private ArrayList<Users> userarraylist;

    public userinfoadapter(Context context, ArrayList<Users> userarraylist) {
        this.context = context;
        this.userarraylist = userarraylist;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userinfotemplate, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Users users = userarraylist.get(position);
        holder.username.setText(" Name: "+users.name);
        holder.userstatus.setText(" Status: "+users.status);
        holder.useremail.setText(" Email: "+users.email);
        holder.userphone.setText(" Phone: "+users.phone);

        Picasso.get().load(users.image).into(holder.userimage);
    }

    @Override
    public int getItemCount() {
        return userarraylist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimage;
        TextView username;
        TextView userstatus;
        TextView useremail, userphone;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.uploadimage);
            username = itemView.findViewById(R.id.uname);
            userstatus = itemView.findViewById(R.id.status);
            useremail = itemView.findViewById(R.id.email);
            userphone = itemView.findViewById(R.id.phone);
        }
    }
}
