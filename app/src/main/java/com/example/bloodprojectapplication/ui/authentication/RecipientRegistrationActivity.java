package com.example.bloodprojectapplication.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodprojectapplication.R;
import com.example.bloodprojectapplication.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RecipientRegistrationActivity extends AppCompatActivity {

    private TextView  backButton;

    private TextInputEditText registerFullNames,registerIdNumber,registerPhoneNumber, registerEmailAddress,
            registerPassword;

    private Spinner bloodGroupSpinner;

    private Button registerButton;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;

    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_registration);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipientRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        registerFullNames = findViewById(R.id.registerFullNames);
        registerIdNumber = findViewById(R.id.registerIdNumber);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerEmailAddress = findViewById(R.id.registerEmailAddress);
        registerPassword = findViewById(R.id.registerPassword);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        registerButton = findViewById(R.id.registerButton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = registerEmailAddress.getText().toString().trim();
                final String password =registerPassword.getText().toString().trim();
                final String fullName =registerFullNames.getText().toString().trim();
                final String idNumber = registerIdNumber.getText().toString().trim();
                final String phoneNumber = registerPhoneNumber.getText().toString();
                final String bloodGroup= bloodGroupSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)){
                    registerEmailAddress.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    registerPassword.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(fullName)){
                    registerFullNames.setError("Full name is required");
                    return;
                }
                if (TextUtils.isEmpty(idNumber)){
                    registerIdNumber.setError("Id Number is required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    registerPhoneNumber.setError("Phone Number is required");
                    return;
                }
                if (bloodGroup.equals("Select your blood group")){
                    Toast.makeText(RecipientRegistrationActivity.this, "Select blood group", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    loader.setMessage("Registering You...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RecipientRegistrationActivity.this, "Error" +error, Toast.LENGTH_SHORT).show();
                            }

                            else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", fullName);
                                userInfo.put("email", email);
                                userInfo.put("idnumber", idNumber);
                                userInfo.put("phonenumber", phoneNumber);
                                userInfo.put("bloodgroup", bloodGroup);
                                userInfo.put("type", "recipient");
                                userInfo.put("search", "recipient"+bloodGroup);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RecipientRegistrationActivity.this, "Data is set successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(RecipientRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                        finish();
                                        //loader.dismiss();

                                    }
                                });

                                Intent intent = new Intent(RecipientRegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();







                            }

                        }
                    });
                }







            }
        });


    }
}