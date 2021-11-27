package id.ac.umn.isihape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import id.ac.umn.isihape.ui.doctors.DoctorsFragment;

public class TambahDokter extends AppCompatActivity {
    private EditText etNamaDokter, etSpesialisDokter, etAlamatDokter, etHargaDokter;
    private Button tambahDokter;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_dokter);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference();

        etNamaDokter = (EditText) findViewById(R.id.etNamaDokter);
        etSpesialisDokter = (EditText) findViewById(R.id.etSpesialisDokter);
        etAlamatDokter = (EditText) findViewById(R.id.etAlamatDokter);
        etHargaDokter = (EditText) findViewById(R.id.etHargaDokter);
        tambahDokter = (Button) findViewById(R.id.tambahDokter);

        tambahDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaDokter = etNamaDokter.getText().toString();
                String spesialisDokter = etSpesialisDokter.getText().toString();
                String alamatDokter = etAlamatDokter.getText().toString();
                String hargaDokter = etHargaDokter.getText().toString();

                HashMap<String, String> dokterMap = new HashMap<>();
                dokterMap.put("uid", currentUserId);
                dokterMap.put("nama", namaDokter);
                dokterMap.put("spesialis", spesialisDokter);
                dokterMap.put("harga", hargaDokter);
                dokterMap.put("alamat", alamatDokter);
                String key = RootRef.child("Dokter").child(currentUserId).push().getKey();
                RootRef.child("Dokter").child(currentUserId).child(key).setValue(dokterMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TambahDokter.this, "Jadwal berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            SendUserToDoctors();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(TambahDokter.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void SendUserToDoctors() {
        finish();
    }
}