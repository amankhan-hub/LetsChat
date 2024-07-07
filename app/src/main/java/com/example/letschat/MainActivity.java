package com.example.letschat;

import static com.example.letschat.R.id.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView recyclerView;
    Useradapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> userarraylist;
    ImageView logoutimage;
    Button btnyes,btnno;
    ProgressDialog progressdialog;
    BottomNavigationView btnv;
    AdView adview;
    InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        FirebaseApp.initializeApp(this);

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        init();

        //Banner ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        AdRequest adrequest=new AdRequest.Builder().build();
        adview.loadAd(adrequest);




        String adUnitId = getString(R.string.appinstantid);

        // Load the interstitial ad
        InterstitialAd.load(this, adUnitId, adrequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                Log.d("AdMob", "Interstitial ad loaded successfully.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("AdMob", "Interstitial ad failed to load: " + loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

        // Example of showing the ad after a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.e("AdMob", "Interstitial ad was not ready.");
                }
            }
        }, 1000); // Adjust delay time as needed





        DatabaseReference reference=database.getReference().child("user");
        userarraylist=new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Useradapter(MainActivity.this,userarraylist);
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
                Dialog dialog=new Dialog(MainActivity.this,R.style.dialog);
                dialog.setContentView(R.layout.dialog_layout);
                btnyes=dialog.findViewById(R.id.yeslogout);
                btnno=dialog.findViewById(R.id.nologout);

                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       FirebaseAuth.getInstance().signOut();
                       progressdialog.show();
                       Intent intent=new Intent(MainActivity.this,Login_Screen.class);
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
        adview=findViewById(R.id.bannerad);

        if (btnv != null) {
            btnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.Setting) {
                        Intent intent = new Intent(MainActivity.this, Settingpage.class);
                        startActivity(intent);
                        return true;
                    } else if (item.getItemId() == R.id.status) {
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent2, 100);
                        return true;
                    } else if (item.getItemId()==R.id.Chats){
                        Intent intent3=new Intent(MainActivity.this,Userinfo.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    }else if(item.getItemId()==R.id.Message) {
                        Intent intent4 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent4);
                        finish();
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            });
        } else {
            Log.e("MainActivity", "btnv is null");

        }
    }


   @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cuser = auth.getCurrentUser();
        if (cuser != null) {
            // User is logged in
            // Log some message to verify
            Log.d("MainActivity", "User is logged in: " + cuser.getEmail());
        } else {
            // User is not logged in, redirect to Login_Screen
            Intent intent = new Intent(MainActivity.this, Login_Screen.class);
            startActivity(intent);
            finish();
        }


    }


}