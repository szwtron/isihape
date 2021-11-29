package id.ac.umn.isihape.ui.patients;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import id.ac.umn.isihape.EditProfile;
import id.ac.umn.isihape.R;
import id.ac.umn.isihape.TambahDokter;
//import id.ac.umn.isihape.databinding.FragmentHomeBinding;

public class PatientsFragment extends Fragment {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private String currentUserID;
    private Button ubahprofil;
    private TextView uId, namaProfile,nikProfile,tanggalLahirProfile,alamatProfile,notelpProfile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patients, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //fieldInitialize
        ubahprofil = (Button) root.findViewById(R.id.ubahprofil);
        namaProfile = (TextView) root.findViewById(R.id.namaProfile);
        nikProfile = (TextView) root.findViewById(R.id.nikProfile);
        tanggalLahirProfile = (TextView) root.findViewById(R.id.tanggalLahirProfile);
        alamatProfile = (TextView) root.findViewById(R.id.alamatProfile);
        notelpProfile = (TextView) root.findViewById(R.id.notelpProfile);
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");
        uId = (TextView) root.findViewById(R.id.Uid);
        uId.setText(currentUserID);
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
