package id.ac.umn.isihape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opensooq.supernova.gligar.GligarPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BuatJanji extends AppCompatActivity {

    private EditText buatJanjiNama, buatJanjiSpesial, buatJanjiHarga, buatJanjiAlamat;
    private DatePicker buatJanjiTanggal;
    private TimePicker buatJanjiWaktu;
    private Button tambahJanji;


    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_janji);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference();

        InitializeFields();

        Intent intent = getIntent();
        String Nama = (String) intent.getExtras().get("nama");
        String Spesialis = (String) intent.getExtras().get("spesialis");
        String Harga = (String) intent.getExtras().get("harga");
        String Alamat = (String) intent.getExtras().get("alamat");
        String Type = (String) intent.getExtras().get("type");
        String idStaff = (String) intent.getExtras().get("idstaff");
        String idPasien = currentUserId;

        buatJanjiNama.setText(Nama);
        buatJanjiSpesial.setText(Spesialis);
        buatJanjiHarga.setText(Harga);

        tambahJanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day  = buatJanjiTanggal.getDayOfMonth();
                int month= buatJanjiTanggal.getMonth();
                int year = buatJanjiTanggal.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formatedDate = sdf.format(calendar.getTime());

                String AM_PM;
                if(buatJanjiWaktu.getHour() < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }

                String time = buatJanjiWaktu.getHour() + ":" + buatJanjiWaktu.getMinute() + " " + AM_PM;
                String nama = buatJanjiNama.getText().toString();
                String spesial = buatJanjiSpesial.getText().toString();
                String harga = buatJanjiHarga.getText().toString();

                HashMap<String, String> janjiMap = new HashMap<>();
                janjiMap.put("dokter", nama);
                janjiMap.put("spesial", spesial);
                janjiMap.put("harga", harga);
                janjiMap.put("tanggal", formatedDate);
                janjiMap.put("waktu", time);
                janjiMap.put("type", Type);
                janjiMap.put("idstaff", idStaff);
                janjiMap.put("idpasien", idPasien);
                janjiMap.put("status", "diproses");

                String key = RootRef.child("Jadwal").push().getKey();
                RootRef.child("Jadwal").child(key).setValue(janjiMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SendUserToMainActivity();
                            Toast.makeText(BuatJanji.this, "Jadwal appointment berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(BuatJanji.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(BuatJanji.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void InitializeFields() {
        buatJanjiNama = findViewById(R.id.buatJanjiNama);
        buatJanjiSpesial = findViewById(R.id.buatJanjiSpesial);
        buatJanjiHarga = findViewById(R.id.buatJanjiHarga);
        buatJanjiTanggal = findViewById(R.id.buatJanjiTanggal);
        buatJanjiWaktu = findViewById(R.id.buatJanjiWaktu);
        tambahJanji = findViewById(R.id.tambahJanji);

    }
}