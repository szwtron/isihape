package id.ac.umn.isihape.ui.patients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.databinding.FragmentHomeBinding;

public class PatientsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patients, container, false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
