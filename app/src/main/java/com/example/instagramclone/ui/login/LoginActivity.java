package com.example.instagramclone.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagramclone.MainActivity;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.LoginViewModel;
import com.example.instagramclone.ui.login.LoginViewModelFactory;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = ".LoginAtivity";
    private EditText  etUsername;
    private EditText  etPassword;
    private Button  btnLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        if(ParseUser.getCurrentUser()!=null){
            goMainActivity();
        }


        etUsername = findViewById( R.id.etUsername );
        etPassword = findViewById( R.id.etPassword );
        btnLogin = findViewById( R.id.btnLogin );
        btnLogin.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.i( TAG, "onClick login button" );
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser( username, password );
            }
        } );
    }

    private void goMainActivity() {
    }

    private void loginUser(String username, String password) {
        Log.i( TAG,"Attempting to login user" + username);
        ParseUser.logInInBackground( username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Log.e(TAG,"Issue with login",e);
                    Toast.makeText( LoginActivity.this,"Issue with login!",Toast.LENGTH_SHORT ).show();
                    return;
                }
                goMainActivity();
                Toast.makeText( LoginActivity.this,"success",Toast.LENGTH_SHORT ).show();

            }

            private void goMainActivity() {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity( i );
                finish();
            }
        } );
    }

}
