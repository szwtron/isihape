package id.ac.umn.isihape.admin.ui.CRUDLab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import id.ac.umn.isihape.R;

public class TambahLab extends AppCompatActivity{

    private EditText etNamaLab, etDeskripsiLab, etHargaLab;
    private Button tambahLab;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lab);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference();

        etNamaLab = (EditText) findViewById(R.id.etNamaLab);
        etDeskripsiLab = (EditText) findViewById(R.id.etDeskripsiLab);
        etHargaLab = (EditText) findViewById(R.id.etHargaLab);
        tambahLab = (Button) findViewById(R.id.tambahLab);

        tambahLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaLab = etNamaLab.getText().toString();
                String deskripsiLab = etDeskripsiLab.getText().toString();
                String hargaLab = etHargaLab.getText().toString();

                HashMap<String, String> labMap = new HashMap<>();
                labMap.put("uid", currentUserId);
                labMap.put("nama", namaLab);
                labMap.put("deskripsi", deskripsiLab);
                labMap.put("harga", hargaLab);
                String key = RootRef.child("Lab").push().getKey();
                RootRef.child("Lab").child(key).setValue(labMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TambahLab.this, "Lab berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            finishActivity();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(TambahLab.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void finishActivity() {
        finish();
    }
}