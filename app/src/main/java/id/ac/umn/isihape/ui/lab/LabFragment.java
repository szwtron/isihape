package id.ac.umn.isihape.ui.lab;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ac.umn.isihape.BuatJanji;
import id.ac.umn.isihape.R;
import id.ac.umn.isihape.admin.ui.CRUDLab.TambahLab;
import id.ac.umn.isihape.databinding.FragmentHomeBinding;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class LabFragment extends Fragment {

    private LabViewModel labViewModel;
    private FragmentHomeBinding binding;

    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference labRef;
    private DatabaseReference getUserRef;

    private TextView tvNamaPasien, tvNomorPasien;
    private CircleImageView fotoUser;
    private StorageReference storageReference;
    private FirebaseStorage storage;


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

        tvNomorPasien = (TextView) root.findViewById(R.id.LabNomorUser);
        tvNamaPasien = (TextView) root.findViewById(R.id.LabNamaUser);
        fotoUser = (CircleImageView) root.findViewById(R.id.userImageLab);

        //Check user Type
        getUserRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users").getRef();
        Log.d("check", getUserRef.child(currentUserID).toString());
        storageReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        getUserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String retrieveType = snapshot.child("userType").getValue().toString();
                Log.d("check", retrieveType);
                String namaPasien = snapshot.child("name").getValue().toString();
                String retrieveImage = snapshot.child("image").getValue().toString();
                String nomorPasien = "0000-0000-0000";
                if (snapshot.child("notelp").exists()) {
                    nomorPasien = snapshot.child("notelp").getValue().toString();
                }
                if (snapshot.child("image").getValue() != null) {
                    Log.d("tag", retrieveImage);
                    StorageReference httpsReference = storage.getReferenceFromUrl(retrieveImage);
                    httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("tag", uri.toString());
                            Picasso.get().load(uri.toString()).transform(new CropCircleTransformation()).into(fotoUser);
                        }
                    });
                }
                Log.d("fotourl", fotoUser.toString());
                tvNamaPasien.setText(namaPasien);
                tvNomorPasien.setText(nomorPasien);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Labs> options =
                new FirebaseRecyclerOptions.Builder<Labs>()
                        .setQuery(labRef, Labs.class)
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
                                    labViewHolder.harga.setText("Rp. "+ retrieveHarga);
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
                                            String retrieveId = snapshot.getKey();

                                            buatJanjiIntent.putExtra("idstaff", retrieveId);
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
                        getUserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String retrieveType = snapshot.child("userType").getValue().toString();

                                if (retrieveType.equalsIgnoreCase("dokter")) {
                                    labViewHolder.buatJanji.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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
