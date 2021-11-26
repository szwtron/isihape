package id.ac.umn.isihape.ui.doctors;

import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import id.ac.umn.isihape.MainActivity;
import id.ac.umn.isihape.R;
import id.ac.umn.isihape.TambahDokter;
import id.ac.umn.isihape.databinding.FragmentHomeBinding;
import id.ac.umn.isihape.ui.home.HomeFragment;


public class DoctorsFragment extends Fragment {

    private DoctorsViewModel doctorsViewModel;
    private FragmentHomeBinding binding;

    private FloatingActionButton btnTambahDokter;

    private EditText etNamaDokter, etSpesialisDokter, etAlamatDokter;
    private Button tambahDokter;
    private RecyclerView rvDokterList;

    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference doctorRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doctorsViewModel =
                new ViewModelProvider(this).get(DoctorsViewModel.class);

        View root = inflater.inflate(R.layout.doctors_slideshow, container, false);

        rvDokterList = (RecyclerView) root.findViewById(R.id.rvDokterList);
        rvDokterList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        doctorRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Dokter");

        btnTambahDokter = (FloatingActionButton) root.findViewById(R.id.fabTambahDokter);
        btnTambahDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambahDokterIntent = new Intent(getActivity(), TambahDokter.class);
                startActivity(tambahDokterIntent);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(doctorRef.child(currentUserID), Doctors.class)
                        .build();
        FirebaseRecyclerAdapter<Doctors, DoctorsFragment.DoctorViewHolder> adapter =
                new FirebaseRecyclerAdapter<Doctors, DoctorsFragment.DoctorViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DoctorsFragment.DoctorViewHolder doctorViewHolder, int i, @NonNull Doctors doctors) {
                        final String list_user_id = getRef(i).getKey();
                        DatabaseReference getDoctorsRef = getRef(i).getRef();

                        getDoctorsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if((snapshot.exists()) && snapshot.hasChild("nama") && (snapshot.hasChild("spesialis"))){
                                    String retrieveTanggal = snapshot.child("nama").getValue().toString();
                                    String retrieveDokter = snapshot.child("spesialis").getValue().toString();
                                    String retrieveWaktu = snapshot.child("alamat").getValue().toString();

                                    doctorViewHolder.nama.setText(retrieveTanggal);
                                    doctorViewHolder.spesialis.setText(retrieveDokter);
                                    doctorViewHolder.alamat.setText(retrieveWaktu);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public DoctorsFragment.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dokter_layout, parent, false);
                        DoctorsFragment.DoctorViewHolder holder = new DoctorsFragment.DoctorViewHolder(view);
                        return holder;
                    }
                };
        rvDokterList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder{
        TextView nama, spesialis, alamat;
        Button buatJanji;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.tvNamaDokter);
            spesialis = itemView.findViewById(R.id.tvSpesialisasiDokter);
            alamat = itemView.findViewById(R.id.tvAdditionalDesc);
            buatJanji = itemView.findViewById(R.id.btnBuatJanji);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}