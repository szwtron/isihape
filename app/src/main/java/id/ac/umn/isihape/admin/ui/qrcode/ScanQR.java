package id.ac.umn.isihape.admin.ui.qrcode;

import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;
import id.ac.umn.isihape.R;

public class ScanQR extends AppCompatActivity {

    private ScannerLiveView camera;
    private TextView scannedTV;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference RootRef;
    private DatabaseReference antrianRef;
    private DatabaseReference usersRef;

    private String namaUser;
    private String retrieveUid;
    private boolean exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference();
        antrianRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Antrian");
        usersRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");

        // check permission method is to check that the
        // camera permission is granted by user or not.
        // request permission method is to request the
        // camera permission if not given.
        if (checkPermission()) {
            // if permission is already granted display a toast message
            Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        // initialize scannerLiveview and textview.
        scannedTV = findViewById(R.id.idTVscanned);
        camera = (ScannerLiveView) findViewById(R.id.camview);

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started
                Toast.makeText(ScanQR.this, "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // method is called when scanner is stopped.
                Toast.makeText(ScanQR.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                // method is called when scanner gives some error.
                Toast.makeText(ScanQR.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                // method is called when camera scans the
                // qr code and the data from qr code is
                // stored in data in string format.
                scannedTV.setText(data);
                retrieveUid = data.toString();

                RootRef.child("Users").child(retrieveUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        namaUser = snapshot.child("name").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                antrianRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild("uid")){
                            if(retrieveUid == snapshot.child("uid").getValue().toString()){
                                exist = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(exist = false){
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a");

                    String formatedDate = sdf.format(calendar.getTime());
                    String time = sdfTime.format(calendar.getTime());

                    Log.d("test", formatedDate);
                    Log.d("test", time);

                    HashMap<String, String> antrianMap = new HashMap<>();
                    antrianMap.put("uid", retrieveUid);
                    antrianMap.put("waktu", time);
                    antrianMap.put("tanggal", formatedDate);

                    String key = RootRef.child("Antrian").push().getKey();
                    RootRef.child("Antrian").child(key).setValue(antrianMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ScanQR.this, "Antrian " + namaUser + " dengan UID: " + retrieveUid + " berhasil ditambahkan ke daftar antrian", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanQR.this);
                    builder.setCancelable(true);
                    builder.setTitle("User ini sudah ada didalam antrian!");
                    builder.setMessage("User ID: " + retrieveUid);
                    builder.setPositiveButton("Baiklah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        // 0.5 is the area where we have
        // to place red marker for scanning.
        decoder.setScanAreaPercent(0.8);
        // below method will set secoder to camera.
        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    protected void onPause() {
        // on app pause the
        // camera will stop scanning.
        camera.stopScanner();
        super.onPause();
    }

    private boolean checkPermission() {
        // here we are checking two permission that is vibrate
        // and camera which is granted by user and not.
        // if permission is granted then we are returning
        // true otherwise false.
        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 101;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted && vibrateaccepted) {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denined \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

}