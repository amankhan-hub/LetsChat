package com.example.letschat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Useradapter extends RecyclerView.Adapter<Useradapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Users> userarraylist;
    public Useradapter(MainActivity mainActivity, ArrayList<Users> userarraylist) {
    this.mainActivity=mainActivity;
    this.userarraylist=userarraylist;
    }

    @NonNull
    @Override
    public Useradapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
    View v;
    v = LayoutInflater.from(mainActivity).inflate(R.layout.user_template,parent,false) ;
    return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Useradapter.viewholder holder, int position) {
    Users users=userarraylist.get(position);
    holder.username.setText(users.name);
    holder.userstatus.setText(users.status);
    Picasso.get().load(users.image).into(holder.userimage);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mainActivity, chatframe.class);
            intent.putExtra("namek",users.getName());
            intent.putExtra("recieverimg",users.getImage());
            intent.putExtra("uid",users.getId());
            mainActivity.startActivity(intent);
        }
    });


    }

    @Override
    public int getItemCount() {
        return userarraylist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimage;
        TextView username;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimage=itemView.findViewById(R.id.uploadimage);
            username=itemView.findViewById(R.id.uname);
            userstatus=itemView.findViewById(R.id.status);

        }
    }
}
