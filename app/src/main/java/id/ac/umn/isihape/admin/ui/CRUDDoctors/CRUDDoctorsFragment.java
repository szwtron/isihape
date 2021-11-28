package id.ac.umn.isihape.admin.ui.CRUDDoctors;

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

public class CRUDDoctorsFragment extends Fragment {

    private CRUDDoctorsViewModel dashboardViewModel;
    //private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(CRUDDoctorsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_cruddoctors, container, false);

        getActivity().setTitle("CRUD Doctors");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}