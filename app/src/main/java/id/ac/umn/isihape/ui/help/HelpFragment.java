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

    private TextView tvchris1, tvchris2, tvevan1, tvevan2, tvjulius1, tvjulius2, tvstainley1, tvstainley2, tvtania1, tvtania2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.help, container, false);

        tvchris1 = root.findViewById(R.id.tvAdditionalDesc1);
        tvchris2 = root.findViewById(R.id.tvAdditionalDesc11);

        tvevan1 = root.findViewById(R.id.tvAdditionalDesc2);
        tvevan2 = root.findViewById(R.id.tvAdditionalDesc22);

        tvjulius1 = root.findViewById(R.id.tvAdditionalDesc3);
        tvjulius2 = root.findViewById(R.id.tvAdditionalDesc33);

        tvstainley1 = root.findViewById(R.id.tvAdditionalDescStainley);
        tvstainley2 = root.findViewById(R.id.tvAdditionalDescStainley2);

        tvtania1 = root.findViewById(R.id.tvAdditionalDesc4);
        tvtania2 = root.findViewById(R.id.tvAdditionalDesc44);

        Linkify.addLinks(tvchris1, Linkify.WEB_URLS);
        Linkify.addLinks(tvchris2, Linkify.WEB_URLS);

        Linkify.addLinks(tvevan1, Linkify.WEB_URLS);
        Linkify.addLinks(tvevan2, Linkify.WEB_URLS);

        Linkify.addLinks(tvjulius1, Linkify.WEB_URLS);
        Linkify.addLinks(tvjulius2, Linkify.WEB_URLS);

        Linkify.addLinks(tvstainley1, Linkify.WEB_URLS);
        Linkify.addLinks(tvstainley2, Linkify.WEB_URLS);

        Linkify.addLinks(tvtania1, Linkify.WEB_URLS);
        Linkify.addLinks(tvtania2, Linkify.WEB_URLS);

        return root;
    }
}
