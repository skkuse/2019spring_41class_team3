package com.example.flixtyle;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AfterLogin extends AppCompatActivity {
    private DatabaseReference mPostReference;
    private DatabaseReference mReference;

    String user_name, user_gender, user_birth, user_country,user_city, UID, user_email = "";
    private EditText mName;
    private EditText mBirth;
    private EditText mCountry;
    private EditText mCity;

    private RadioGroup mRadioGender;
    private Button mSave;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        mName=(EditText)findViewById(R.id.edit_name);
        mBirth=(EditText)findViewById(R.id.edit_birth);
        mCountry=(EditText)findViewById(R.id.edit_country);
        mCity=(EditText)findViewById(R.id.edit_city);
        mRadioGender=(RadioGroup)findViewById(R.id.radioGender);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();

        mSave=(Button)findViewById(R.id.btn_save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectID = mRadioGender.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectID);

                //must choose gender
                if (radioButton.getText() == null) {
                    return;
                }

                user_name = mName.getText().toString();
                user_gender = radioButton.getText().toString();
                user_birth = mBirth.getText().toString();
                user_city = mCity.getText().toString();
                user_country = mCountry.getText().toString();
                UID=mAuth.getCurrentUser().getUid();
                user_email = mAuth.getCurrentUser().getEmail();

                if ((user_name.length() * user_gender.length() * user_birth.length() * user_city.length() * user_country.length()) == 0) {
                    Toast.makeText(AfterLogin.this, "Data is missing", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabase(true);
                    Intent intent = new Intent(AfterLogin.this, MainActivity.class);
                    startActivity(intent);

                }
            }

        });


    }

    public void postFirebaseDatabase(boolean add){

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            long long_user_birth = Long.parseLong(user_birth);
            Userfirebase post = new Userfirebase(UID, user_email, user_name, long_user_birth, user_gender, user_country, user_city);
            postValues = post.toMap();
        }
        childUpdates.put("/Users/" + UID, postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }
    public void clearET () {
        mName.setText("");
        mBirth.setText("");
        mCity.setText("");
        mCountry.setText("");

    }
}
