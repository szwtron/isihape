package id.ac.umn.isihape.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import id.ac.umn.isihape.EditProfile;
import id.ac.umn.isihape.R;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
//import id.ac.umn.isihape.databinding.FragmentHomeBinding;

public class ProfileFragment extends Fragment {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    private String currentUserID;
    private Button ubahprofil;
    private TextView uId, namaProfile,nikProfile,tanggalLahirProfile,alamatProfile,notelpProfile;
    private ImageView userProfileImg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patients, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        //fieldInitialize
        ubahprofil = (Button) root.findViewById(R.id.ubahprofil);
        namaProfile = (TextView) root.findViewById(R.id.namaProfile);
        nikProfile = (TextView) root.findViewById(R.id.nikProfile);
        tanggalLahirProfile = (TextView) root.findViewById(R.id.tanggalLahirProfile);
        alamatProfile = (TextView) root.findViewById(R.id.alamatProfile);
        notelpProfile = (TextView) root.findViewById(R.id.notelpProfile);
        userProfileImg = (ImageView) root.findViewById(R.id.userProfile);
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");
        //uId = (TextView) root.findViewById(R.id.Uid);
        //uId.setText(currentUserID);

        Log.d("tag", "banana");
//
//        StorageReference imagesRef = storageReference.child("users");
//        Log.d("tag", imagesRef.toString());
//
//        StorageReference imagesRef2 = storageReference.child("users/"+currentUserID+".jpg");
//        //File f = new File(imagesRef2);
//
//        Log.d("tag", imagesRef2.toString());

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

                  if(snapshot.child("image").getValue() != null){
                      String retrieveImage = snapshot.child("image").getValue().toString();
                      Log.d("tag", retrieveImage);
                      StorageReference httpsReference = storage.getReferenceFromUrl(retrieveImage);
                      httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              Log.d("tag", uri.toString());
                              Picasso.get().load(uri.toString()).transform(new CropCircleTransformation()).into(userProfileImg);
                          }
                      });
                  }
                  namaProfile.setText(retrieveNama);
                  nikProfile.setText(retrieveNik);
                  tanggalLahirProfile.setText(retrieveTanggalLahir);
                  alamatProfile.setText(retrieveAlamat);
                  notelpProfile.setText(retrieveNoTelp);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
        });
        //fieldInitialize

        ubahprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ubahProfile = new Intent(getActivity(), EditProfile.class);
                startActivity(ubahProfile);
            }
        });

        return root;
    }
    public void onStart() {
        super.onStart();
    }

    public void initializeField(){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
