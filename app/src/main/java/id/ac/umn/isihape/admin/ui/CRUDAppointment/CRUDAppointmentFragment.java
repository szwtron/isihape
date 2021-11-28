package id.ac.umn.isihape.admin.ui.CRUDAppointment;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        appointmentRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Jadwal");

        //getActivity().setTitle("CRUD Appointment");
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
                                if(snapshot.exists()) {
                                    Log.d("Dokter", getAppointmentRef.getKey().toString());
                                    String id = getAppointmentRef.getKey().toString();

                                    DatabaseReference getIdRef = FirebaseDatabase.getInstance(
                                            "https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app"
                                    ).getReference().child("Jadwal").child(id);

                                    getIdRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String retrieveTanggal = snapshot.child("tanggal").getValue().toString();
                                            String retrieveDokter = snapshot.child("dokter").getValue().toString();
                                            String retrieveId = snapshot.child("idpasien").getValue().toString();

                                            DatabaseReference getIdPasienRef = FirebaseDatabase.getInstance(
                                                    "https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app"
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

                                            Log.d("Tanggal", retrieveTanggal);
                                            appointmentViewHolder.tanggal.setText(retrieveTanggal);
                                            appointmentViewHolder.dokter.setText(retrieveDokter);

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

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView tanggal, dokter, nama;
        ImageButton deleteBtn;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.TanggalKonsultasi);
            dokter = itemView.findViewById(R.id.Dokter);
            nama = itemView.findViewById(R.id.Pasien);
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