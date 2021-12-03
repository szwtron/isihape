package id.ac.umn.isihape.ui.help;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.ui.doctors.DoctorsViewModel;

public class HelpFragment extends Fragment {

    private TextView tvchris1, tvchris2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.help, container, false);

        tvchris1 = root.findViewById(R.id.tvAdditionalDesc1);
        tvchris2 = root.findViewById(R.id.tvAdditionalDesc11);

        Linkify.addLinks(tvchris1, Linkify.WEB_URLS);
        Linkify.addLinks(tvchris2, Linkify.WEB_URLS);

        return root;
    }
}
