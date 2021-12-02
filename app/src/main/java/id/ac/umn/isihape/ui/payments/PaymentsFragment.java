package id.ac.umn.isihape.ui.payments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import id.ac.umn.isihape.R;

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
