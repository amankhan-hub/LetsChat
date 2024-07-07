package com.example.letschat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Userinfo extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView recyclerView;
    userinfoadapter adapter;
    BottomNavigationView btnv;
    FirebaseDatabase database;
    ArrayList<Users> userarraylist;
    ImageView logoutimage;
    Button btnyes,btnno;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userinfo);
        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        init();
        DatabaseReference reference=database.getReference().child("user");
        userarraylist=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new userinfoadapter(Userinfo.this,userarraylist);
        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot datasnapshot: snapshot.getChildren())
                {
                    Users user=datasnapshot.getValue(Users.class);
                    userarraylist.add(user);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        progressdialog=new ProgressDialog(this);
        progressdialog.setMessage("Logging out...");
        progressdialog.setCancelable(false);

        logoutimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(Userinfo.this,R.style.dialog);
                dialog.setContentView(R.layout.dialog_layout);
                btnyes=dialog.findViewById(R.id.yeslogout);
                btnno=dialog.findViewById(R.id.nologout);

                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        progressdialog.show();
                        Intent intent=new Intent(Userinfo.this,Login_Screen.class);
                        startActivity(intent);

                    }
                });

                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }
    private void init()
    {
        recyclerView=findViewById(R.id.recycle);
        logoutimage=findViewById(R.id.logoutmain);
        btnv=findViewById(R.id.bottomnavigationview);
        if (btnv != null) {
            btnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.Setting) {
                        Intent intent = new Intent(Userinfo.this, Settingpage.class);
                        startActivity(intent);
                        return true;
                    } else if (item.getItemId() == R.id.status) {
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent2, 100);
                        return true;
                    } else if(item.getItemId()==R.id.Chats) {
                        Intent intent3 = new Intent(Userinfo.this, Userinfo.class);
                        startActivity(intent3);
                        return true;
                    }else if(item.getItemId()==R.id.Message) {
                        Intent intent4 = new Intent(Userinfo.this, MainActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                    }else {
                        return false;
                    }
                }
            });
        } else {
            Log.e("MainActivity", "btnv is null");
            // Handle this situation gracefully, perhaps show a message or fallback behavior
        }
    }
}