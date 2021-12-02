package id.ac.umn.isihape;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EditProfile extends AppCompatActivity {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private EditText namaProfile, notelpProfile, nikProfile, tanggalLahirProfile, alamatProfile;
    private Button editBtn, btnEditProfileImg;
    private String currentUserID, userType;
    private ImageView editProfileImg;
    private String currentPhotoPath;
    private String uploadedImageURL;
    private FirebaseStorage storage;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference storageReference;


    static final int PICKER_REQUEST_CODE = 30;

    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        InitializeFields();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        RootRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("asdd", String.valueOf(snapshot.child("name").getValue()));
                String retrieveNama = snapshot.child("name").getValue().toString();
                String retrieveNik = snapshot.child("NIK").getValue().toString();
                String retrieveTanggalLahir = snapshot.child("tanggalLahir").getValue().toString();
                String retrieveAlamat = snapshot.child("alamat").getValue().toString();
                String retrieveNoTelp = snapshot.child("notelp").getValue().toString();

                storageReference.child("users/"+currentUserID+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(uri != null){
                            Log.d("tag", uri.toString());
                            editProfileImg.setImageURI(uri);
                        }
                    }
                });

//                if(snapshot.child("image").getValue() == null){
//                    currentPhotoURL = "";
//                } else {
//                    currentPhotoURL = snapshot.child("image").getValue().toString();
//                }

                if(snapshot.child("image").getValue() != null){
                    String retrieveImage = snapshot.child("image").getValue().toString();
                    Log.d("tag", retrieveImage);
                    StorageReference httpsReference = storage.getReferenceFromUrl(retrieveImage);
                    httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("tag", uri.toString());
                            Picasso.get().load(uri.toString()).transform(new CropCircleTransformation()).into(editProfileImg);
                        }
                    });
                }

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
                usersMap.put("image", uploadedImageURL);



                RootRef.child(currentUserID).updateChildren(usersMap);
                SendUserToPatiens();
            }
        });

        btnEditProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
            }
        });
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
            //openCamera();
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                editProfileImg.setImageURI(Uri.fromFile(f));
                Log.d("tag",  currentPhotoPath  );
                Log.d("tag", "Absolute Url of image: " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(), contentUri);

            }
        }
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        StorageReference image = storageReference.child("users/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         Log.d("tag", "iamge uri: " + uri);
                         uploadedImageURL = uri.toString();
                         Log.d("tag", "onSuccess: Uploaded image url is: " + uri.toString());
                     }
                 });
                Toast.makeText(EditProfile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // users/jpeg

    private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        String imageFileName = currentUserID;
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error: " + ex, Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "id.ac.umn.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERM_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
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
        editProfileImg = findViewById(R.id.editProfileImg);
        btnEditProfileImg = findViewById(R.id.btnEditProfileImg);
    }
}