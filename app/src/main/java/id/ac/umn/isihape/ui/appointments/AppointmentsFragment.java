package id.ac.umn.isihape.ui.appointments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.ac.umn.isihape.R;
import id.ac.umn.isihape.databinding.FragmentHomeBinding;

public class AppointmentsFragment extends Fragment {

    private AppointmentsViewModel appointmentsViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appointmentsViewModel =
                new ViewModelProvider(this).get(AppointmentsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_appointments, container, false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}