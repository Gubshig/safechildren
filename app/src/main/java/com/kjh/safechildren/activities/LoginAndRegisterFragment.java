package com.kjh.safechildren.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;
import com.kjh.safechildren.data.User_Firebase;

public class LoginAndRegisterFragment extends Fragment {
    public static FirebaseAuth mAuth;
    Button emailSignInButton;
    Button emailCreateAccountButton;
    Button signOutButton;
    Button verifyEmailButton;
    Button reloadButton;
    ProgressBar progressBar;
    EditText fieldEmail;
    EditText fieldPassword;
    TextView status, detail;
    Group emailPasswordButtons, emailPasswordFields, signedInButtons;
    Activity activity;
    private DatabaseReference mDatabase;
    TextView pleaselogintext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (ViewGroup) inflater.inflate(
                R.layout.loginandregister_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        activity = getActivity();
        ((Global)getContext().getApplicationContext()).setFirstActivity(this);
        pleaselogintext = (TextView)view.findViewById(R.id.pleaselogintext);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        setProgressBar(progressBar);
        emailSignInButton =  (Button)view.findViewById(R.id.emailSignInButton);
        emailCreateAccountButton =  (Button)view.findViewById(R.id.emailCreateAccountButton);
        signOutButton =  (Button)view.findViewById(R.id.signOutButton);
        verifyEmailButton =  (Button)view.findViewById(R.id.verifyEmailButton);
        reloadButton =  (Button)view.findViewById(R.id.reloadButton);

        fieldEmail =  (EditText)view.findViewById(R.id.fieldEmail);
        fieldPassword =  (EditText)view.findViewById(R.id.fieldPassword);

        status = (TextView)view.findViewById(R.id.status);
        detail = (TextView)view.findViewById(R.id.detail);

        emailPasswordButtons = (Group)view.findViewById(R.id.emailPasswordButtons);
        emailPasswordFields = (Group)view.findViewById(R.id.emailPasswordFields);
        signedInButtons = (Group)view.findViewById(R.id.signedInButtons);

        // Buttons
        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = fieldEmail.getText().toString();
                String password = fieldPassword.getText().toString();
                signIn(email, password);
            }
        });
        emailCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = fieldEmail.getText().toString();
                String password = fieldPassword.getText().toString();
                createAccount(email, password);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){

        }else{
            boolean bLogin = currentUser.isEmailVerified();
            Log.e("!!!!!", String.valueOf(currentUser.isEmailVerified()));
            updateUI(currentUser, bLogin);
        }

    }

    private void createAccount(String email, String password) {
        //Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, true);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String errorMsg = task.getException().getMessage();

                            Toast.makeText(getContext(), errorMsg,
                                    Toast.LENGTH_LONG).show();
                            updateUI(null, false);
                        }

                        hideProgressBar();
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user, true);
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null, false);
                        checkForMultiFactorFailure(task.getException());
                    }

                    if (!task.isSuccessful()) {
                        status.setText(R.string.auth_failed);
                    }
                    hideProgressBar();
                }
            });
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(null, false);

    }

    public void sendEmailVerification() {
        // Disable button
        verifyEmailButton.setEnabled(false);

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re-enable button
                        verifyEmailButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void reload() {
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateUI(mAuth.getCurrentUser(), true);
                    Toast.makeText(getContext(),
                            "Reload successful!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getContext(),
                            "Failed to reload user.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = fieldEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            fieldEmail.setError("Required.");
            valid = false;
        } else {
            fieldEmail.setError(null);
        }

        String password = fieldPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            fieldPassword.setError("Required.");
            valid = false;
        } else {
            fieldPassword.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user, boolean bLogin) {
        hideProgressBar();
        if (user != null) {
            Global.login  = true;
            status.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            emailPasswordButtons.setVisibility(View.GONE);
            emailPasswordFields.setVisibility(View.GONE);
            pleaselogintext.setVisibility(View.GONE);
            signedInButtons.setVisibility(View.VISIBLE);
            User_Firebase.getAllUserData(getContext(), getFragmentManager(), user, bLogin);

            if (user.isEmailVerified()) {
                verifyEmailButton.setVisibility(View.GONE);
            } else {
                verifyEmailButton.setVisibility(View.VISIBLE);
            }
        } else {
            status.setText(R.string.signed_out);
            detail.setText(null);
            Global.login = false;
            emailPasswordButtons.setVisibility(View.VISIBLE);
            emailPasswordFields.setVisibility(View.VISIBLE);
            signedInButtons.setVisibility(View.GONE);
            pleaselogintext.setVisibility(View.VISIBLE);


        }
        //if(bLogin){
            //get user from firebase


        //}
    }

    private void checkForMultiFactorFailure(Exception e) {
        // Multi-factor authentication with SMS is currently only available for
        // Google Cloud Identity Platform projects. For more information:
        // https://cloud.google.com/identity-platform/docs/android/mfa
        /*if (e instanceof FirebaseAuthMultiFactorException) {
            //Log.w(TAG, "multiFactorFailure", e);
            MultiFactorResolver resolver = ((FirebaseAuthMultiFactorException) e).getResolver();
            Bundle args = new Bundle();
            args.putParcelable(MultiFactorSignInFragment.EXTRA_MFA_RESOLVER, resolver);
            args.putBoolean(MultiFactorFragment.RESULT_NEEDS_MFA_SIGN_IN, true);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_emailpassword_to_mfa, args);
        }*/
    }

    public ProgressBar mProgressBar;

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}