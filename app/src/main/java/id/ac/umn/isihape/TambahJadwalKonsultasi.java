package id.ac.umn.isihape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class TambahJadwalKonsultasi extends AppCompatActivity {

    private DatePicker etTanggal;
    private EditText etDokter;
    private TimePicker etWaktu;
    private Button btnTambahJadwalKonsultasi;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jadwal_konsultasi);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference();

        etTanggal = (DatePicker) findViewById(R.id.etTanggalKonsultasi);
        etDokter = (EditText) findViewById(R.id.etDokterKonsultasi);
        etWaktu = (TimePicker) findViewById(R.id.etWaktuKonsultasi);
        btnTambahJadwalKonsultasi = (Button) findViewById(R.id.tambahJadwalKonsultasi);

        btnTambahJadwalKonsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day  = etTanggal.getDayOfMonth();
                int month= etTanggal.getMonth();
                int year = etTanggal.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formatedDate = sdf.format(calendar.getTime());

                String AM_PM;
                if(etWaktu.getHour() < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }

                String time = etWaktu.getHour() + ":" + etWaktu.getMinute() + " " + AM_PM;
                String dokter = etDokter.getText().toString();

                HashMap<String, String> jadwalMap = new HashMap<>();
                    jadwalMap.put("uid", currentUserId);
                    jadwalMap.put("tanggal", formatedDate);
                    jadwalMap.put("dokter", dokter);
                    jadwalMap.put("waktu", time);
                String key = RootRef.child("Jadwal").child(currentUserId).push().getKey();
                RootRef.child("Jadwal").child(currentUserId).child(key).setValue(jadwalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SendUserToMainActivity();
                            Toast.makeText(TambahJadwalKonsultasi.this, "Jadwal berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(TambahJadwalKonsultasi.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(TambahJadwalKonsultasi.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}