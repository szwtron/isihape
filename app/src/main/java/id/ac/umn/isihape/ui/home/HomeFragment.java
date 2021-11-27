package id.ac.umn.isihape.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference jadwalKonsultasiRef;
    private FirebaseAuth mAuth;

    private String currentUserID;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvJadwalKonsultasi = (RecyclerView) root.findViewById(R.id.rvJadwalKonsultasi);
        rvJadwalKonsultasi.setLayoutManager(new LinearLayoutManager(getContext()));

        //firebase init
        FirebaseApp.initializeApp(getActivity());
        //homeFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        jadwalKonsultasiRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Jadwal");


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



    @Override
    public void onStart(){
        super.onStart();
        FirebaseRecyclerOptions<SumberJadwal> options =
                new FirebaseRecyclerOptions.Builder<SumberJadwal>()
                .setQuery(jadwalKonsultasiRef.child(currentUserID), SumberJadwal.class)
                .build();
        FirebaseRecyclerAdapter<SumberJadwal, JadwalKonsultasiViewHolder> adapter =
                new FirebaseRecyclerAdapter<SumberJadwal, JadwalKonsultasiViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull JadwalKonsultasiViewHolder jadwalKonsultasiViewHolder, int i, @NonNull SumberJadwal sumberJadwal) {
                        final String list_user_id = getRef(i).getKey();
                        DatabaseReference getJadwalRef = getRef(i).getRef();

                        jadwalKonsultasiViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Alert dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setCancelable(true);
                                builder.setTitle("Kamu yakin mau menghapus appointment ini?");
                                builder.setMessage("This action cannot be undone!");
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getJadwalRef.removeValue();
                                    }
                                });
                                builder.show();



                            }
                        });

                        getJadwalRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if((snapshot.exists()) && snapshot.hasChild("tanggal") && (snapshot.hasChild("dokter") && (snapshot.hasChild("waktu")))){
                                    String retrieveTanggal = snapshot.child("tanggal").getValue().toString();
                                    String retrieveDokter = snapshot.child("dokter").getValue().toString();
                                    String retrieveWaktu = snapshot.child("waktu").getValue().toString();

                                    jadwalKonsultasiViewHolder.tanggal.setText(retrieveTanggal);
                                    jadwalKonsultasiViewHolder.dokter.setText(retrieveDokter);
                                    jadwalKonsultasiViewHolder.waktu.setText(retrieveWaktu);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public JadwalKonsultasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jadwal_konsultasi_layout, parent, false);
                        JadwalKonsultasiViewHolder holder = new JadwalKonsultasiViewHolder(view);
                        return holder;
                    }
                };
        rvJadwalKonsultasi.setAdapter(adapter);
        adapter.startListening();
    }

    public static class JadwalKonsultasiViewHolder extends RecyclerView.ViewHolder{
        TextView tanggal, dokter, waktu;
        ImageButton deleteBtn;

        public JadwalKonsultasiViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tvTanggalKonsultasi);
            dokter = itemView.findViewById(R.id.tvDokterKonsultasi);
            waktu = itemView.findViewById(R.id.tvWaktuKonsultasi);
            deleteBtn = itemView.findViewById(R.id.btnDeleteKonsultasi);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}