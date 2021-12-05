package id.ac.umn.isihape.ui.doctors;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ac.umn.isihape.BuatJanji;
import id.ac.umn.isihape.R;
import id.ac.umn.isihape.ui.home.HomeFragment;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class DoctorsFragment extends Fragment {

    private DoctorsViewModel doctorsViewModel;


    private RecyclerView rvDokterList;

    private TextView tvNamaPasien, tvNomorPasien;
    private CircleImageView fotoUser;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference doctorRef;
    private DatabaseReference getUserRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doctorsViewModel =
                new ViewModelProvider(this).get(DoctorsViewModel.class);

        View root = inflater.inflate(R.layout.doctors_slideshow, container, false);

        rvDokterList = (RecyclerView) root.findViewById(R.id.rvDokterList);
        rvDokterList.setLayoutManager(new LinearLayoutManager(getContext()));
        tvNamaPasien = (TextView) root.findViewById(R.id.namaPasien);
        tvNomorPasien = (TextView) root.findViewById(R.id.nomorPasien);
        fotoUser = (CircleImageView) root.findViewById(R.id.userImage);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        doctorRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        //Check user Type
        getUserRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users").getRef();
        Log.d("check", getUserRef.child(currentUserID).toString());
        getUserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String namaPasien = snapshot.child("name").getValue().toString();

                String nomorPasien = "0000-0000-0000";
                if (snapshot.child("notelp").exists()) {
                    nomorPasien = snapshot.child("notelp").getValue().toString();
                }

                if (snapshot.child("image").getValue() != null) {
                    String retrieveImage = snapshot.child("image").getValue().toString();
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
        storage = FirebaseStorage.getInstance();
        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(doctorRef.orderByChild("userType").equalTo("Dokter"), Doctors.class)
                        .build();
        FirebaseRecyclerAdapter<Doctors, DoctorsFragment.DoctorViewHolder> adapter =
                new FirebaseRecyclerAdapter<Doctors, DoctorsFragment.DoctorViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DoctorsFragment.DoctorViewHolder doctorViewHolder, int i, @NonNull Doctors doctors) {
                        final String list_user_id = getRef(i).getKey();
                        DatabaseReference getDoctorsRef = getRef(i).getRef();
                        getUserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String retrieveType = snapshot.child("userType").getValue().toString();
                                Log.d("check", retrieveType);
                                if(retrieveType.equalsIgnoreCase("Dokter")){
                                    doctorViewHolder.buatJanji.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        getDoctorsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() && snapshot.child("userType").getValue().toString().equals("Dokter")) {
                                    Log.d("test", snapshot.child("name").getValue().toString());

                                    String retrieveNama = snapshot.child("name").getValue().toString();
                                    String retrieveSpesialis = snapshot.child("spesialis").getValue().toString();
                                    Log.d("add", "tes1");
                                    String retrieveHarga = snapshot.child("harga").getValue().toString();
                                    String retrieveAlamat = snapshot.child("alamat").getValue().toString();

                                    String retrieveImage = snapshot.child("image").getValue().toString();

                                    if(snapshot.child("image").getValue() != null){
                                        Log.d("tag", retrieveImage);
                                        StorageReference httpsReference = storage.getReferenceFromUrl(retrieveImage);
                                        httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.d("tag", uri.toString());
                                                Picasso.get().load(uri.toString()).transform(new CropCircleTransformation()).into(doctorViewHolder.fotoDokter);
                                            }
                                        });
                                    }
                                    Log.d("fotourl", fotoUser.toString());

                                    doctorViewHolder.nama.setText(retrieveNama);
                                    doctorViewHolder.spesialis.setText(retrieveSpesialis);
                                    doctorViewHolder.harga.setText("Rp. " + retrieveHarga);
                                    doctorViewHolder.alamat.setText(retrieveAlamat);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        doctorViewHolder.buatJanji.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent buatJanjiIntent = new Intent(getContext(), BuatJanji.class);
                                DatabaseReference getDoctorsRef = getRef(doctorViewHolder.getLayoutPosition()).getRef();

                                getDoctorsRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String retrieveNama = snapshot.child("name").getValue().toString();
                                            String retrieveSpesialis = snapshot.child("spesialis").getValue().toString();
                                            String retrieveHarga = snapshot.child("harga").getValue().toString();
                                            String retrieveAlamat = snapshot.child("alamat").getValue().toString();
                                            String retrieveId = snapshot.getKey().toString();
                                            Log.d("iddoctor", retrieveId);

                                            buatJanjiIntent.putExtra("idstaff", retrieveId);
                                            buatJanjiIntent.putExtra("nama", retrieveNama);
                                            buatJanjiIntent.putExtra("spesialis", retrieveSpesialis);
                                            buatJanjiIntent.putExtra("harga", retrieveHarga);
                                            buatJanjiIntent.putExtra("alamat", retrieveAlamat);
                                            buatJanjiIntent.putExtra("type", "appointment");
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
        TextView nama, spesialis, alamat, harga;
        CircleImageView fotoDokter;
        Button buatJanji;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.tvNamaDokter);
            spesialis = itemView.findViewById(R.id.tvSpesialisasiDokter);
            alamat = itemView.findViewById(R.id.tvAdditionalDesc);
            harga = itemView.findViewById(R.id.tvHargaDokter);
            buatJanji = itemView.findViewById(R.id.btnBuatJanji);
            fotoDokter = itemView.findViewById(R.id.gambarDokter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}