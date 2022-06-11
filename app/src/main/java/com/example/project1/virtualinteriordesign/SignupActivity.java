package com.example.project1.virtualinteriordesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    Button loginButton,registerButton;
    TextInputLayout userName,email,password;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();

        loginButton=findViewById(R.id.signup_login_bt);
        registerButton=findViewById(R.id.signup_register_bt);
        email=findViewById(R.id.signup_email);
        password=findViewById(R.id.signup_password);
        userName=findViewById(R.id.signup_fullname);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= userName.getEditText().getText().toString();
                String pass= password.getEditText().getText().toString();
                String mail=email.getEditText().getText().toString();
                if(!name.isEmpty()){
                    userName.setError(null);
                    userName.setErrorEnabled(false);
                    if(!mail.isEmpty()){
                        email.setError(null);
                        email.setErrorEnabled(false);
                        if(!pass.isEmpty()){
                            password.setError(null);
                            password.setErrorEnabled(false);


                            //Log.d("+++++++++++++++++++++++++++++++++++++++++++   andar","  +++++++++++++++++++++++++++++++++++++++++   andar");
                            firebaseDatabase=FirebaseDatabase.getInstance();
                            reference=firebaseDatabase.getReference("user");
//                            String name_s= userName.getEditText().getText().toString();
//                            String pass_s= password.getEditText().getText().toString();
//                            String mail_s=email.getEditText().getText().toString();
                            auth.createUserWithEmailAndPassword(mail,
                                    pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        User user=new User(name,mail,pass);
                                        String id=task.getResult().getUser().getUid();
                                        firebaseDatabase.getReference().child("user").child(id).setValue(user);
                                        Toast.makeText(SignupActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else{
                            password.setError("please enter password");
                        }
                    }
                    else{
                        email.setError("please enter Email");
                    }
                }
                else{
                    userName.setError("please enter username");
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

