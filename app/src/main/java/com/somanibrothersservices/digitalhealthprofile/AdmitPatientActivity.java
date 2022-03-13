package com.somanibrothersservices.digitalhealthprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.FIREBASE_FIRESTORE;

public class AdmitPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admit_patient);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Admit Patient") ;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tab_layout_admit);
        final Group registeredGroup = findViewById(R.id.group_registered);
        final Group newRegistrationGroup = findViewById(R.id.group_new_registration);
        registeredGroup.setVisibility(View.VISIBLE);
        registered();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    registeredGroup.setVisibility(View.VISIBLE);
                    newRegistrationGroup.setVisibility(View.INVISIBLE);
                    registered();
                } else if (tab.getPosition() == 1) {
                    registeredGroup.setVisibility(View.INVISIBLE);
                    newRegistrationGroup.setVisibility(View.VISIBLE);
                    newRegistration();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void registered() {
        final AutoCompleteTextView patientIdAuto = findViewById(R.id.autoPatientId) , doctorNameAuto = findViewById(R.id.autoDoctorName);
        Button admitPatientButton = findViewById(R.id.button_admit_patient);
        FIREBASE_FIRESTORE.collection("PATIENTS").document("All Patients List").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    list = (List<String>) task.getResult().get("id_list");
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        arrayList.add(list.get(i));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(AdmitPatientActivity.this, android.R.layout.simple_dropdown_item_1line, arrayList);
                    patientIdAuto.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        FIREBASE_FIRESTORE.collection("HOSPITAL").document("M G M HOSPITAL").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    list = (List<String>) task.getResult().get("All Doctor List");
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        arrayList.add(list.get(i));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(AdmitPatientActivity.this, android.R.layout.simple_dropdown_item_1line, arrayList);
                    doctorNameAuto.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        admitPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(patientIdAuto.getText()) && !TextUtils.isEmpty(doctorNameAuto.getText())) {
                    FIREBASE_FIRESTORE.collection("HOSPITAL/M G M HOSPITAL/DR ROHIT").document("Patients")
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> patientIds = (List<String>) task.getResult().get("patients");
                                if (!patientIds.contains(patientIdAuto.getText().toString())) {
                                    patientIds.add(patientIdAuto.getText().toString());
                                    FIREBASE_FIRESTORE.document("HOSPITAL/M G M HOSPITAL/DR ROHIT/Patients")
                                            .update("patients" , patientIds).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FIREBASE_FIRESTORE.collection("PATIENTS").document(patientIdAuto.getText().toString())
                                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            String blockChain = task.getResult().getString("blockchain");
                                                            String[] blocks = blockChain.split(">");
                                                            String[] block = blocks[blocks.length - 1].split(";");
                                                            String data = "Patient Admitted at M G M Hospital under DR ROHIT";
                                                            String timestamp = getDateTime();
                                                            try {
                                                                blockChain += ">" + timestamp + ";" + block[1] + ";" + Block.getHash(timestamp , block[1] , data) + ";" + data + ";" + "1,2,3,5";
                                                                FIREBASE_FIRESTORE.collection("PATIENTS").document(patientIdAuto.getText().toString())
                                                                        .update("blockchain" , blockChain).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(AdmitPatientActivity.this, "Patient already under same doctor's supervision", Toast.LENGTH_SHORT).show();
                                }
                                } else {
                                Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AdmitPatientActivity.this, "Patient Id and Doctor's Name required !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void newRegistration() {
        final EditText name = findViewById(R.id.editPatientNameNR),
                id = findViewById(R.id.editPatientIdNR3) ,
                sex = findViewById(R.id.editPatientSexNR4) ,
                age = findViewById(R.id.editPatientAgeNR),
                mobile = findViewById(R.id.editPatientMobileNoNR),
                address = findViewById(R.id.editPatientAddressNR);
        final AutoCompleteTextView doctorName = findViewById(R.id.autoDoctorNameNR);
        Button confirmRegistrationButton = findViewById(R.id.button_confirm_registration);
        FIREBASE_FIRESTORE.collection("HOSPITAL").document("M G M HOSPITAL").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    list = (List<String>) task.getResult().get("All Doctor List");
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        arrayList.add(list.get(i));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(AdmitPatientActivity.this, android.R.layout.simple_dropdown_item_1line, arrayList);
                    doctorName.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        confirmRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameT = name.getText().toString() ,
                        idT = id.getText().toString() ,
                        sexT = sex.getText().toString() ,
                        ageT = age.getText().toString() ,
                        mobilT = mobile.getText().toString() ,
                        addressT = address.getText().toString();
                if (idT.length() == 12) {
                    if (nameT.length() != 0 && sexT.length() != 0 && ageT.length() != 0 && mobilT.length() != 0 && addressT.length() != 0) {
                        FIREBASE_FIRESTORE.collection("PATIENTS").document("All Patients List").get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            final List<String> patientIds = (List<String>) task.getResult().get("id_list");
                                            if (!patientIds.contains(idT)) {
                                                patientIds.add(idT);
                                                FIREBASE_FIRESTORE.collection("PATIENTS").document("All Patients List")
                                                        .update("id_list" , patientIds).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            String timestamp = getDateTime();
                                                            String blockchain = new String();
                                                            try {
                                                                String lastHash = Block.getHash(String.valueOf(timestamp) ,"not valid" , nameT +
                                                                        " registered by M G M Hospital");
                                                                blockchain = timestamp + ";not valid;" + lastHash + ";" +
                                                                        nameT + " registered by M G M Hospital"+ ";" + "1,2,3,5" + ">" + timestamp + ";" + lastHash + ";" +
                                                                        Block.getHash(String.valueOf(timestamp) , lastHash , "Patient assigned under treatment of " +
                                                                                "DR ROHIT") + ";" + "Patient under treatment of DR ROHIT" + ";" + "1,2,3,5";
                                                            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                                                                e.printStackTrace();
                                                            }
                                                            Map<String , Object> patientMap = new HashMap<>();
                                                            patientMap.put("name" , nameT);
                                                            patientMap.put("id" , idT);
                                                            patientMap.put("age" , ageT);
                                                            patientMap.put("sex" , sexT);
                                                            patientMap.put("address" , addressT);
                                                            patientMap.put("mobile" , mobilT);
                                                            patientMap.put("blockchain" , blockchain);
                                                            FIREBASE_FIRESTORE.collection("PATIENTS").document(idT).set(patientMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        FIREBASE_FIRESTORE.collection("HOSPITAL/M G M HOSPITAL/DR ROHIT").document("Patients")
                                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    List<String> patientIds = (List<String>) task.getResult().get("patients");
                                                                                        patientIds.add(idT);
                                                                                        FIREBASE_FIRESTORE.document("HOSPITAL/M G M HOSPITAL/DR ROHIT/Patients")
                                                                                                .update("patients" , patientIds).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Map<String , Object> loginMap = new HashMap<>();
                                                                                                    loginMap.put("login_id" , idT);
                                                                                                    loginMap.put("login_password" , idT);
                                                                                                    loginMap.put("actor" , "patient");
                                                                                                    FIREBASE_FIRESTORE.collection("LOGINS").document(idT).set(loginMap)
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        finish();
                                                                                                                    } else {
                                                                                                                        Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                } else {
                                                                                                    Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                } else {
                                                                                    Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    } else {
                                                                        Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(AdmitPatientActivity.this, "Id already registered", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(AdmitPatientActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(AdmitPatientActivity.this, "All fields are Compulsory", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdmitPatientActivity.this, "Id should of 12 Digits", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        Long time = System.currentTimeMillis();
        calendar.setTimeInMillis(time);

        //dd=day, MM=month, yyyy=year, hh=hour, mm=minute, ss=second.

        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss",calendar).toString();
        return date;
    }
}
