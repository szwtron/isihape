//package id.ac.umn.isihape.ui.home;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.LinkedList;
//
//import id.ac.umn.isihape.R;
//
//public class DaftarJadwalAdapter extends RecyclerView<DaftarJadwalAdapter.ItemJadwalViewHolder> {
//    private LinkedList<SumberJadwal> mDaftarJadwal;
//    private LayoutInflater mInflater;
//    private Context mContext;
//
//    public DaftarJadwalAdapter(Context context, LinkedList<SumberJadwal> daftarJadwal) {
//        this.mContext = context;
//        this.mDaftarJadwal = daftarJadwal;
//        this.mInflater = LayoutInflater.from(context);
//    }
//
//    @NonNull
//    @Override
//    public ItemJadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.jadwal_konsultasi_layout, parent, false);
//        return new ItemJadwalViewHolder(view, this);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ItemJadwalViewHolder holder, int position) {
//        SumberJadwal mSumberJadwal = mDaftarJadwal.get(position);
//
//        holder.tvTanggal.setText(mSumberJadwal.getTanggal());
//        holder.tvDokter.setText(mSumberJadwal.getDokter());
//        holder.tvWaktu.setText(mSumberJadwal.getWaktu());
//
//        holder.btnDelete.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                mDaftarJadwal.remove(mSumberJadwal);
//                notifyDataSetChanged();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() { return mDaftarJadwal.size(); }
//
//    public class ItemJadwalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView tvTanggal;
//        private TextView tvDokter;
//        private TextView tvWaktu;
//        private ImageButton btnDelete;
//        private DaftarJadwalAdapter mJadwalAdapter;
//        private int mPosisi;
//        private SumberJadwal mSumberJadwal;
//
//        public ItemJadwalViewHolder(@NonNull View itemView, DaftarJadwalAdapter adapter) {
//            super(itemView);
//            mJadwalAdapter = adapter;
//            tvTanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
//            tvDokter = (TextView) itemView.findViewById(R.id.tvDokter);
//            tvWaktu = (TextView) itemView.findViewById(R.id.tvWaktu);
//            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            mPosisi = getLayoutPosition();
//            mJadwalAdapter = mJadwalAdapter.get(mPosisi);
//
//        }
//
//    }
//}
