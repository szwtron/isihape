package id.ac.umn.isihape.ui.payments;

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
import id.ac.umn.isihape.ui.patients.PatientsViewModel;

public class PaymentsFragment extends Fragment {
    private PaymentsViewModel paymentsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        paymentsViewModel =
                new ViewModelProvider(this).get(PaymentsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_payments, container, false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
