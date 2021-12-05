package id.ac.umn.isihape.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.TambahJadwalKonsultasi;

public class HomeFragment extends Fragment {

    private RecyclerView rvJadwalKonsultasi;
    private TextView tvNamaHome;

    //edit
    //private Button btnTambahJadwal;

    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference jadwalKonsultasiRef;
    private DatabaseReference usersRef;

    private TextView actionDokter;
    private String currentUserID;
    public String usertype;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvJadwalKonsultasi = (RecyclerView) root.findViewById(R.id.rvJadwalKonsultasi);
        rvJadwalKonsultasi.setLayoutManager(new LinearLayoutManager(getContext()));
        tvNamaHome = (TextView) root.findViewById(R.id.text_home);

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
       // btnTambahJadwal = (Button) root.findViewById(R.id.tambahJadwalKonsultasi);
//        btnTambahJadwal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent tambahJadwalIntent = new Intent(getActivity(), TambahJadwalKonsultasi.class);
//                startActivity(tambahJadwalIntent);
//            }
//        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        usertype = sharedPreferences.getString("userType", "idpasien");
        Log.d("shared", usertype);
        actionDokter = root.findViewById(R.id.actionDokter);
        if(usertype.equalsIgnoreCase("idpasien")){
            actionDokter.setAlpha(0.0f);
        }

        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String namaPasien = snapshot.child("name").getValue().toString();
                tvNamaHome.setText("Selamat Datang, " + namaPasien);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                                builder.setTitle("Anda yakin untuk menolak permintaan ini?");
                                builder.setMessage("Aksi ini tidak dapat dirubah lagi");
                                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        HashMap<String, Object> tolakAppointment = new HashMap<String, Object>();
                                        tolakAppointment.put("status", "ditolak");
                                        getJadwalRef.updateChildren(tolakAppointment).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Appointment ditolak", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                                builder.show();
                            }
                        });

                        jadwalKonsultasiViewHolder.approveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Alert dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setCancelable(true);
                                builder.setTitle("Anda yakin untuk menerima permintaan ini?");
                                builder.setMessage("Aksi ini tidak dapat dirubah lagi");
                                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        HashMap<String, Object> terimaAppointment = new HashMap<>();
                                        terimaAppointment.put("status", "diterima");
                                        getJadwalRef.updateChildren(terimaAppointment).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Appointment diterima", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                                builder.show();
                            }
                        });

                        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String namaPasien = snapshot.child("name").getValue().toString();
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
                                tvNamaHome.setText("Selamat Datang, " + namaPasien);
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

                                        if (snapshot.child("status").getValue().toString().equals("ditolak")) {
                                            jadwalKonsultasiViewHolder.dokter.setTextColor(Color.RED);
                                            jadwalKonsultasiViewHolder.waktu.setTextColor(Color.RED);
                                            jadwalKonsultasiViewHolder.tanggal.setTextColor(Color.RED);
                                        }
                                        if (snapshot.child("status").getValue().toString().equals("diterima")) {
                                            jadwalKonsultasiViewHolder.dokter.setTextColor(Color.parseColor("#10ad09"));
                                            jadwalKonsultasiViewHolder.waktu.setTextColor(Color.parseColor("#10ad09"));
                                            jadwalKonsultasiViewHolder.tanggal.setTextColor(Color.parseColor("#10ad09"));
                                        }
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

                                        if (!snapshot.child("status").getValue().toString().equals("diproses")) {
                                            jadwalKonsultasiViewHolder.approveBtn.setEnabled(false);
                                            jadwalKonsultasiViewHolder.approveBtn.setClickable(false);
                                            jadwalKonsultasiViewHolder.approveBtn.setAlpha(0.0f);
                                            jadwalKonsultasiViewHolder.deleteBtn.setEnabled(false);
                                            jadwalKonsultasiViewHolder.deleteBtn.setClickable(false);
                                            jadwalKonsultasiViewHolder.deleteBtn.setAlpha(0.0f);
                                        }
                                        if (snapshot.child("status").getValue().toString().equals("ditolak")) {
                                            jadwalKonsultasiViewHolder.dokter.setTextColor(Color.RED);
                                            jadwalKonsultasiViewHolder.waktu.setTextColor(Color.RED);
                                            jadwalKonsultasiViewHolder.tanggal.setTextColor(Color.RED);
                                        }
                                        if (snapshot.child("status").getValue().toString().equals("diterima")) {
                                            jadwalKonsultasiViewHolder.dokter.setTextColor(Color.parseColor("#10ad09"));
                                            jadwalKonsultasiViewHolder.waktu.setTextColor(Color.parseColor("#10ad09"));
                                            jadwalKonsultasiViewHolder.tanggal.setTextColor(Color.parseColor("#10ad09"));
                                        }

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