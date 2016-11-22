package com.gppmds.tra.temremdioa.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.Toast;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.tra.gppmds.temremdioa.R;

public class SignUpActivity extends AppCompatActivity {

    public boolean getSpecialCharacter(String word) {
        final Pattern pattern = (Pattern) Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = (Matcher) pattern.matcher(word);

        return matcher.find();
    }

    public boolean isContainValid(String email) {
        return email.contains("@");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setValues();
        setListener();
    }

    /*Variables to User*/
    private static EditText mEmailView;
    private static EditText mPasswordView;
    private static EditText mPasswordViewConfirmation;
    private static EditText mAgeView;
    private static EditText mNameView;
    private static EditText mUsernameView;
    private static RadioButton mGenreMaleView;
    private static RadioButton mGenreFemView;
    private static TextView mGenre;
    private static Button mRegisterButton;
    private static Button cancelButton;

    private void setValues() {
        mNameView = (EditText) findViewById(R.id.name);
        mEmailView = (EditText) findViewById(R.id.email);
        mAgeView = (EditText) findViewById(R.id.ageText);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordViewConfirmation = (EditText) findViewById(R.id.password2);
        mGenre = (TextView) findViewById(R.id.textViewGenre);
        mGenreFemView = (RadioButton) findViewById(R.id.femButton);
        mGenreMaleView = (RadioButton) findViewById(R.id.mascButton);

        mRegisterButton = (Button) findViewById(R.id.register_button);
        cancelButton = (Button) findViewById(R.id.register_cancel);
    }

    private void setListener() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void attemptRegister() {
        // Reset errors.
        mNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordViewConfirmation.setError(null);
        mAgeView.setError(null);
        mGenreMaleView.setError(null);
        mGenreFemView.setError(null);
        mGenre.setError(null);
        mUsernameView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the login attempt.
        /*Password*/
        String password = mPasswordView.getText().toString();
        String passwordConfirmation = (String) mPasswordViewConfirmation.getText().toString();
        validatePassword(password, passwordConfirmation, cancel, focusView);

        /*Email*/
        String email = (String) mEmailView.getText().toString();
        validateEmail(email, cancel, focusView);

        /*Name*/
        String name = (String) mNameView.getText().toString();
        validateName(name, cancel, focusView);

        /*Age*/
        int age = 0;
        validateAge(age, cancel, focusView);

        /*Genre*/
        String genre = null;
        validateGenre(genre, cancel, focusView);

        /*Username*/
        String username = (String) mUsernameView.getText().toString();
        validateUsername(username, cancel, focusView);

        // When user press cancel button.
        if (cancel) {
            focusView.requestFocus();
        } else {
            mountUser(user, email, password, username, name, age, genre);
        }
    }

    private void validatePassword(String password, String passwordConfirmation, boolean cancel, View focusView){
        // conditions to validate the integrity of the password on register.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = (View) mPasswordView;
            cancel = true;
        } else if (password.length() < 6){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = (View) mPasswordView;
            cancel = true;
        } else if (!password.equals(passwordConfirmation)) {
            mPasswordViewConfirmation.setError(getString(R.string.error_different_password));
            focusView = (View) mPasswordViewConfirmation;
            cancel = true;
        }else {
            // Nothing to do.
        }
    }

    private void validateEmail(String email, boolean cancel, View focusView){
        // checking if the user's email is valid to register on database.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = (View) mEmailView;
            cancel = true;
        } else if (!isContainValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = (View) mEmailView;
            cancel = true;
        }else {
            // Nothing to do.
        }
    }

    private void validateName(String name, boolean cancel, View focusView){
        // checking if the user's name is valid to pursue a user registration.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = (View) mNameView;
            cancel = true;
        } else if (getSpecialCharacter(name)) {
            mNameView.setError(getString(R.string.error_character));
            focusView = (View) mNameView;
            cancel = true;
        }else {
            // Nothing to do.
        }
    }

    private void validateAge(int age, boolean cancel, View focusView){
        // conditions to validate the user's age to continue registration.
        if (TextUtils.isEmpty(mAgeView.getText().toString())) {
            mAgeView.setError(getString(R.string.error_field_required));
            focusView = (View) mAgeView;
            cancel = true;
        } else {
            age = Integer.parseInt(mAgeView.getText().toString());
            if (age < 0 || age > 100) {
                mAgeView.setError(getString(R.string.error_invalid_age));
            } else {
                // Nothing to do.
            }
        }
    }

    private void validateGenre(String genre, boolean cancel, View focusView){
        if (!mGenreMaleView.isChecked() && !mGenreFemView.isChecked()) {
            mGenre.setError(getString(R.string.error_invalid_genre));
            focusView = (View) mGenre;
            cancel = true;
        }
        else if (mGenreFemView.isChecked()){
            genre = (String) "Feminino";
        }
        else if (mGenreMaleView.isChecked()){
            genre = (String) "Masculino";
        } else {
            // Nothing to do.
        }
    }

    private void validateUsername(String username, boolean cancel, View focusView){
        // conditions to validate the user name and proceed to registration.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else {
            // Nothing to do.
        }
    }

    private static ParseUser user = new ParseUser();

    private void mountUser(ParseUser user, String email, String password, String username, String name, int age, String genre){
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.put("Name", name);
        user.put("Age" , age);
        user.put("Genre" , genre);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Cadastrado efetuado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro no cadastro, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        finish();
    }
}