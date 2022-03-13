package com.somanibrothersservices.digitalhealthprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.somanibrothersservices.digitalhealthprofile.DBqueries.DOCTOR_MODEL;
import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.ACTOR;
import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.FIREBASE_FIRESTORE;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ACTOR.equals("doctor") || ACTOR.equals("medical student")) {
            RecyclerView recyclerView = findViewById(R.id.chat_list);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            DBqueries.getPatients(MainActivity.this, recyclerView);
        } else if (ACTOR.equals("hospital management")) {
            final Button admitPatientButton = findViewById(R.id.button_patient_hm) , patientList = findViewById(R.id.button_patient_list_hm);
            admitPatientButton.setVisibility(View.VISIBLE);
            patientList.setVisibility(View.VISIBLE);
            admitPatientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this , AdmitPatientActivity.class));
                }
            });
            patientList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    admitPatientButton.setVisibility(View.INVISIBLE);
                    patientList.setVisibility(View.INVISIBLE);
                    RecyclerView recyclerView = findViewById(R.id.chat_list);
                    recyclerView.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    DBqueries.getPatients(MainActivity.this, recyclerView);
                }
            });
        }

//        Blockchain blockchain = new Blockchain();
//        try {
//            blockchain.addBlock("First");
//            blockchain.addBlock("Second");
//            String chain = new String();
//            for (Block block : blockchain.chain) {
//                chain += block.blockToString() + "\n" ;
//            }
////            TextView textView = findViewById(R.id.textView);
////            textView.setText(chain);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void display(View view) {
    }

    public void logOut(View view) {
        startActivity(new Intent(MainActivity.this , LoginActivity.class));
        finish();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
    }
}
