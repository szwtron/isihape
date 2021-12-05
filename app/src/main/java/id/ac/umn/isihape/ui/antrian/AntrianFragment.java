package id.ac.umn.isihape.ui.antrian;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import id.ac.umn.isihape.R;

public class AntrianFragment extends Fragment {

    private RecyclerView rvAntrian;
    private TextView text_home;

    private FirebaseAuth mAuth;
    private DatabaseReference antrianRef;
    private DatabaseReference usersRef;

    private String currentUserID;
    private String retrieveUid;
    private String retrieveNama;
    private Button btnAntrian;
    private int nomorAntrian = 0;
    private int nmrAntrian;

    private static final long[] VIBRATE_PATTERN = { 500, 500 };
    private Vibrator mVibrator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_antrian, container, false);

        rvAntrian = (RecyclerView) root.findViewById(R.id.rvAntrian);
        rvAntrian.setLayoutManager(new LinearLayoutManager(getContext()));
        text_home = root.findViewById(R.id.text_home_antrian);

        btnAntrian = root.findViewById(R.id.nomorAntrian);
        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);

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
                                if(snapshot.exists() && snapshot.child("uid").getValue().toString() != null){
                                    retrieveUid = snapshot.child("uid").getValue().toString();

                                    //Check nomor antrian
                                    if(snapshot.child("uid").getValue().toString().equalsIgnoreCase(currentUserID)){
                                        nomorAntrian = nmrAntrian;
                                    }

                                    if(nomorAntrian == 0){
                                        btnAntrian.setText("Anda belum mengantri");
                                    } else {
                                        btnAntrian.setText(String.valueOf(nomorAntrian));
                                    }

                                    usersRef.child(retrieveUid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            retrieveNama = snapshot.child("name").getValue().toString();
                                            Log.d("test", retrieveNama);
                                            antrianViewHolder.nama.setText(retrieveNama);
                                            text_home.setText("Selamat Datang, " + retrieveNama);
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

                                }
                                nmrAntrian++;

                                if(nomorAntrian == 1){
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
                                    r.play();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        // API 26 and above
                                        mVibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN, 0));
                                    } else {
                                        // Below API 26
                                        mVibrator.vibrate(VIBRATE_PATTERN, 0);
                                    }

                                    //Alert dialog
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Giliran anda telah tiba!");
                                    builder.setMessage("Harap secepatnya pergi menuju reesepsionis!");

                                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            r.stop();
                                            mVibrator.cancel();
                                        }
                                    });
                                    builder.show();
                                }
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
