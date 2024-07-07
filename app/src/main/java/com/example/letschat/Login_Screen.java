package com.example.letschat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Screen extends AppCompatActivity {
    EditText userlog,passlog;
    Button loginbtn,signupbtn;
    FirebaseAuth auth;
    String Pattern= "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@"
            + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    android.app.ProgressDialog progressdialog;
    ProgressDialog progressDialoglog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        progressdialog=new ProgressDialog(this);
        progressdialog.setMessage("Checking Credentials...");
        progressdialog.setCancelable(false);

        progressDialoglog=new ProgressDialog(this);
        progressDialoglog.setMessage("Redirecting to Signup Page");
        progressDialoglog.setCancelable(false);

        auth= FirebaseAuth.getInstance();
        userlog=findViewById(R.id.userl);
        passlog=findViewById(R.id.passl);
        loginbtn=findViewById(R.id.btnl);
        signupbtn=findViewById(R.id.loginsignupbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=userlog.getText().toString().trim();
                String password=passlog.getText().toString().trim();
                if((TextUtils.isEmpty(username)||(TextUtils.isEmpty(password))))
                {

                    Toast.makeText(Login_Screen.this, "Credentials are missing", Toast.LENGTH_SHORT).show();
                }
                else if (!username.matches(Pattern))
                {


                    userlog.setError("Provide Proper Email Pattern");
                }
                else if (password.length()<=6)
                {


                     passlog.setError("Password length should be greater then six");
                }
                else
                {
                    auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {


                                try {
                                    progressdialog.show();
                                    Intent intent =new Intent(Login_Screen.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }catch(Exception e)
                                {
                                    Toast.makeText(Login_Screen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(Login_Screen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialoglog.show();
            Intent intent=new Intent(Login_Screen.this,Signup_Screen.class);
            startActivity(intent);
            finish();
            }
        });
    }
}