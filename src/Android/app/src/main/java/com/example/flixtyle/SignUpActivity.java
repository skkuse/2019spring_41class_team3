package com.example.flixtyle;

import android.content.Intent;
import android.graphics.Region;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordCheck;
    private EditText mName;

    private RadioGroup mRadioGender;

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
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mEmail=(EditText) findViewById(R.id.edit_email);
        mPassword=(EditText)findViewById(R.id.edit_password);
        mPasswordCheck=(EditText)findViewById(R.id.edit_check);
        mName=(EditText)findViewById(R.id.edit_name);

        mSignup=(Button)findViewById(R.id.btn_signup);

        mRadioGender=(RadioGroup)findViewById(R.id.radioGender);


        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectID=mRadioGender.getCheckedRadioButtonId();
                final RadioButton radioButton= (RadioButton) findViewById(selectID);

                //must choose gender
                if (radioButton.getText()==null){
                    return;
                }






                final String email= mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                final String passwordCheck=mPasswordCheck.getText().toString();
                final String name=mName.getText().toString();

                //check if equal
                if(password.equals(passwordCheck)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this,
                                    new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //check if creation wasn't successful
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "failed to sign up",
                                        Toast.LENGTH_SHORT).show();

                            }
                            //if successful
                            else{
                                String userId=mAuth.getCurrentUser().getUid();
                                //reference
                                DatabaseReference currentUserDb= FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(radioButton.getText().toString()).child(userId).child("name");

                                currentUserDb.setValue(userId);
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(SignUpActivity.this, "passwords do not match",
                            Toast.LENGTH_SHORT).show();
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
