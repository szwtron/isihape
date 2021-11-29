package id.ac.umn.isihape.admin.ui.CRUDLab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CRUDLabViewModel =
                new ViewModelProvider(this).get(CRUDLabViewModel.class);


        View root = inflater.inflate(R.layout.fragment_crudlab, container, false);

        rvLabList = (RecyclerView) root.findViewById(R.id.rvLabList);
        rvLabList.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseApp.initializeApp(getActivity());
        labRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Lab");
        Log.d("asd", "masuk1");
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
        Log.d("asd", "masuk2");
        FirebaseRecyclerAdapter<Labs, CRUDLabFragment.LabViewHolder> adapter =
                new FirebaseRecyclerAdapter<Labs, CRUDLabFragment.LabViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CRUDLabFragment.LabViewHolder labViewHolder, int i, @NonNull Labs labs) {
                        Log.d("asd", "masuk3");

                        DatabaseReference getLabRef = getRef(i).getRef();
                        Log.d("asd", String.valueOf(getLabRef));
                        getLabRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.d("asd", "masuk4");

                                if(snapshot.exists()) {
                                    Log.d("asd", "masuk5");

                                    String id = getLabRef.getKey().toString();

                                    DatabaseReference getIdRef = FirebaseDatabase.getInstance(
                                            "https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app"
                                    ).getReference().child("Lab").child(id);

                                    getIdRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String retrieveNama= snapshot.child("nama").getValue().toString();
                                            String retrieveHarga = snapshot.child("harga").getValue().toString();
                                            String retrieveDeksripsi = snapshot.child("deskripsi").getValue().toString();

                                            labViewHolder.nama.setText(retrieveNama);
                                            labViewHolder.harga.setText(retrieveHarga);
                                            labViewHolder.deskripsi.setText(retrieveDeksripsi);
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
        ImageButton deleteBtn,editBtn;

        public LabViewHolder(@NonNull View itemView) {
            super(itemView);

            deskripsi = itemView.findViewById(R.id.tvlabName);
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