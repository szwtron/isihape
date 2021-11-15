package id.ac.umn.isihape.ui.doctors;

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


public class DoctorsFragment extends Fragment {

    private DoctorsViewModel doctorsViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doctorsViewModel =
                new ViewModelProvider(this).get(DoctorsViewModel.class);

        View root = inflater.inflate(R.layout.doctors_slideshow, container, false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}