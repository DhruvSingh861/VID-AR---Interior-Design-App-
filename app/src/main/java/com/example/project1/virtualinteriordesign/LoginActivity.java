package com.example.project1.virtualinteriordesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button loginButton, signupButton;
    TextInputLayout email,password;
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;





    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        signupButton=findViewById(R.id.login_signup_bt);
        loginButton=findViewById(R.id.login_bt);
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail= email.getEditText().getText().toString();
                String pass= password.getEditText().getText().toString();
                if(!mail.isEmpty()){
                    email.setError(null);
                    email.setErrorEnabled(false);
                    if(!pass.isEmpty()){
                        password.setError(null);
                        password.setErrorEnabled(false);
                                auth.signInWithEmailAndPassword(mail,pass)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                    }
                    else{
                        password.setError("please enter password");
                    }
                }
                else{
                    email.setError("please enter username");
                }
            }
        });


        //Log.d("inside login=======>>>>>>>>>>>>>>>>  ",auth.getCurrentUser().getDisplayName());

        if(auth.getCurrentUser()!=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });



    }
}