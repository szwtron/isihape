package id.ac.umn.isihape.ui.lab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.umn.isihape.BuatJanji;
import id.ac.umn.isihape.R;
import id.ac.umn.isihape.TambahLab;
import id.ac.umn.isihape.databinding.FragmentHomeBinding;
import id.ac.umn.isihape.ui.lab.Labs;

public class LabFragment extends Fragment {

    private LabViewModel labViewModel;
    private FragmentHomeBinding binding;

    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference labRef;

    private FloatingActionButton btnTambahLab;

    private RecyclerView rvLabList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        labViewModel =
                new ViewModelProvider(this).get(LabViewModel.class);

        View root = inflater.inflate(R.layout.fragment_lab, container, false);

        rvLabList = (RecyclerView) root.findViewById(R.id.rvLabList);
        rvLabList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        labRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Lab");

        btnTambahLab = (FloatingActionButton) root.findViewById(R.id.fabTambahLab);
        btnTambahLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambahLabIntent = new Intent(getActivity(), TambahLab.class);
                startActivity(tambahLabIntent);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Labs> options =
                new FirebaseRecyclerOptions.Builder<Labs>()
                        .setQuery(labRef.child(currentUserID), Labs.class)
                        .build();
        FirebaseRecyclerAdapter<Labs, LabViewHolder> adapter =
                new FirebaseRecyclerAdapter<Labs, LabViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull LabViewHolder labViewHolder, int i, @NonNull Labs labs) {
                        final String list_user_id = getRef(i).getKey();
                        DatabaseReference getLabRef = getRef(i).getRef();

                        getLabRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if((snapshot.exists()) && snapshot.hasChild("nama") && (snapshot.hasChild("deskripsi"))){
                                    String retrieveNama = snapshot.child("nama").getValue().toString();
                                    String retrieveDeskripsi = snapshot.child("deskripsi").getValue().toString();
                                    String retrieveHarga = snapshot.child("harga").getValue().toString();

                                    labViewHolder.nama.setText(retrieveNama);
                                    labViewHolder.deskripsi.setText(retrieveDeskripsi);
                                    labViewHolder.harga.setText(retrieveHarga);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        labViewHolder.buatJanji.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent buatJanjiIntent = new Intent(getContext(), BuatJanji.class);
                                DatabaseReference getLabRef = getRef(labViewHolder.getLayoutPosition()).getRef();

                                getLabRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String retrieveNama = snapshot.child("nama").getValue().toString();
                                            String retrieveSpesialis = snapshot.child("deskripsi").getValue().toString();
                                            String retrieveHarga = snapshot.child("harga").getValue().toString();

                                            buatJanjiIntent.putExtra("nama", retrieveNama);
                                            buatJanjiIntent.putExtra("spesialis", retrieveSpesialis);
                                            buatJanjiIntent.putExtra("harga", retrieveHarga);
                                            buatJanjiIntent.putExtra("alamat", "-");
                                            buatJanjiIntent.putExtra("type", "lab");
                                            startActivity(buatJanjiIntent);
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
                    public LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_layout, parent, false);
                        LabFragment.LabViewHolder holder = new LabFragment.LabViewHolder(view);
                        return holder;
                    }
                };
        rvLabList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class LabViewHolder extends RecyclerView.ViewHolder{
        TextView nama, deskripsi, harga;
        Button buatJanji;

        public LabViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.tvlabName);
            deskripsi = itemView.findViewById(R.id.tvlabDesc);
            harga = itemView.findViewById(R.id.tvlabPrice);
            buatJanji = itemView.findViewById(R.id.btnRequestLab);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
