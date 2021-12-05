package id.ac.umn.isihape.admin.ui.LiveAntrian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.umn.isihape.R;

public class LiveAntrianFragment extends Fragment {
    private RecyclerView rvAntrian;

    private FirebaseAuth mAuth;
    private DatabaseReference antrianRef;
    private DatabaseReference usersRef;

    private String currentUserID;
    private int nomorAntrian;
    private int nmrAntrian;
    private String retrieveUid;
    private String retrieveNama;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crud_antrian, container, false);

        rvAntrian = (RecyclerView) root.findViewById(R.id.rvCRUDAntrian);
        rvAntrian.setLayoutManager(new LinearLayoutManager(getContext()));

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
        FirebaseRecyclerAdapter<Antrian, LiveAntrianFragment.LiveAntrianViewHolder> adapter =
                new FirebaseRecyclerAdapter<Antrian, LiveAntrianFragment.LiveAntrianViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull LiveAntrianFragment.LiveAntrianViewHolder liveAntrianViewHolder, int i, @NonNull Antrian antrian) {
                        final String list_user_id = getRef(i).getKey();
                        nmrAntrian = 1;

                        DatabaseReference getAntrianRef = getRef(i).getRef();

                        getAntrianRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists() && snapshot.child("uid").getValue().toString() != null){
                                    retrieveUid = snapshot.child("uid").getValue().toString();

                                    usersRef.child(retrieveUid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            retrieveNama = snapshot.child("name").getValue().toString();
                                            Log.d("test", retrieveNama);
                                            liveAntrianViewHolder.nama.setText(retrieveNama);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    String retrieveTanggal = snapshot.child("tanggal").getValue().toString();
                                    String retrieveWaktu = snapshot.child("waktu").getValue().toString();


                                    liveAntrianViewHolder.tanggal.setText(retrieveTanggal);
                                    liveAntrianViewHolder.waktu.setText(retrieveWaktu);
                                    liveAntrianViewHolder.nomor.setText(String.valueOf(nmrAntrian));
                                }


                                nmrAntrian++;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        liveAntrianViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setCancelable(true);
                                builder.setTitle("Hapus antrian ini?");
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
                                        DatabaseReference getAntrianRef = getRef(liveAntrianViewHolder.getLayoutPosition()).getRef();
                                        getAntrianRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String id = getAntrianRef.getKey().toString();
                                                    getAntrianRef.removeValue();
                                                    Toast.makeText(getActivity(), "Antrian berhasil dihapus dari queue", Toast.LENGTH_LONG).show();
                                                    notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public LiveAntrianFragment.LiveAntrianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crud_antrian_layout, parent, false);
                        LiveAntrianFragment.LiveAntrianViewHolder holder = new LiveAntrianFragment.LiveAntrianViewHolder(view);
                        return holder;
                    }
                };
        rvAntrian.setAdapter(adapter);
        adapter.startListening();
    }

    public static class LiveAntrianViewHolder extends RecyclerView.ViewHolder{
        TextView tanggal, nama, waktu, nomor;
        ImageButton btnDelete;

        public LiveAntrianViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tvTanggalLiveAntrian);
            nama = itemView.findViewById(R.id.tvNamaLiveAntrian);
            waktu = itemView.findViewById(R.id.tvWaktuLiveAntrian);
            nomor = itemView.findViewById(R.id.tvNomorLiveAntrian);
            btnDelete = itemView.findViewById(R.id.btnDeleteLiveAntrian);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
