package com.somanibrothersservices.digitalhealthprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PharmacistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist);

        final EditText idE = findViewById(R.id.edit_text_unique_id_pharmacist);
        Button load = findViewById(R.id.button_load_pharmacist);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idE.getText().toString();
                if (id.length() != 0) {
                    RecyclerView recyclerView = findViewById(R.id.recycler_view_medicines);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PharmacistActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    DBqueries.getPatientHistory(PharmacistActivity.this , recyclerView , id);
                    idE.setText("");
                } else {
                    Toast.makeText(PharmacistActivity.this, "Unique Id required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logOut(View view) {
        startActivity(new Intent(PharmacistActivity.this , LoginActivity.class));
        finish();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
    }
}
