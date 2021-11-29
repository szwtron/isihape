package id.ac.umn.isihape.admin.ui.CRUDLab;

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
import androidx.fragment.app.FragmentTransaction;
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


public class CRUDLabFragment extends Fragment {

    private RecyclerView rvLabList;
    private id.ac.umn.isihape.admin.ui.CRUDLab.CRUDLabViewModel CRUDLabViewModel;
    private DatabaseReference labRef;
    private FloatingActionButton btnTambahLab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CRUDLabViewModel =
                new ViewModelProvider(this).get(CRUDLabViewModel.class);


        View root = inflater.inflate(R.layout.fragment_crudlab, container, false);
        Log.d("keluar","masuk activity");

        rvLabList = (RecyclerView) root.findViewById(R.id.rvLabList);
        rvLabList.setLayoutManager(new LinearLayoutManager(getContext()));
        btnTambahLab = (FloatingActionButton) root.findViewById(R.id.fabTambahLab);
        FirebaseApp.initializeApp(getActivity());
        labRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Lab");
        btnTambahLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahLab.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("asd", "masuk2");

        FirebaseRecyclerOptions<Labs> options =
                new FirebaseRecyclerOptions.Builder<Labs>()
                        .setQuery(labRef, Labs.class)
                        .build();
        FirebaseRecyclerAdapter<Labs, CRUDLabFragment.LabViewHolder> adapter =
                new FirebaseRecyclerAdapter<Labs, CRUDLabFragment.LabViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CRUDLabFragment.LabViewHolder labViewHolder, int i, @NonNull Labs labs) {

                        DatabaseReference getLabRef = getRef(i).getRef();
                        getLabRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()) {
                                    String id = getLabRef.getKey().toString();
                                    String retrieveNama= snapshot.child("nama").getValue().toString();
                                    String retrieveHarga = snapshot.child("harga").getValue().toString();
                                    String retrieveDeksripsi = snapshot.child("deskripsi").getValue().toString();
                                    Log.d("nama", retrieveNama);
                                    Log.d("harga", retrieveHarga);
                                    Log.d("deskripsi", retrieveDeksripsi);

                                    labViewHolder.nama.setText(retrieveNama);
                                    labViewHolder.harga.setText(retrieveHarga);
                                    labViewHolder.deskripsi.setText(retrieveDeksripsi);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        labViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference getLabRef = getRef(labViewHolder.getLayoutPosition()).getRef();

                                getLabRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String id = getLabRef.getKey().toString();
                                            Log.d("dsa", String.valueOf(getLabRef.child(id)));
                                            getLabRef.removeValue();
                                            Log.d("berhasil", "berhasil");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                        labViewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference getLabRef = getRef(labViewHolder.getLayoutPosition()).getRef();
                                getLabRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String id = getLabRef.getKey().toString();
                                            Log.d("id", id);
                                            Intent intent = new Intent(getActivity(), EditLab.class);
                                            String retrieveNama= snapshot.child("nama").getValue().toString();
                                            String retrieveHarga = snapshot.child("harga").getValue().toString();
                                            String retrieveDeskripsi = snapshot.child("deskripsi").getValue().toString();

                                            intent.putExtra("id",id);
                                            intent.putExtra("nama",retrieveNama);
                                            intent.putExtra("harga",retrieveHarga);
                                            intent.putExtra("deksripsi",retrieveDeskripsi);
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
                    public CRUDLabFragment.LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crud_lab_layout, parent, false);
                        CRUDLabFragment.LabViewHolder holder = new CRUDLabFragment.LabViewHolder(view);
                        return holder;
                    }
                };
        rvLabList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class LabViewHolder extends RecyclerView.ViewHolder{
        TextView nama, deskripsi, harga;
        Button deleteBtn,editBtn;

        public LabViewHolder(@NonNull View itemView) {
            super(itemView);

            deskripsi = itemView.findViewById(R.id.tvlabDesc);
            harga = itemView.findViewById(R.id.tvlabPrice);
            nama = itemView.findViewById(R.id.tvlabName);
            deleteBtn = itemView.findViewById(R.id.deleteLab);
            editBtn = itemView.findViewById(R.id.editLab);
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