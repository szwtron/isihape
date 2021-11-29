package id.ac.umn.isihape.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.TambahJadwalKonsultasi;

public class HomeFragment extends Fragment {

    private RecyclerView rvJadwalKonsultasi;

    //edit
    private Button btnTambahJadwal;

    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference jadwalKonsultasiRef;
    private DatabaseReference usersRef;


    private String currentUserID;
    public String usertype;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvJadwalKonsultasi = (RecyclerView) root.findViewById(R.id.rvJadwalKonsultasi);
        rvJadwalKonsultasi.setLayoutManager(new LinearLayoutManager(getContext()));

        //firebase init
        FirebaseApp.initializeApp(getActivity());
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        jadwalKonsultasiRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Jadwal");
        usersRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");
        Log.d("login", usersRef.getKey());
        Log.d("login", String.valueOf(usersRef.getRef()));
        Log.d("login", String.valueOf(usersRef.getRoot()));
        Log.d("login", String.valueOf(usersRef.getClass()));
        Log.d("login", String.valueOf(usersRef.getParent()));


        //tambah jadwal
        btnTambahJadwal = (Button) root.findViewById(R.id.tambahJadwalKonsultasi);
        btnTambahJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambahJadwalIntent = new Intent(getActivity(), TambahJadwalKonsultasi.class);
                startActivity(tambahJadwalIntent);
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        usertype = sharedPreferences.getString("userType", "idpasien");
        Log.d("shared", usertype);
        return root;
    }


    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<SumberJadwal> options =
                new FirebaseRecyclerOptions.Builder<SumberJadwal>()
                .setQuery(jadwalKonsultasiRef.orderByChild(usertype).equalTo(currentUserID), SumberJadwal.class)
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

                        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // usertype = snapshot.child("userType").getValue().toString();
//                                    Log.d("usertype", usertype);

                                    if(usertype.equalsIgnoreCase("idpasien")){
                                        jadwalKonsultasiViewHolder.approveBtn.setEnabled(false);
                                        jadwalKonsultasiViewHolder.approveBtn.setClickable(false);
                                        jadwalKonsultasiViewHolder.approveBtn.setAlpha(0.0f);
                                        jadwalKonsultasiViewHolder.deleteBtn.setEnabled(false);
                                        jadwalKonsultasiViewHolder.deleteBtn.setClickable(false);
                                        jadwalKonsultasiViewHolder.deleteBtn.setAlpha(0.0f);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if(usertype.equalsIgnoreCase("idpasien")){
                            getJadwalRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()) {
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
                        } else {
                            getJadwalRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()) {
                                        String retrieveTanggal = snapshot.child("tanggal").getValue().toString();

                                        String retrieveWaktu = snapshot.child("waktu").getValue().toString();

                                        jadwalKonsultasiViewHolder.tanggal.setText(retrieveTanggal);
                                        jadwalKonsultasiViewHolder.waktu.setText(retrieveWaktu);

                                        usersRef.child(snapshot.child("idpasien").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String retrieveDokter = snapshot.child("name").getValue().toString();
                                                jadwalKonsultasiViewHolder.dokter.setText(retrieveDokter);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
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
        ImageButton deleteBtn, approveBtn;

        public JadwalKonsultasiViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tvTanggalKonsultasi);
            dokter = itemView.findViewById(R.id.tvDokterKonsultasi);
            waktu = itemView.findViewById(R.id.tvWaktuKonsultasi);
            deleteBtn = itemView.findViewById(R.id.btnDeleteKonsultasi);
            approveBtn = itemView.findViewById(R.id.btnApproveKonsultasi);
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