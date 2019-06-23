package com.example.flixtyle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private SignInButton mLogin2;
    private Button mLogin;
    private Button mSignUp;
    private EditText mEmail;
    private EditText mPassword;
    private ArrayList<String[]> list = new ArrayList<>();
    private int flag = 0;

    private static final String TAG = "GoogleActivity";
    int RC_SIGN_IN = 0;
    String user_email="",UID="";
    GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        flag = 0;
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("498634721820-spic3fjcbgsb2gdkbmpsi9evquc55pm3.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth=FirebaseAuth.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                //User is logged in
                if(user!=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mEmail=(EditText) findViewById(R.id.email_edit);
        mPassword=(EditText)findViewById(R.id.pw_edit);

        mSignUp=(Button)findViewById(R.id.signup_button);
        mLogin=(Button)findViewById(R.id.login_button) ;
        mLogin2=(SignInButton)findViewById(R.id.google_signin) ;

        getFirebaseDatabase();

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email= mEmail.getText().toString();
                final String password=mPassword.getText().toString();



                mAuth.signInWithEmailAndPassword(email,
                        password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check if creation wasn't successful
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "failed to sign in", Toast.LENGTH_SHORT).show();
                            ;

                        }
                    }

                });

            }
        });

        mLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(firebaseAuthStateListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }

    public void  updateUI(FirebaseUser account) {
        if (account != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("FirebaseAuthwithGoogle", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url

                                user_email = user.getEmail();
                                UID = user.getUid();
                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getIdToken() instead.
                                for(int j = 0 ; j < list.size(); j++){
                                    if(UID.equals(list.get(j)[1])) {

                                        flag = 1;
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("UID", UID);
                                        startActivity(intent);


                                        }
                                    }
                                if(flag ==0) {
                                    GoogleFirebase post = new GoogleFirebase(user_email, UID);

                                    Map<String, Object> childUpdates = new HashMap<>();
                                    Map<String, Object> postValues = null;

                                    postValues = post.toMap();
                                    childUpdates.put("/Users/" + UID, postValues);
                                    mPostReference.updateChildren(childUpdates);
                                    Intent intent = new Intent(LoginActivity.this, AfterLogin.class);
                                    intent.putExtra("UID", UID);
                                    startActivity(intent);

                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }



    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //만약에 데이터가 추가되거나 삭제되거나 바뀌면 실행됨.
                Log.d("onDataChange", "Data is Updated");
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { //노드 다시 읽어서 추가
                    String key = postSnapshot.getKey();
                    GoogleFirebase get = postSnapshot.getValue(GoogleFirebase.class);
                    list.add(new String[]{get.user_email, get.UID});


                    Log.d("getFirebaseDatabase", "key: " + key);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("Users").addValueEventListener(postListener); //id_list 의 서브트리부터 밑으로만 접근하겟다.
    }


}


