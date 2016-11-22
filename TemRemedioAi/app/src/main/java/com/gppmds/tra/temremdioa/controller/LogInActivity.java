/**
 * File: LogInActivity.java
 * Purpose: this file set all information about user for login.
 */
package com.gppmds.tra.temremdioa.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.tra.gppmds.temremdioa.R;

public class LogInActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Method: onLoadFinished()
     * Purpose:
     * @param cursor
     * @param loader
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        finish();
    }

    /**
     * Method: onLoaderReset()
     * Purpose:
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        finish();
    }

    /**
     * Method: onCreateLoader()
     * Purpose:
     * return ou param
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    /**
     * Method: getCurrentUser()
     * Purpose:
     * @return currentUser
     */
    public ParseUser getCurrentUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();

        return currentUser;
    }

    /**
     * Method: setValues()
     * Purpose:
     * @return dont have return.
     */
    public void setValues() {
        mUsernameView = (EditText) findViewById(R.id.log_in_username_field);
        mPasswordView = (EditText) findViewById(R.id.log_in_password_field);

        mUsernameSignInButton = (Button) findViewById(R.id.log_in_button);
        mRegisterButton = (Button) findViewById(R.id.log_in_sign_in_button);
        info = (TextView)findViewById(R.id.info);
        mFacebookButton = (LoginButton) findViewById(R.id.login_button_fb);

        mLoginFormView = (View) findViewById(R.id.log_in_form);
        mProgressView = (ProgressBar) findViewById(R.id.log_in_progress_bar);
    }

    /**
     * Method: validateError()
     * Purpose:
     * return ou param
     */
    public boolean validateError(String word, String phrase, EditText text) {
        String phraseValited = TextUtils.isEmpty(word) ? phrase : null;
        text.setError(phraseValited);

        if(phraseValited != null)
            focusView = (EditText) text;
        else {
            focusView = null;
        }

        return TextUtils.isEmpty(word);
    }

    /**
     * Method: UserLoginTask()
     * Purpose:
     * return ou param
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = (String) username;
            mPassword = (String) password;
        }

        /**
         * Method: ()
         * Purpose:
         * return ou param
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            //for (String credential : DUMMY_CREDENTIALS) {
            //  String[] pieces = credential.split(":");
            //  if (pieces[0].equals(mEmail)) {
            //      Account exists, return true if the password matches.
            //      return pieces[1].equals(mPassword);
            //   }
            //}

            // TODO: register the new account here.
            return true;
        }

        /**
         * Method: onPostExecute()
         * Purpose:
         * return ou param
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        /**
         * Method: onCancelled()
         * Purpose:
         * return ou param
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Method: onActivityResult()
     * Purpose:
     * @return dont have return.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("data",data.toString());
    }

    //Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;
    private static TextView info;
    private static EditText mUsernameView;
    private static EditText mPasswordView;
    private static ProgressBar mProgressView;
    private static View mLoginFormView;
    private static View focusView = null;
    private static Button mUsernameSignInButton;
    private static Button mRegisterButton;
    private static LoginButton mFacebookButton;
    private final String errorMessageUsername = (String) "Ops! Campo de username esta vazio.";
    private final String errorMessagePassword = (String) "Ops! Campo do password esta vazio.";
    private CallbackManager callbackManager;

    /**
     * Method: onCreate()
     * Purpose: create the activity on the screen.
     * @return dont have return.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setValues();
        setListener();
    }

    /**
     * Method: facebookSDKInitialize()
     * Purpose:
     * @return dont have return.
     */
    protected void facebookSDKInitialize() {
        callbackManager = (CallbackManager) CallbackManager.Factory.create();
    }

    /**
     * Method: setListener()
     * Purpose:
     * @return dont have return.
     */
    private void setListener() {
        mFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" + "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                finish();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                } else {
                    // Nothing to do.
                }
                return false;
            }
        });

        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpActivity = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(signUpActivity);
            }
        });
    }

    /**
     * Method: attemptLogin()
     * Purpose:
     * @return dont have return.
     */

    // Attempts to login in the system if the entries email and password are valid
    private void attemptLogin(){
        // Store values at the time of the login attempt
        final String username = (String) mUsernameView.getText().toString();
        String password = (String) mPasswordView.getText().toString();

        if (mAuthTask != null){
            return;
        } else {
            // Nothing do to.
        }

        if ((validateError(username, errorMessageUsername, mUsernameView) ||
                validateError(password, errorMessagePassword, mPasswordView))) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser != null) {
                        Toast.makeText(getApplicationContext(), "Logado com sucesso, seja bem " +
                                "vindo " + username +"!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Nome de usuário e/ou senha não "
                                + "existente!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LogInActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    /**
     * Method: showProgress()
     * Purpose:
     * return ou param
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) { // Honeycombe api use to show progress bar

        mProgressView.getIndeterminateDrawable().setColorFilter(Color.RED,
                android.graphics.PorterDuff.Mode.SRC_IN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = (int) getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Method: ProfileQuery()
     * Purpose:
     * return ou param
     */
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        final int ADDRESS = 0;
        final int IS_PRIMARY = 1;
    }
}
