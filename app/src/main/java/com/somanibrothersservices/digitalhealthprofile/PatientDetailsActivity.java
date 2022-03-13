package com.somanibrothersservices.digitalhealthprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.somanibrothersservices.digitalhealthprofile.DBqueries.DOCTOR_MODEL;
import static com.somanibrothersservices.digitalhealthprofile.DBqueries.PATIENT_MODEL;
import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.ACTOR;
import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.FIREBASE_FIRESTORE;
import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.PATIENT_ID;

public class PatientDetailsActivity extends AppCompatActivity {

    public Integer position , buttonSeleted;
    public ChatDetailsRecyclerviewAdapter chatDetailsRecyclerviewAdapter;
    public TextView optionSelected;
    public Group doctorGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        position = getIntent().getIntExtra("position" , 0);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_patient_details);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PatientDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (ACTOR.equals("doctor")) {
            final LinearLayout linearLayout = findViewById(R.id.linearLayout_actor_doctor);
            linearLayout.setVisibility(View.VISIBLE);
            doctorGroup = findViewById(R.id.group_actor_doctor);
            optionSelected = findViewById(R.id.text_selected_option);
            Button analysis = findViewById(R.id.button_analysis_actor_doctor) , medicine = findViewById(R.id.button_medicines_actor_doctor) ,
            test = findViewById(R.id.button_test_actor_doctor) , note = findViewById(R.id.button_note_actor_doctor);
            analysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doctorGroup.setVisibility(View.VISIBLE);
                    buttonSeleted = 0;
                    optionSelected.setVisibility(View.VISIBLE);
                    optionSelected.setText("Analysis / Check-Up report :-\n");
                }
            });
            medicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doctorGroup.setVisibility(View.VISIBLE);
                    buttonSeleted = 1;
                    optionSelected.setVisibility(View.VISIBLE);
                    optionSelected.setText("Prescribed medicines :- (Name-Quantity-Dose)\n");
                }
            });
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doctorGroup.setVisibility(View.VISIBLE);
                    buttonSeleted = 2;
                    optionSelected.setVisibility(View.VISIBLE);
                    optionSelected.setText("Prescribed test :-\n");
                }
            });
            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doctorGroup.setVisibility(View.VISIBLE);
                    buttonSeleted = 3;
                    optionSelected.setVisibility(View.VISIBLE);
                    optionSelected.setText("Personal Note :-\n");
                }
            });
            chatDetailsRecyclerviewAdapter = new ChatDetailsRecyclerviewAdapter(DOCTOR_MODEL.patientModelList.get(position).blockchain.chain);
            recyclerView.setAdapter(chatDetailsRecyclerviewAdapter);
            getSupportActionBar().setTitle(DOCTOR_MODEL.patientModelList.get(position).name) ;
        } else if (ACTOR.equals("patient")) {
            Group patientGroup = findViewById(R.id.group_actor_patient);
            patientGroup.setVisibility(View.VISIBLE);
            DBqueries.getPatientHistory(PatientDetailsActivity.this , recyclerView , PATIENT_ID);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportActionBar().setTitle(PATIENT_MODEL.name) ;
                }
            } , 1000);
        } else if (ACTOR.equals("medical student")) {
            chatDetailsRecyclerviewAdapter = new ChatDetailsRecyclerviewAdapter(DOCTOR_MODEL.patientModelList.get(position).blockchain.chain);
            recyclerView.setAdapter(chatDetailsRecyclerviewAdapter);
        } else if (ACTOR.equals("hospital management")) {
            Group group = findViewById(R.id.group_actor_hm);
            group.setVisibility(View.VISIBLE);
            chatDetailsRecyclerviewAdapter = new ChatDetailsRecyclerviewAdapter(DOCTOR_MODEL.patientModelList.get(position).blockchain.chain);
            recyclerView.setAdapter(chatDetailsRecyclerviewAdapter);
        }

    }

    public void addTreatment(View view) {
        final EditText treatment = findViewById(R.id.edit_text_treatment_msg);;
        final String treatmentT = optionSelected.getText().toString() + treatment.getText().toString() + "\n\t\t\t by Dr Rohit";
        if (treatmentT.length() != 0 ) {
            FIREBASE_FIRESTORE.collection("PATIENTS").document(DOCTOR_MODEL.patientModelList.get(position).getId()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String timestamp = getDateTime() ,
                                        lastHash = DOCTOR_MODEL.patientModelList.get(position).blockchain.chain.get(DOCTOR_MODEL.patientModelList.get(position).blockchain.chain.size() -1).hash;
                                String hash = null;
                                try {
                                    hash = Block.getHash(timestamp , lastHash , treatmentT);
                                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                String access = "1,2";
                                switch (buttonSeleted) {
                                    case 0:
                                        access += ",5";
                                        break;
                                    case 1:
                                        access += ",3,4,5";
                                        break;
                                    case 2:
                                        access += ",3,5";
                                        break;
                                    case 3:
                                        access += ",3";
                                }
                                final Block block = new Block(timestamp , lastHash , hash , treatmentT , Arrays.asList(access.split(",")));
                                String blockchain = task.getResult().getString("blockchain");
                                blockchain += ">" + timestamp + ";" + lastHash + ";" + hash + ";" + treatmentT + ";" + access;
                                FIREBASE_FIRESTORE.collection("PATIENTS").document(DOCTOR_MODEL.patientModelList.get(position).getId())
                                        .update("blockchain" , blockchain).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DOCTOR_MODEL.patientModelList.get(position).blockchain.addBlock(block);
                                            chatDetailsRecyclerviewAdapter.notifyDataSetChanged();
                                            treatment.setText("");
                                            doctorGroup.setVisibility(View.GONE);
                                            optionSelected.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Treatment/Analysis can't be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        Long time = System.currentTimeMillis();
        calendar.setTimeInMillis(time);

        //dd=day, MM=month, yyyy=year, hh=hour, mm=minute, ss=second.

        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss",calendar).toString();
        return date;
    }

    public void logOut(View view) {
        startActivity(new Intent(PatientDetailsActivity.this , LoginActivity.class));
        finish();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
    }

    public void finishCurrTreatment(View view) {
        final Dialog feedbackDialog = new Dialog(PatientDetailsActivity.this);
        feedbackDialog.setContentView(R.layout.feedback_dialog);
        feedbackDialog.setCancelable(true);
        feedbackDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        feedbackDialog.show();
        final EditText doctorE = feedbackDialog.findViewById(R.id.editText_doctor_feedback) , hospitalE = feedbackDialog.findViewById(R.id.editText_hospital_feedback) ,
                moneyE = feedbackDialog.findViewById(R.id.editText_money_feedback);
        Button submit = feedbackDialog.findViewById(R.id.button_submit_feedback);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctor = doctorE.getText().toString() , hospital = hospitalE.getText().toString() , money = moneyE.getText().toString() ;
                if (doctor.length() != 0 || hospital.length() != 0 || money.length() != 0) {
                    FIREBASE_FIRESTORE.collection("PATIENTS").document(PATIENT_ID).update("mgm_feedback" , doctor + "," +
                            hospital + "," + money).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FIREBASE_FIRESTORE.collection("HOSPITAL/M G M HOSPITAL/DR ROHIT").document("Patients").get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<String > patientsList = (List<String>) task.getResult().get("patients");
                                                    patientsList.remove(PATIENT_ID);
                                                    FIREBASE_FIRESTORE.collection("HOSPITAL/M G M HOSPITAL/DR ROHIT").document("Patients")
                                                            .update("patients" , patientsList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                FIREBASE_FIRESTORE.collection("PATIENTS").document(PATIENT_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            String timestamp = getDateTime() ,
                                                                                    lastHash = PATIENT_MODEL.blockchain.chain.get(PATIENT_MODEL.blockchain.chain.size() -1).hash;
                                                                            String hash = null;
                                                                            String data = "Patient has opted to finish Ongoing treatment";
                                                                            try {
                                                                                hash = Block.getHash(timestamp , lastHash , data);
                                                                            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            String access = "1,2,3,5";
                                                                            final Block block = new Block(timestamp , lastHash , hash , data , Arrays.asList(access.split(",")));
                                                                            String blockchain = task.getResult().getString("blockchain");
                                                                            blockchain += ">" + timestamp + ";" + lastHash + ";" + hash + ";" + data + ";" + access;
                                                                            FIREBASE_FIRESTORE.collection("PATIENTS").document(PATIENT_ID)
                                                                                    .update("blockchain" , blockchain).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        chatDetailsRecyclerviewAdapter = new ChatDetailsRecyclerviewAdapter(PATIENT_MODEL.blockchain.chain);
                                                                                        PATIENT_MODEL.blockchain.addBlock(block);
                                                                                        chatDetailsRecyclerviewAdapter.notifyDataSetChanged();
                                                                                        Button button = findViewById(R.id.button2);
                                                                                        button.setVisibility(View.GONE);
                                                                                        feedbackDialog.dismiss();
                                                                                    } else {
                                                                                        Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                        } else {
                                                                            Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(PatientDetailsActivity.this, "All Ratings required !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addBill(View view) {
        final EditText bill = findViewById(R.id.edit_text_bill_actor_hm);;
        final String billT = bill.getText().toString() + "\n\t\t\t by M G M Hospital";
        if (billT.length() != 0 ) {
            FIREBASE_FIRESTORE.collection("PATIENTS").document(DOCTOR_MODEL.patientModelList.get(position).getId()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String timestamp = getDateTime() ,
                                        lastHash = DOCTOR_MODEL.patientModelList.get(position).blockchain.chain.get(DOCTOR_MODEL.patientModelList.get(position).blockchain.chain.size() -1).hash;
                                String hash = null;
                                try {
                                    hash = Block.getHash(timestamp , lastHash , billT);
                                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                String access = "1,3";
                                final Block block = new Block(timestamp , lastHash , hash , billT , Arrays.asList(access.split(",")));
                                String blockchain = task.getResult().getString("blockchain");
                                blockchain += ">" + timestamp + ";" + lastHash + ";" + hash + ";" + billT + ";" + access;
                                FIREBASE_FIRESTORE.collection("PATIENTS").document(DOCTOR_MODEL.patientModelList.get(position).getId())
                                        .update("blockchain" , blockchain).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DOCTOR_MODEL.patientModelList.get(position).blockchain.addBlock(block);
                                            chatDetailsRecyclerviewAdapter.notifyDataSetChanged();
                                            bill.setText("");
                                        } else {
                                            Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(PatientDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "This field can't be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
