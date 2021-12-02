package id.ac.umn.isihape.ui.antrian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.ac.umn.isihape.R;

public class AntrianFragment extends Fragment {

    private RecyclerView rvAntrian;

    private FirebaseAuth mAuth;
    private DatabaseReference antrianRef;
    private DatabaseReference usersRef;

    private String currentUserID;
    private String retrieveUid;
    private String retrieveNama;
    private Button btnAntrian;
    private int nomorAntrianTemp = 1;
    private int nomorAntrian;
    private int nmrAntrian;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_antrian, container, false);

        rvAntrian = (RecyclerView) root.findViewById(R.id.rvAntrian);
        rvAntrian.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAntrian = root.findViewById(R.id.nomorAntrian);

        //firebase init
        FirebaseApp.initializeApp(getActivity());
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        antrianRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Antrian");
        usersRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference().child("Users");
        Log.d("login", usersRef.getKey());

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Antrian> options =
                new FirebaseRecyclerOptions.Builder<Antrian>()
                        .setQuery(antrianRef, Antrian.class)
                        .build();
        FirebaseRecyclerAdapter<Antrian, AntrianFragment.AntrianViewHolder> adapter =
                new FirebaseRecyclerAdapter<Antrian, AntrianFragment.AntrianViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AntrianFragment.AntrianViewHolder antrianViewHolder, int i, @NonNull Antrian antrian) {
                        final String list_user_id = getRef(i).getKey();
                        nmrAntrian = 1;

                        DatabaseReference getAntrianRef = getRef(i).getRef();

                        getAntrianRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                retrieveUid = snapshot.child("uid").getValue().toString();
                                Log.d("test", retrieveUid);

                                //Check nomor antrian
                                getAntrianRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists() && currentUserID.equalsIgnoreCase(retrieveUid)){
                                            Log.d("exist", snapshot.child("uid").getValue().toString());
                                            if(snapshot.child("uid").getValue().toString().equalsIgnoreCase(retrieveUid)){
                                                nomorAntrian = nomorAntrianTemp;
                                                btnAntrian.setText(String.valueOf(nomorAntrian));
                                            } else {
                                                nomorAntrianTemp++;
                                            }
                                        } else {
                                            btnAntrian.setText("Anda belum mengantri");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                usersRef.child(retrieveUid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        retrieveNama = snapshot.child("name").getValue().toString();
                                        Log.d("test", retrieveNama);
                                        antrianViewHolder.nama.setText(retrieveNama);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                String retrieveTanggal = snapshot.child("tanggal").getValue().toString();
                                String retrieveWaktu = snapshot.child("waktu").getValue().toString();


                                antrianViewHolder.tanggal.setText(retrieveTanggal);
                                antrianViewHolder.waktu.setText(retrieveWaktu);
                                antrianViewHolder.nomor.setText(String.valueOf(nmrAntrian));
                                nmrAntrian++;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AntrianFragment.AntrianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.antrian_layout, parent, false);
                        AntrianFragment.AntrianViewHolder holder = new AntrianFragment.AntrianViewHolder(view);
                        return holder;
                    }
                };
        rvAntrian.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AntrianViewHolder extends RecyclerView.ViewHolder{
        TextView tanggal, nama, waktu, nomor;

        public AntrianViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tvTanggalAntrian);
            nama = itemView.findViewById(R.id.tvNamaAntrian);
            waktu = itemView.findViewById(R.id.tvWaktuAntrian);
            nomor = itemView.findViewById(R.id.tvNomorAntrian);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
