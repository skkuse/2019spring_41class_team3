package com.example.flixtyle;

import android.content.Intent;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordCheck;

    private Button mSignup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth=FirebaseAuth.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                //User is logged in
                if(user!=null){
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    finish();
                    return;
                }
            }
        };

        mEmail=(EditText) findViewById(R.id.edit_email);
        mPassword=(EditText)findViewById(R.id.edit_password);
        mPasswordCheck=(EditText)findViewById(R.id.edit_check);

        mSignup=(Button)findViewById(R.id.btn_signup);


        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email= mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                final String passwordCheck=mPasswordCheck.getText().toString();

                if(password==passwordCheck) {
                    mAuth.createUserWithEmailAndPassword(email,
                            password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //check if creation wasn't successful
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "failed to sign up", Toast.LENGTH_SHORT).show();;

                            }
                        }
                    });

                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    //remove when closed

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
