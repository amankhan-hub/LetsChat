package com.example.letschat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup_Screen extends AppCompatActivity {
    CircleImageView image;
    Button signuploginbtn,signupbtn;
    EditText Name,Phone,Emails,Passwords;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String EmailPattern= "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@"
            + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    ProgressDialog progressDialog;
    ProgressDialog progressDialoglog;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_screen);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");
        progressDialog.setCancelable(false);

        progressDialoglog=new ProgressDialog(this);
        progressDialoglog.setMessage("Redirecting to Login Page");
        progressDialoglog.setCancelable(false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        init();

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=Name.getText().toString();
                String phone=Phone.getText().toString();
                String email=Emails.getText().toString();
                String password=Passwords.getText().toString();
                String status="Hey there i am Using this Application";


                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)||
                TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
                {
                    progressDialog.dismiss();
                    progressDialoglog.dismiss();
                    Toast.makeText(Signup_Screen.this, "Some Fields need to be filled", Toast.LENGTH_SHORT).show();
                }
                else if(!email.matches(EmailPattern))
                {
                    progressDialog.dismiss();
                    progressDialoglog.dismiss();

                    Emails.setError("Email Pattern not correct");
                }
                else if(password.length()<=6)
                {
                    progressDialog.dismiss();
                    progressDialoglog.dismiss();

                    Passwords.setError("Password Should be greater then six");
                }
                else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String id = task.getResult().getUser().getUid();
                            DatabaseReference reference = database.getReference().child("user").child(id);
                           StorageReference storageReference=storage.getReference().child("Upload").child(id);
                           if(imageURI!=null)
                           {
                             storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                 @Override
                                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                   if(task.isSuccessful())
                                   {
                                       storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                           @Override
                                           public void onSuccess(Uri uri) {
                                             imageuri=uri.toString();
                                             Users users=new Users(id,imageuri,name,phone,email,password,status);
                                             reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if(task.isSuccessful())
                                                     {
                                                         progressDialog.show();
                                                         progressDialoglog.dismiss();

                                                         Intent intent=new Intent(Signup_Screen.this,MainActivity.class);
                                                         startActivity(intent);
                                                         finish();
                                                     }
                                                     else
                                                     {
                                                         Toast.makeText(Signup_Screen.this, "Error while signing up", Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             });
                                           }
                                       });
                                   }else
                                   {
                                       Toast.makeText(Signup_Screen.this,task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                   }
                                 }
                             });
                           }
                           else
                           {
                               String status="Hey there i am Using this Application";
                               imageuri="https://firebasestorage.googleapis.com/v0/b/letschat-1e86f.appspot.com/o/dumy.webp?alt=media&token=1e336ed3-1333-476e-95d5-0c9665855615";
                               Users users=new Users(id,imageuri,name,phone,email,password,status);
                               reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful())
                                       {
                                           progressDialog.show();
                                           progressDialoglog.dismiss();

                                           Intent intent=new Intent(Signup_Screen.this,MainActivity.class);
                                           startActivity(intent);
                                           finish();
                                       }
                                       else
                                       {
                                           Toast.makeText(Signup_Screen.this, "Error while signing up", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                           }
                        }
                    });
                }
            }
        });



        signuploginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialoglog.show();
                Intent intent=new Intent(Signup_Screen.this, Login_Screen.class);
                startActivity(intent);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),10);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
                imageURI=data.getData();
                image.setImageURI(imageURI);
            }
        }

    }



    private void init()
    {
        image=findViewById(R.id.uploadimage);
        signuploginbtn=findViewById(R.id.btnlogins);
        signupbtn=findViewById((R.id.signups));
        Name=findViewById(R.id.name);
        Phone=findViewById(R.id.phone);
        Emails=findViewById(R.id.users);
        Passwords=findViewById(R.id.passs);

    }
}