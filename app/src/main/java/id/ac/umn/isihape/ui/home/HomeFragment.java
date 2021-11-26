package id.ac.umn.isihape.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

import id.ac.umn.isihape.Login;
import id.ac.umn.isihape.MainActivity;
import id.ac.umn.isihape.R;
import id.ac.umn.isihape.RegisterPage;
import id.ac.umn.isihape.TambahJadwalKonsultasi;

public class HomeFragment extends Fragment {

    private RecyclerView rvJadwalKonsultasi;
    //DaftarJadwalAdapter homeJadwalAdapter;
    LinkedList<SumberJadwal> daftarJadwal = new LinkedList<>();

    //edit
    private Button btnTambahJadwal;
    private EditText etEditTanggal;
    private EditText etEditDokter;
    private EditText etEditWaktu;

    //private ArrayList<ModelJadwal> arrayDaftarJadwal;

    //firebase
    private DatabaseReference homeDatabaseReference;
    private FirebaseDatabase homeFirebaseInstance;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvJadwalKonsultasi = (RecyclerView) root.findViewById(R.id.rvJadwalKonsultasi);

        rvJadwalKonsultasi.setLayoutManager(new LinearLayoutManager(getContext()));

        //firebase init
        FirebaseApp.initializeApp(getActivity());
        homeFirebaseInstance = FirebaseDatabase.getInstance();
        homeDatabaseReference = homeFirebaseInstance.getReference("jadwal");


        btnTambahJadwal = (Button) root.findViewById(R.id.tambahJadwalKonsultasi);

        btnTambahJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambahJadwalIntent = new Intent(getActivity(), TambahJadwalKonsultasi.class);
                startActivity(tambahJadwalIntent);
            }
        });

//        homeDatabaseReference.child("data_jadwal").addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                arrayDaftarJadwal = new ArrayList<>();
////
////                for(DataSnapshot homeDataSnapshot : snapshot.getChildren()) {
////                    ModelJadwal jadwal = homeDataSnapshot.getValue(ModelJadwal.class);
////                    //jadwal.setKey(homeDataSnapshot.getKey());
////                    arrayDaftarJadwal.add(jadwal);
////                }
////
////                homeJadwalAdapter = new MainAdapter(MainActivity.this, arrayDaftarJadwal);
////
////            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        })



        return root;
    }



//    @Override
//    public void onStart(){
//        FirebaseRecyclerOptions<SumberJadwal> options =
//                new FirebaseRecyclerOptions.Builder<SumberJadwal>()
//                .setQuery()
//                .build();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}