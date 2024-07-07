package com.example.letschat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatframe extends AppCompatActivity {


    String Recieverimage,Recieverid,Recievername,Senderid;
    CircleImageView circleimage;
    TextView rname;
    CardView sbtn;
    EditText etmessage;
    FirebaseAuth firebaseauth;
    FirebaseDatabase firebasedatabase;
    public static String senderimg;
    public static String recieverimg;
    String senderroom,recieverroom;
    RecyclerView recyclerView;
    ArrayList<messagemodelclass> messagesArrayList;
    messageadapter msgadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatframe);


        firebaseauth = FirebaseAuth.getInstance();
        firebasedatabase = FirebaseDatabase.getInstance();
        messagesArrayList=new ArrayList<>();

        recyclerView=findViewById(R.id.recyclercf);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        msgadapter=new messageadapter(chatframe.this,messagesArrayList);
        recyclerView.setAdapter(msgadapter);

        Recievername=getIntent().getStringExtra("namek");
        Recieverid=getIntent().getStringExtra("uid");
        Recieverimage=getIntent().getStringExtra("recieverimg");




        sbtn=findViewById(R.id.sendbtn);
        etmessage=findViewById(R.id.umessage);

        circleimage=findViewById(R.id.uploadimage);
        rname=findViewById(R.id.reciever);


        Senderid=firebaseauth.getUid();

        senderroom=Senderid+Recieverid;
        recieverroom=Recieverid+Senderid;

        Picasso.get().load(Recieverimage).into(circleimage);
        rname.setText(" "+Recievername);

        DatabaseReference reference=firebasedatabase.getReference().child("user").child(firebaseauth.getUid());
        DatabaseReference referenceforchat=firebasedatabase.getReference().child("chats").child(senderroom).child("messages");

        referenceforchat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren())
                {
                    messagemodelclass messagemc=dataSnapshot.getValue(messagemodelclass.class);
                    messagesArrayList.add(messagemc);
                }
                msgadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             senderimg=snapshot.child("image").getValue().toString();
             recieverimg=Recieverimage;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=etmessage.getText().toString();
                if(message.isEmpty())
                {
                    Toast.makeText(chatframe.this, "Enter the message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                etmessage.setText("");
                Date date=new Date();
                messagemodelclass messagemc= new messagemodelclass(message,Senderid,date.getTime());
                firebasedatabase.getReference().child("chats").child(senderroom).child("messages")
                        .push().setValue(messagemc).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                firebasedatabase.getReference().child("chats").child(recieverroom).child("messages").push().setValue(messagemc).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        });
            }
        });

    }

}
