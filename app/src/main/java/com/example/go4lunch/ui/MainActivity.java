package com.example.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JeroSo94 on 02/02/2022.
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    // MVVM - Object declaration
    private MainViewModel mMainViewModel;

    /*
     * MainActivity VITAL SETUP
     */

    //MainActivity VITAL SETUP - Layout link
    @Override
    public ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    // MainActivity VITAL SETUP - LifeCycle behavior
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViewModel();

        startSignInActivity();
    }


    /*
     * SIGN IN PROCESS
     */

    // SIGN_IN_PROCESS_04--Run SignIn Activity
    private void startSignInActivity(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers =
                Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build()
                    ,new AuthUI.IdpConfig.FacebookBuilder().build()
                );
                /*
                new AuthUI.IdpConfig.GoogleBuilder().build()
                ,new AuthUI.IdpConfig.FacebookBuilder().build()
                ,new AuthUI.IdpConfig.EmailBuilder().build()
                ,new AuthUI.IdpConfig.TwitterBuilder().build()
                ,new AuthUI.IdpConfig.PhoneBuilder().build()
                );
                */

        /* TEST - Single authentification
        List<AuthUI.IdpConfig> providers =
                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
        */

        //Setup sign-in layout
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.signin)
                .setGoogleButtonId(R.id.signin_button_google)
                .setFacebookButtonId(R.id.signin_button_facebook)
                .build();

        // Setup sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.SignIn)
                .setAuthMethodPickerLayout(customLayout)
                .setIsSmartLockEnabled(false, true)
                .build();
        signInLauncher.launch(signInIntent);
    }

    // SIGN_IN_PROCESS_01--Setup signInLauncher
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    // SIGN_IN_PROCESS_02--Setup signInResult
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            mMainViewModel.createUser();
                // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            showSnackBar(getString(R.string.connection_succeed));
            finish();
            starthomeActivity();

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            // Sign in failed.
            // ERROR 1--response is null the user canceled the
            // sign-in flow using the back button.
            if (response == null) {
                // ERROR 1--Setup Snack Bar to display a user-friendly message about canceled authentication
                showSnackBar(getString(R.string.error_authentication_canceled));
            } else if (response.getError()!= null) {
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    // ERROR 2--Setup Snack Bar to display a user-friendly message about network issue
                    showSnackBar(getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    // ERROR 3--Setup Snack Bar to display a user-friendly message about unknown issue
                    showSnackBar(getString(R.string.error_unknown_error));
                }
            }
        }
    }

    private void starthomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void setupViewModel() {
        mMainViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);
    }

    // SIGN_IN_PROCESS_03--Show Snack Bar in mainLayout
    private void showSnackBar( String message){
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}
