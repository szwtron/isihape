package id.ac.umn.isihape.admin.ui.CRUDDoctors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.admin.AdminActivity;
import id.ac.umn.isihape.admin.ui.CRUDLab.EditLab;

public class EditDoctors extends AppCompatActivity {
    private String id, nik, name, harga, spesialis;
    private EditText etNama,etharga, etSpesialisDokter, etNik;
    private Button editBtn;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctors);
        setTitle("Edit Doctor");

        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");
        Intent intent = getIntent();
        Log.d("DokterIntent", intent.getStringExtra("name"));

        id = intent.getStringExtra("id");
        nik = intent.getStringExtra("NIK");
        name = intent.getStringExtra("name");
        harga = intent.getStringExtra("harga");
        spesialis = intent.getStringExtra("spesialis");

        Log.d("DokterIntentName", name);

        initializeField();

        etNama.setText(name);
        etSpesialisDokter.setText(spesialis);
        etharga.setText(harga);
        etNik.setText(nik);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                harga= etharga.getText().toString();

                HashMap<String, Object> docMap = new HashMap<>();
                docMap.put("harga", harga);
                if(harga.isEmpty() ){
                    Toast.makeText(EditDoctors.this, "Semua field haris diisi", Toast.LENGTH_SHORT).show();
                }
                else{
                    RootRef.child(id).updateChildren(docMap);
                    Toast.makeText(EditDoctors.this, "Lab berhasil diedit", Toast.LENGTH_SHORT).show();
                    SendtoCRUDDoctors();
                }
            }
        });
    }

    public void SendtoCRUDDoctors() {
        finish();
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void initializeField(){
        etNama= (EditText) findViewById(R.id.etNamaDokter);
        etharga= (EditText) findViewById(R.id.etHargaDokter);
        etSpesialisDokter= (EditText) findViewById(R.id.etSpesialisDokter);
        etNik= (EditText) findViewById(R.id.etNIKDokter);
        editBtn=(Button) findViewById(R.id.editDocs);
    }
}