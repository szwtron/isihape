package id.ac.umn.isihape.admin.ui.CRUDDoctors;

import android.content.Intent;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.admin.ui.CRUDLab.CRUDLabFragment;
import id.ac.umn.isihape.admin.ui.CRUDLab.CRUDLabViewModel;
import id.ac.umn.isihape.admin.ui.CRUDLab.EditLab;
import id.ac.umn.isihape.admin.ui.CRUDLab.Labs;
import id.ac.umn.isihape.admin.ui.CRUDLab.TambahLab;

public class CRUDDoctorsFragment extends Fragment {

    private RecyclerView rvDokterList;
    private DatabaseReference docRef;
    private CRUDDoctorsViewModel CRUDDoctorsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CRUDDoctorsViewModel =
                new ViewModelProvider(this).get(CRUDDoctorsViewModel.class);


        View root = inflater.inflate(R.layout.fragment_cruddoctors, container, false);

        rvDokterList = (RecyclerView) root.findViewById(R.id.rvDokterList);
        rvDokterList.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseApp.initializeApp(getActivity());
        docRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("asd", "masuk2");

        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(docRef.orderByChild("userType").equalTo("Dokter"), Doctors.class)
                        .build();
        FirebaseRecyclerAdapter<Doctors, CRUDDoctorsFragment.DoctorsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Doctors, CRUDDoctorsFragment.DoctorsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CRUDDoctorsFragment.DoctorsViewHolder doctorsViewHolder, int i, @NonNull Doctors doctors) {

                        DatabaseReference getDocRef = getRef(i).getRef();
                        getDocRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String id = getDocRef.getKey().toString();
                                Log.d("DokterIdddd", id);

                                if(snapshot.exists()) {
                                    String retrieveNama= snapshot.child("name").getValue().toString();
                                    String retrieveHarga = snapshot.child("harga").getValue().toString();
                                    String retrieveNIK = snapshot.child("NIK").getValue().toString();
                                    String retrieveSpesialis = snapshot.child("spesialis").getValue().toString();

                                    Log.d("Dokternama", retrieveNama);
                                    Log.d("harga", retrieveHarga);
                                    Log.d("NIK", retrieveSpesialis);

                                    doctorsViewHolder.NIK.setText(retrieveNIK);
                                    doctorsViewHolder.name.setText(retrieveNama);
                                    doctorsViewHolder.harga.setText(retrieveHarga);
                                    doctorsViewHolder.spesialis.setText(retrieveSpesialis);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        doctorsViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference getDocRef = getRef(doctorsViewHolder.getLayoutPosition()).getRef();

                                getDocRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String id = getDocRef.getKey().toString();
                                            Log.d("dsa", String.valueOf(getDocRef.child(id)));
                                            getDocRef.removeValue();
                                            Log.d("berhasil", "berhasil");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                        doctorsViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference getDocRef = getRef(doctorsViewHolder.getLayoutPosition()).getRef();
                                getDocRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String id = getDocRef.getKey().toString();
                                            Log.d("id", id);
                                            Intent intent = new Intent(getActivity(), EditDoctors.class);
                                            String retrieveNama= snapshot.child("name").getValue().toString();
                                            String retrieveHarga = snapshot.child("harga").getValue().toString();
                                            String retrieveNik = snapshot.child("NIK").getValue().toString();
                                            String retrieveSpesialis = snapshot.child("spesialis").getValue().toString();

                                            intent.putExtra("id",id);
                                            intent.putExtra("name",retrieveNama);
                                            intent.putExtra("harga",retrieveHarga);
                                            intent.putExtra("NIK",retrieveNik);
                                            intent.putExtra("spesialis",retrieveSpesialis);
                                            Log.d("DokterNama", retrieveNama);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CRUDDoctorsFragment.DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctorlist_layout, parent, false);
                        CRUDDoctorsFragment.DoctorsViewHolder holder = new CRUDDoctorsFragment.DoctorsViewHolder(view);
                        return holder;
                    }
                };
        rvDokterList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder {
        TextView NIK, harga, name, spesialis;
        Button editButton, deleteBtn;

        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);

            NIK = itemView.findViewById(R.id.tvDokterNIK);
            harga = itemView.findViewById(R.id.tvDokterHarga);
            name = itemView.findViewById(R.id.tvDokterNama);
            spesialis = itemView.findViewById(R.id.tvDokterSpesialis);
            editButton = itemView.findViewById(R.id.editDoc);
            deleteBtn = itemView.findViewById(R.id.deleteDoc);
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