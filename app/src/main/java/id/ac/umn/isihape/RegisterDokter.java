package id.ac.umn.isihape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterDokter extends AppCompatActivity {

    private TextView typeUser;
    private String email, password, userType;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private EditText userName, userTelp, userNIK, userLahir, userAlamat, userEmail, userHarga, userSpesialis;
    private ProgressDialog loadingBar;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dokter);
        setTitle("Register");
        mAuth = FirebaseAuth.getInstance();
        //RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference();
        Intent myIntent = getIntent();
        email = myIntent.getStringExtra("email");
        password= myIntent.getStringExtra("password");
        userType= myIntent.getStringExtra("userType");

        typeUser= (TextView) findViewById(R.id.userType);
        typeUser.setText(userType);
        InitializeFields();
        userEmail.setText(email);

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

        userLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterDokter.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String name =userName.getText().toString();
        String notelp =userTelp.getText().toString();
        String nik =userNIK.getText().toString();
        String tanggalLahir =userLahir.getText().toString();
        String alamat =userAlamat.getText().toString();
        String harga = userHarga.getText().toString();
        String spesialis = userSpesialis.getText().toString();
        email = userEmail.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Tolong isi nama. . .", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(notelp)){
            Toast.makeText(this, "Tolong isi no telepon. . .", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(nik)){
            Toast.makeText(this, "Tolong isi NIK. . .", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tanggalLahir)){
            Toast.makeText(this, "Tolong isi tanggal lahir. . .", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(alamat)){
            Toast.makeText(this, "Tolong isi alamat. . .", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(harga)){
            Toast.makeText(this, "Tolong isi harga. . .", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(alamat)){
            Toast.makeText(this, "Tolong isi spesialis. . .", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Membuat akun");
            loadingBar.setMessage("Mohon menunggu . . .");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("database", RootRef.getRoot().toString());
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object> usersMap = new HashMap<>();
                        usersMap.put("userType", userType);
                        usersMap.put("name", userName.getText().toString());
                        usersMap.put("notelp", userTelp.getText().toString());
                        usersMap.put("NIK", userNIK.getText().toString());
                        usersMap.put("tanggalLahir", userLahir.getText().toString());
                        usersMap.put("alamat", userAlamat.getText().toString());
                        usersMap.put("harga", userHarga.getText().toString());
                        usersMap.put("spesialis", userSpesialis.getText().toString());

                        RootRef.child("Users").child(currentUserID).setValue(usersMap);

                        SendUserToMainActivity();
                        Toast.makeText(RegisterDokter.this, "Account created successfuly", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(RegisterDokter.this, "Error: " + message, Toast.LENGTH_LONG).show();
                    }
                    loadingBar.dismiss();
                }
            });
        }

    }

    private void InitializeFields() {
        registerBtn = (Button) findViewById(R.id.register_button2);
        userName = (EditText) findViewById(R.id.register_name);
        userTelp = (EditText) findViewById(R.id.register_notelp);
        userNIK = (EditText) findViewById(R.id.register_nik);
        userLahir = (EditText) findViewById(R.id.register_lahir);
        userAlamat = (EditText) findViewById(R.id.register_alamat);
        userEmail = (EditText) findViewById(R.id.register_email);
        userHarga = (EditText) findViewById(R.id.register_harga);
        userSpesialis = (EditText) findViewById(R.id.register_spesialis);

        loadingBar = new ProgressDialog(this);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        userLahir.setText(sdf.format(myCalendar.getTime()));
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}