package com.example.bloodprojectapplication.ui.authentication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bloodprojectapplication.R;
import com.example.bloodprojectapplication.domain.SharePreference;
import com.example.bloodprojectapplication.model.User;
import com.example.bloodprojectapplication.ui.MainActivity;
import com.example.bloodprojectapplication.ui.admin.AdminMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private TextView backButton;
    private TextInputEditText loginEmailAddress, loginPassword;
    private TextView forgotPassword;
    private Button loginButton;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                getUserDetails();
            }
        };

        loginEmailAddress = findViewById(R.id.loginEmailAddress);
        loginPassword = findViewById(R.id.loginPassword);
        forgotPassword = findViewById(R.id.forgotPassword);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.backButton);

        loader = new ProgressDialog(this);


        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SelectRegistrationActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            final String email = loginEmailAddress.getText().toString().trim();
            final String password = loginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                loginEmailAddress.setError("Email is required");
            }

            if (TextUtils.isEmpty(password)) {
                loginPassword.setError("Password is required");
            } else {
                loader.setMessage("log in progress");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login is successful", Toast.LENGTH_SHORT).show();
                            getUserDetails();

                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                        loader.dismiss();
                    }
                });
            }
        });
        forgotPassword.setOnClickListener(view -> showRecoverPasswordDialog());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    private void getUserDetails() {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                String name = snapshot.child("name").getValue().toString();
                SharePreference.getINSTANCE(getApplicationContext()).setName(name);


                String email = snapshot.child("email").getValue().toString();
                SharePreference.getINSTANCE(getApplicationContext()).setEmail(email);


                String bloodGroup = snapshot.child("bloodgroup").getValue().toString();
                SharePreference.getINSTANCE(getApplicationContext()).setBloodgroup(bloodGroup);


                String type = snapshot.child("type").getValue().toString();
                SharePreference.getINSTANCE(getApplicationContext()).setType(type);

                String phoneNumber = snapshot.child("phonenumber").getValue().toString();
                SharePreference.getINSTANCE(getApplicationContext()).setPhonenumber(phoneNumber);
                if (type.equals("donor")) {
                    updateLocation(name, phoneNumber, bloodGroup);
                }
                Intent intent;
                if (type.equals("Admin")) {
                    intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

        });
    }


    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailet.getText().toString().trim();
                beginRecovery(email);
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        ProgressDialog loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if (task.isSuccessful()) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this, "Done sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateLocation(String name, String phoneNumber, String bloodGroup) {
        if (ActivityCompat.checkSelfPermission(
                LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                Double lat = locationGPS.getLatitude();
                Double lng = locationGPS.getLongitude();
                User user = new User();
                user.setUsername(name);
                user.setContact(phoneNumber);
                user.setBloodGroup(bloodGroup);
                user.setLocation(lat + " " + lng);
                FirebaseDatabase.getInstance().getReference().child("location").child(
                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                ).setValue(user);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}