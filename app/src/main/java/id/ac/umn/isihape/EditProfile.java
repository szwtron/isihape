package id.ac.umn.isihape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import id.ac.umn.isihape.ui.patients.PatientsFragment;

public class EditProfile extends AppCompatActivity {
    private EditText namaProfile, notelpProfile, nikProfile, tanggalLahirProfile, alamatProfile;
    private Button editBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private String currentUserID, userType;

    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        InitializeFields();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");

        RootRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("asdd", String.valueOf(snapshot.child("name").getValue()));
                String retrieveNama = snapshot.child("name").getValue().toString();
                String retrieveNik = snapshot.child("NIK").getValue().toString();
                String retrieveTanggalLahir = snapshot.child("tanggalLahir").getValue().toString();
                String retrieveAlamat = snapshot.child("alamat").getValue().toString();
                String retrieveNoTelp = snapshot.child("notelp").getValue().toString();
                Log.d("asd","asd");
                Log.d("asd1",retrieveAlamat);
                namaProfile.setText(retrieveNama);
                nikProfile.setText(retrieveNik);
                tanggalLahirProfile.setText(retrieveTanggalLahir);
                alamatProfile.setText(retrieveAlamat);
                notelpProfile.setText(retrieveNoTelp);
                userType = snapshot.child("userType").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        tanggalLahirProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama =namaProfile.getText().toString();
                String notelp =notelpProfile.getText().toString();
                String nik =nikProfile.getText().toString();
                String tanggalLahir =tanggalLahirProfile.getText().toString();
                String alamat =alamatProfile.getText().toString();
                HashMap<String, Object> usersMap = new HashMap<>();

                usersMap.put("NIK", nik);
                usersMap.put("alamat", alamat);
                usersMap.put("name", nama);
                usersMap.put("notelp", notelp);
                usersMap.put("tanggalLahir", tanggalLahir);
                usersMap.put("userType", userType);

                RootRef.child(currentUserID).updateChildren(usersMap);
                SendUserToPatiens();
            }
        });
    }
    
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tanggalLahirProfile.setText(sdf.format(myCalendar.getTime()));
    }

    private void SendUserToPatiens() {
        finish();
    }
    private void InitializeFields() {
        editBtn = (Button) findViewById(R.id.edit_profile);
        namaProfile = (EditText) findViewById(R.id.namaProfile);
        notelpProfile = (EditText) findViewById(R.id.notelpProfile);
        nikProfile = (EditText) findViewById(R.id.nikProfile);
        tanggalLahirProfile = (EditText) findViewById(R.id.tanggalLahirProfile);
        alamatProfile = (EditText) findViewById(R.id.alamatProfile);
    }
}