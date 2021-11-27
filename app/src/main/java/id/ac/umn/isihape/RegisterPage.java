package id.ac.umn.isihape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterPage extends AppCompatActivity {

    private Button registerBtn;
    private EditText userEmail, userPassword;
    private TextView alreadyHaveAccountLink;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        mAuth = FirebaseAuth.getInstance();
        //RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef = FirebaseDatabase.getInstance("https://"+"isihape-441d5-default-rtdb"+".asia-southeast1."+"firebasedatabase.app").getReference();


        InitializeFields();

        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });
        
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email. . .", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password. . .", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("Please wait, while we are creating new account for you. . .");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("database", RootRef.getRoot().toString());
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        HashMap<String, String> usersMap = new HashMap<>();
                        usersMap.put("userType", userType);

                        RootRef.child("Users").child(currentUserID).setValue(usersMap);

                        SendUserToMainActivity();
                        Toast.makeText(RegisterPage.this, "Account created successfuly", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(RegisterPage.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }



    private void InitializeFields() {
        registerBtn = (Button) findViewById(R.id.register_button);
        userEmail = (EditText) findViewById(R.id.register_email);
        userPassword = (EditText) findViewById(R.id.register_password);
        alreadyHaveAccountLink = (TextView) findViewById(R.id.already_have_an_account_link);

        loadingBar = new ProgressDialog(this);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterPage.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterPage.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_normal:
                if (checked)
                    userType = "Normal";
                    break;
            case R.id.radio_dokter:
                if (checked)
                    userType = "Dokter";
                    break;
        }
    }
}

