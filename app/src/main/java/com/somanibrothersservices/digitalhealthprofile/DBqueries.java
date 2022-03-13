package com.somanibrothersservices.digitalhealthprofile;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.FIREBASE_FIRESTORE;

public class DBqueries {

    public static DoctorModel DOCTOR_MODEL;
    public static PatientModel PATIENT_MODEL;

    public static void getPatients(final Context context , final RecyclerView recyclerView) {
        FIREBASE_FIRESTORE.collection("HOSPITAL/M G M HOSPITAL/DR ROHIT").document("Patients")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    DOCTOR_MODEL = new DoctorModel(documentSnapshot.getString("name") , documentSnapshot.getString("qual"));
                    final List<String> patientIdList = (List<String>) documentSnapshot.get("patients");
                    final List<PatientModel> patientModels = new ArrayList<>();
                    for (final String id : patientIdList) {
                        FIREBASE_FIRESTORE.collection("PATIENTS").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    try {
                                        patientModels.add(new PatientModel(task.getResult().getString("id"),
                                                task.getResult().getString("name") , task.getResult().getString("age"),
                                                task.getResult().getString("image"),task.getResult().getString("blockchain")));
//                                        Toast.makeText(context, "Got it", Toast.LENGTH_SHORT).show();
                                        if (id == patientIdList.get(patientIdList.size() - 1)) {
                                            DOCTOR_MODEL.setPatientModelList(patientModels);
                                            ChatReceyclerviewAdapter chatReceyclerviewAdapter = new ChatReceyclerviewAdapter(DOCTOR_MODEL.patientModelList);
                                            recyclerView.setAdapter(chatReceyclerviewAdapter);
                                            chatReceyclerviewAdapter.notifyDataSetChanged();
                                        }
                                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void getPatientHistory(final Context context , final RecyclerView recyclerView , final String id) {
        FIREBASE_FIRESTORE.collection("PATIENTS").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                        PATIENT_MODEL = new PatientModel(id , task.getResult().getString("name") , " "
                        , task.getResult().getString("age") , task.getResult().getString("blockchain"));
                        ChatDetailsRecyclerviewAdapter chatDetailsRecyclerviewAdapter = new ChatDetailsRecyclerviewAdapter(PATIENT_MODEL.blockchain.chain);
                        recyclerView.setAdapter(chatDetailsRecyclerviewAdapter);
                        chatDetailsRecyclerviewAdapter.notifyDataSetChanged();
                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
