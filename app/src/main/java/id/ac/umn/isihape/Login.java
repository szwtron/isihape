package id.ac.umn.isihape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseUser currentUser;
    private Button loginBtn, phoneLoginBtn;
    private EditText userEmail, userPassword;
    private TextView needNewAccountLink, forgetPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        InitializeFields();

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
            }
        });
    }

    private void InitializeFields() {
        loginBtn = (Button) findViewById(R.id.login_button);
        phoneLoginBtn = (Button) findViewById(R.id.phone_login_button);
        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        needNewAccountLink = (TextView) findViewById(R.id.need_new_account_link);
        forgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser != null) {
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent(Login.this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(Login.this, RegisterPage.class);
        startActivity(registerIntent);
    }
}