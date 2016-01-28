package com.lamyatweng.mmugraduationstaff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Destroy MainActivity so that MainActivity is removed from back stack
        MainActivity.sMainActivity.finish();

        // Redirects to LoginActivity if user is not logged in
        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.wrapper_student_id);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.wrapper_login_password);
        final SessionManager sessionManager = new SessionManager(getApplicationContext());
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar_login);

        // Detecting Firebase Connection State
        Constants.FIREBASE_REF_CONNECTED_STAFF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    mIsConnected = true;
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    mIsConnected = false;
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = usernameWrapper.getEditText().getText().toString();

                String password = "";
                if (passwordWrapper.getEditText() != null)
                    password = passwordWrapper.getEditText().getText().toString();

                hideKeyboard();

                if (!validateEmail(email)) {
                    usernameWrapper.setError("Not a valid email address!");
                } else if (!validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    Constants.FIREBASE_REF_STAFF.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            sessionManager.createLoginSession(email);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            spinner.setVisibility(View.GONE);
                            if (mIsConnected)
                                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 0;
    }
}
