package id.ac.umn.isihape.admin.ui.CRUDAppointment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.ui.home.SumberJadwal;

public class CRUDAppointmentFragment extends Fragment {

    private RecyclerView rvAppointmentList;
    private CRUDAppointmentViewModel CRUDAppointmentViewModel;
    private DatabaseReference appointmentRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CRUDAppointmentViewModel =
                new ViewModelProvider(this).get(CRUDAppointmentViewModel.class);


        View root = inflater.inflate(R.layout.fragment_crudappointment, container, false);

        rvAppointmentList = (RecyclerView) root.findViewById(R.id.rvAppointmentList);
        rvAppointmentList.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseApp.initializeApp(getActivity());
        appointmentRef = FirebaseDatabase.getInstance("https://" + "isihape-441d5-default-rtdb" + ".asia-southeast1." + "firebasedatabase.app").getReference().child("Jadwal");

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Appointments> options =
                new FirebaseRecyclerOptions.Builder<Appointments>()
                        .setQuery(appointmentRef, Appointments.class)
                        .build();
        FirebaseRecyclerAdapter<Appointments, CRUDAppointmentFragment.AppointmentViewHolder> adapter =
                new FirebaseRecyclerAdapter<Appointments, CRUDAppointmentFragment.AppointmentViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CRUDAppointmentFragment.AppointmentViewHolder appointmentViewHolder, int i, @NonNull Appointments appointments) {
                        DatabaseReference getAppointmentRef = getRef(i).getRef();

                        getAppointmentRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Log.d("Dokter", getAppointmentRef.getKey().toString());
                                    String id = getAppointmentRef.getKey().toString();

                                    DatabaseReference getIdRef = FirebaseDatabase.getInstance(
                                            "https://" + "isihape-441d5-default-rtdb" + ".asia-southeast1." + "firebasedatabase.app"
                                    ).getReference().child("Jadwal").child(id);

                                    getIdRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String retrieveTanggal = snapshot.child("tanggal").getValue().toString();
                                            String retrieveDokter = snapshot.child("dokter").getValue().toString();
                                            String retrieveId = snapshot.child("idpasien").getValue().toString();
                                            String retrieveType = snapshot.child("type").getValue().toString();
                                            String retrieveStatus = snapshot.child("status").getValue().toString();

                                            DatabaseReference getIdPasienRef = FirebaseDatabase.getInstance(
                                                    "https://" + "isihape-441d5-default-rtdb" + ".asia-southeast1." + "firebasedatabase.app"
                                            ).getReference().child("Users").child(retrieveId);

                                            getIdPasienRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String retrieveNama = snapshot.child("name").getValue().toString();
                                                    Log.d("Dokter6", retrieveNama);
                                                    appointmentViewHolder.nama.setText(retrieveNama);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

//                                            appointmentViewHolder.approvedBtn.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    HashMap<String, Object> updateAppointment = new HashMap<String, Object>();
//                                                    if (retrieveStatus.equalsIgnoreCase("diproses")) {
//                                                        updateAppointment.put("status", "diterima");
//                                                        appointmentViewHolder.approvedBtn.setEnabled(false);
//                                                        appointmentViewHolder.approvedBtn.setClickable(false);
//                                                    }
//                                                    getIdRef.updateChildren(updateAppointment).addOnCompleteListener(new OnCompleteListener() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task task) {
//                                                            if (task.isSuccessful()) {
//                                                                Toast.makeText(getActivity(), "Antrian Cek Laboratorium Terupdate", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
//                                                }
//                                            });

                                            appointmentViewHolder.approvedBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
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
                                                            HashMap<String, Object> updateAppointment = new HashMap<String, Object>();
                                                            if (retrieveStatus.equalsIgnoreCase("diproses")) {
                                                                updateAppointment.put("status", "diterima");
                                                                appointmentViewHolder.approvedBtn.setEnabled(false);
                                                                appointmentViewHolder.approvedBtn.setClickable(false);
                                                                appointmentViewHolder.approvedBtn.setAlpha(0.0f);
                                                                appointmentViewHolder.deleteBtn.setEnabled(false);
                                                                appointmentViewHolder.deleteBtn.setClickable(false);
                                                                appointmentViewHolder.deleteBtn.setAlpha(0.0f);
                                                            }
                                                            getIdRef.updateChildren(updateAppointment).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getActivity(), "Antrian Cek Laboratorium Terupdate", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                    builder.show();
                                                }
                                            });

                                            appointmentViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
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
                                                            getIdRef.updateChildren(tolakAppointment).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getActivity(), "Appointment ditolak", Toast.LENGTH_SHORT).show();
                                                                        appointmentViewHolder.deleteBtn.setEnabled(false);
                                                                        appointmentViewHolder.deleteBtn.setClickable(false);
                                                                        appointmentViewHolder.deleteBtn.setAlpha(0.0f);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                    builder.show();
                                                }
                                            });

                                            if (retrieveType.equalsIgnoreCase("appointment")) {
                                                appointmentViewHolder.approvedBtn.setEnabled(false);
                                                appointmentViewHolder.approvedBtn.setClickable(false);
                                                appointmentViewHolder.approvedBtn.setAlpha(0.0f);
                                                appointmentViewHolder.deleteBtn.setEnabled(false);
                                                appointmentViewHolder.deleteBtn.setClickable(false);
                                                appointmentViewHolder.deleteBtn.setAlpha(0.0f);
                                            } else {
                                                appointmentViewHolder.type.setText(retrieveType);
                                                appointmentViewHolder.typeHolder.setText("Laboratorium");
                                            }

                                            if (retrieveStatus.equalsIgnoreCase("diproses") ) {
                                                appointmentViewHolder.approvedBtn.setBackgroundColor(Color.YELLOW);
                                                appointmentViewHolder.nama.setTextColor(Color.GRAY);
                                                appointmentViewHolder.type.setTextColor(Color.GRAY);
                                                appointmentViewHolder.tanggal.setTextColor(Color.GRAY);
                                                appointmentViewHolder.status.setTextColor(Color.GRAY);
                                                if(retrieveType.equalsIgnoreCase("lab")) {
                                                    appointmentViewHolder.approvedBtn.setEnabled(true);
                                                    appointmentViewHolder.approvedBtn.setVisibility(View.VISIBLE);
                                                    appointmentViewHolder.approvedBtn.setClickable(true);
                                                    appointmentViewHolder.deleteBtn.setEnabled(true);
                                                    appointmentViewHolder.deleteBtn.setClickable(true);
                                                    appointmentViewHolder.deleteBtn.setAlpha(1.0f);
                                                    appointmentViewHolder.approvedBtn.setAlpha(1.0f);
                                                    appointmentViewHolder.deleteBtn.setVisibility(View.VISIBLE);
                                                }else{
                                                    appointmentViewHolder.approvedBtn.setEnabled(false);
                                                    appointmentViewHolder.approvedBtn.setClickable(false);
                                                    appointmentViewHolder.deleteBtn.setEnabled(false);
                                                    appointmentViewHolder.deleteBtn.setClickable(false);
                                                    appointmentViewHolder.deleteBtn.setAlpha(0.0f);
                                                    appointmentViewHolder.approvedBtn.setAlpha(0.0f);
                                                }

                                            } else if (retrieveStatus.equalsIgnoreCase("diterima")) {
                                                appointmentViewHolder.approvedBtn.setBackgroundColor(Color.parseColor("#10ad09"));
                                                appointmentViewHolder.approvedBtn.setEnabled(false);
                                                appointmentViewHolder.approvedBtn.setClickable(false);
                                                appointmentViewHolder.approvedBtn.setAlpha(0.0f);
                                                appointmentViewHolder.deleteBtn.setEnabled(false);
                                                appointmentViewHolder.deleteBtn.setClickable(false);
                                                appointmentViewHolder.deleteBtn.setAlpha(0.0f);

                                                appointmentViewHolder.nama.setTextColor(Color.parseColor("#10ad09"));
                                                appointmentViewHolder.type.setTextColor(Color.parseColor("#10ad09"));
                                                appointmentViewHolder.tanggal.setTextColor(Color.parseColor("#10ad09"));
                                                appointmentViewHolder.status.setTextColor(Color.parseColor("#10ad09"));
                                            } else if (retrieveStatus.equalsIgnoreCase("ditolak")) {
                                                appointmentViewHolder.approvedBtn.setEnabled(false);
                                                appointmentViewHolder.approvedBtn.setClickable(false);
                                                appointmentViewHolder.approvedBtn.setAlpha(0.0f);
                                                appointmentViewHolder.deleteBtn.setEnabled(false);
                                                appointmentViewHolder.deleteBtn.setClickable(false);
                                                appointmentViewHolder.deleteBtn.setAlpha(0.0f);

                                                appointmentViewHolder.nama.setTextColor(Color.RED);
                                                appointmentViewHolder.type.setTextColor(Color.RED);
                                                appointmentViewHolder.tanggal.setTextColor(Color.RED);
                                                appointmentViewHolder.status.setTextColor(Color.RED);
                                            }

                                            Log.d("Tanggal", retrieveTanggal);
                                            appointmentViewHolder.tanggal.setText(retrieveTanggal);
                                            appointmentViewHolder.type.setText(retrieveDokter);
                                            appointmentViewHolder.status.setText(retrieveStatus);

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

                    @NonNull
                    @Override
                    public CRUDAppointmentFragment.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentlist_layout, parent, false);
                        CRUDAppointmentFragment.AppointmentViewHolder holder = new CRUDAppointmentFragment.AppointmentViewHolder(view);
                        return holder;
                    }
                };
        rvAppointmentList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal, type, nama, typeHolder, status;
        ImageButton approvedBtn, deleteBtn;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.TanggalKonsultasi);
            type = itemView.findViewById(R.id.Type);
            nama = itemView.findViewById(R.id.Pasien);
            status = itemView.findViewById(R.id.Status);
            approvedBtn = itemView.findViewById(R.id.btnApproveKonsultasi);
            deleteBtn = itemView.findViewById(R.id.btnDeleteKonsultasi);
            typeHolder = itemView.findViewById(R.id.TypeHolder);
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