package id.ac.umn.isihape.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.ac.umn.isihape.LoginPage;
import id.ac.umn.isihape.R;



public class LogoutFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        currentUser = mAuth.getCurrentUser();
        View root = inflater.inflate(R.layout.doctors_slideshow, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(currentUser == null){
            SendUserToLoginActivity();
        }
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(getActivity(), LoginPage.class);
        startActivity(loginIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
