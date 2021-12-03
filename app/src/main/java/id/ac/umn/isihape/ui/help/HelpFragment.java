package id.ac.umn.isihape.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.ui.doctors.DoctorsViewModel;

public class HelpFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.help, container, false);




        return root;
    }
}
