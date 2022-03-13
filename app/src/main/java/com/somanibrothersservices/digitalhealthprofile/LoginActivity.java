package com.somanibrothersservices.digitalhealthprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    public static String ACTOR , PATIENT_ID , ACCESS;
    public static FirebaseFirestore FIREBASE_FIRESTORE;
    public Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

                FIREBASE_FIRESTORE = FirebaseFirestore.getInstance();
        ACTOR = "doctor";

        dropdown = findViewById(R.id.spinner_actor);
        String[] items = new String[]{"Patient", "Doctor", "Hospital Management" , "Pharmacist" , "Medical Student" , "Select Actor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(5);

    }

    public void login(View view) {
        final EditText loginId = findViewById(R.id.edit_text_id_login), loginPassword = findViewById(R.id.edit_text_password_login);
        FIREBASE_FIRESTORE.collection("LOGINS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean finisedLoop = true;
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (!dropdown.getSelectedItem().toString().equals("Select Actor")) {
                            if (loginId.getText().toString().equals(documentSnapshot.getString("login_id")) &&
                            loginPassword.getText().toString().equals(documentSnapshot.getString("login_password")) &&
                            dropdown.getSelectedItem().toString().toLowerCase().equals(documentSnapshot.getString("actor"))) {
                                ACTOR = documentSnapshot.getString("actor").toLowerCase();
                                switch (ACTOR) {
                                    case "patient":
                                        ACCESS = "1";
                                        PATIENT_ID = loginId.getText().toString();
                                        startActivity(new Intent(LoginActivity.this, PatientDetailsActivity.class));
                                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                        finish();
                                        break;
                                    case "doctor":
                                        ACCESS = "2";
                                        break;
                                    case "hospital management":
                                        ACCESS = "3";
                                        break;
                                    case "pharmacist":
                                        ACCESS = "4";
                                        startActivity(new Intent(LoginActivity.this, PharmacistActivity.class));
                                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                        finish();
                                        break;
                                    case "medical student":
                                        ACCESS = "5";
                                        break;
                                }
                                if (ACTOR.equals("doctor") || ACTOR.equals("hospital management") || ACTOR.equals("medical student")) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                finisedLoop = false;
                                break;
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Actor needs to be selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (finisedLoop)
                        Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
