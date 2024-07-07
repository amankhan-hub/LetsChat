package com.example.letschat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class Settingpage extends AppCompatActivity {
    ImageView updateimage;
    EditText newname,newstatus;
    Button submitnewbtn;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
     String email,password,phone;

     Uri setImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settingpage);

        init();
        //fetching data info of the user from database
        DatabaseReference reference= firebaseDatabase.getReference("user").child(auth.getUid());
        //fetching the user picture
        StorageReference storagereference=storage.getReference("Upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            email=snapshot.child("email").getValue().toString();
            password=snapshot.child("password").getValue().toString();
            phone=snapshot.child("phone").getValue().toString();

            String name=snapshot.child("name").getValue().toString();
            String status=snapshot.child("status").getValue().toString();
            String image=snapshot.child("image").getValue().toString();
            newname.setText(name);
            newstatus.setText(status);
            Picasso.get().load(image).into(updateimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      updateimage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent();
              intent.setType(("image/*"));
              intent.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);

          }
      });

      //Button
        submitnewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the new name,status or image
                String nameu=newname.getText().toString();
                String statusu=newstatus.getText().toString();
                //the user selected the image
                if(setImageUri!=null)
                {
                    storagereference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storagereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalimageUri=uri.toString();
                                    Users users=new Users(auth.getUid(),finalimageUri,nameu,phone,email,password,statusu);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(Settingpage.this, "Data Update Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(Settingpage.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(Settingpage.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            });
                        }
                    });
                }else{
                    storagereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri=uri.toString();
                            Users users=new Users(auth.getUid(),finalImageUri,nameu,phone,email,password,statusu);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Settingpage.this, "Data Update Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Settingpage.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(Settingpage.this, "Something went Wrong", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            //user selected image
            if(data!=null)
            {
                setImageUri=data.getData();  //store image that the user has selected
                updateimage.setImageURI(setImageUri);  //the selected image is set on update image
            }
        }
    }

    private void init()
    {
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        updateimage=findViewById(R.id.updateimage);
        newname=findViewById(R.id.editText);
        newstatus=findViewById(R.id.editTextstatus);
        submitnewbtn=findViewById(R.id.button);


    }
}