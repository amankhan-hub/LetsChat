package com.example.letschat;

import static com.example.letschat.chatframe.recieverimg;
import static com.example.letschat.chatframe.senderimg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageadapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<messagemodelclass>  messageadapterArraylist;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public messageadapter(Context context, ArrayList<messagemodelclass> messageadapterArraylist) {
        this.context = context;
        this.messageadapterArraylist = messageadapterArraylist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType==ITEM_SEND)
       {
           View view= LayoutInflater.from(context)
                   .inflate(R.layout.sender_layout,parent,false);
           return new senderViewHolder(view);
       }
       else
       {
           View view= LayoutInflater.from(context)
                   .inflate(R.layout.reciever_layout,parent,false);
           return new recieverViewHolder(view);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        messagemodelclass message = messageadapterArraylist.get(position);
        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.tvmsg.setText(message.getMessage());
            if (senderimg != null) {
                Picasso.get().load(senderimg).into(viewHolder.circleimageview);
            }
        } else {
            recieverViewHolder viewHolder = (recieverViewHolder) holder;
            viewHolder.tvmsg.setText(message.getMessage());
            if (recieverimg != null) {
                Picasso.get().load(recieverimg).into(viewHolder.circleimageview);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageadapterArraylist.size();
    }

    @Override
    public int getItemViewType(int position) {
        messagemodelclass messages=messageadapterArraylist.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid()))
        {
            return ITEM_SEND;
        }
        else
        {
         return ITEM_RECIEVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleimageview;
        TextView tvmsg;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleimageview=itemView.findViewById(R.id.profilerggg);
            tvmsg=itemView.findViewById(R.id.msgsendertyp);

        }
    }
    class recieverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleimageview;
        TextView tvmsg;
        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleimageview=itemView.findViewById(R.id.pro);
            tvmsg=itemView.findViewById(R.id.recivertextset);
        }
    }
}
