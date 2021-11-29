package id.ac.umn.isihape.admin.ui.CRUDLab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import id.ac.umn.isihape.R;

public class EditLab extends AppCompatActivity {
    private String id, nama, harga, deskripsi;
    private EditText etNama,etharga, etDeskripsi;
    private Button editBtn;
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lab);
        setTitle("Edit Lab");

        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Lab");
        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        nama = intent.getStringExtra("nama");
        harga = intent.getStringExtra("harga");
        deskripsi = intent.getStringExtra("deksripsi");

        initializeField();

        etNama.setText(nama);
        etDeskripsi.setText(deskripsi);
        etharga.setText(harga);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama= etNama.getText().toString();
                harga= etharga.getText().toString();
                deskripsi= etDeskripsi.getText().toString();

                HashMap<String, Object> labMap = new HashMap<>();
                labMap.put("nama", nama);
                labMap.put("deskripsi", deskripsi);
                labMap.put("harga", harga);
                if(nama.isEmpty() || harga.isEmpty() || deskripsi.isEmpty()){
                    Toast.makeText(EditLab.this, "Semua field haris diisi", Toast.LENGTH_SHORT).show();
                }
                else{
                    RootRef.child(id).updateChildren(labMap);
                    Toast.makeText(EditLab.this, "Lab berhasil diedit", Toast.LENGTH_SHORT).show();
                    SendtoCRUDLab();
                }


            }
        });

    }
    public void SendtoCRUDLab() {
        finish();
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void initializeField(){
        etNama= (EditText) findViewById(R.id.etNamaLab);
        etharga= (EditText) findViewById(R.id.etHargaLab);
        etDeskripsi= (EditText) findViewById(R.id.etDeskripsiLab);
        editBtn=(Button) findViewById(R.id.editLab);
    }
}