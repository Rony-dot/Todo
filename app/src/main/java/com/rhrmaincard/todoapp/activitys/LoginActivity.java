package com.rhrmaincard.todoapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.presenter.BasePresenter;
import com.rhrmaincard.todoapp.presenter.LoginPresenter;
import com.rhrmaincard.todoapp.presenterImpl.LoginPresenterImpl;
import com.rhrmaincard.todoapp.utils.CommonTask;
import com.facebook.FacebookSdk;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, BasePresenter.View  {

    private static final int RC_SIGN_IN = 999;
    EditText etUsername, etPassword;
    Button loginBtn, signUpBtn, freeTrial;// googleBtn;//, facebookBtn;
    LoginPresenter.Model loginPresenter;
    private CallbackManager callbackManager;
    LoginButton facebookBtn;
    boolean isLoggedIn;
    AccessToken accessToken;
    GoogleSignInClient mGoogleSignInClient;
    // Set the dimensions of the sign-in button.
    SignInButton googleBtn;
    LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter = new LoginPresenterImpl(this,this);
        googleBtn = findViewById(R.id.btn_google);
        googleBtn.setSize(SignInButton.SIZE_STANDARD);
        loginBtn = findViewById(R.id.login_btn);
        facebookBtn = (LoginButton) findViewById(R.id.btn_facebook);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        signUpBtn = findViewById(R.id.signup_btn);
        freeTrial = findViewById(R.id.free_trial);

        /**
         *  Setting data to username and password from sharedpreferences
         *  if they exists
         */

        if(!CommonTask.getDataFromSharedPreferences(this,"Username").isEmpty() &&
        !CommonTask.getDataFromSharedPreferences(this,"Password").isEmpty())
        {
            etUsername.setText(CommonTask.getDataFromSharedPreferences(this,"Username"));
            etPassword.setText(CommonTask.getDataFromSharedPreferences(this,"Password"));
        }


        /**
        * Google Auth Starts here
         **/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            onSuccess();
            CommonTask.printLog("google","google on success");
        }




        /**
         * Facebook auth starts here
         **/

        FacebookSdk.sdkInitialize(this);
//        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();





//        facebookBtn = findViewById(R.id.btn_facebook);
        googleBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
//        googleBtn.setOnClickListener(this);
        facebookBtn.setOnClickListener(this);
        freeTrial.setOnClickListener(this);



        // If you are using in a fragment, call loginButton.setFragment(this);

//        // Callback registration
//        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                loginResult.getAccessToken();
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            onSuccess();
            CommonTask.printLog("facebook","facebook on success");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                loginPresenter.connectWithRemoteServer(etUsername.getText().toString().trim(),
                        etPassword.getText().toString().trim());
                break;
            case R.id.signup_btn:
                finish();
                Intent intent = new Intent(this,RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_google:
//                signIn();
                loginPresenter.connectWithGoogle();
                break;
            case R.id.btn_facebook:
                loginManager.logInWithReadPermissions(
                        this,
                        Arrays.asList(
                                "email",
                                "public_profile",
                                "user_birthday",
                                "user_gender"));
//                loginManager.setReadPermissions(Arrays.asList("user_gender","user_birthday","email", "public_profile"));
//                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//                loginPresenter.connectWithFacebook();
                break;
            case R.id.free_trial:
                onSuccess();
                break;
        }
    }

    public void facebookLogin()
    {

        loginManager
                = LoginManager.getInstance();
        callbackManager
                = CallbackManager.Factory.create();

        // Callback registration
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                loginResult.getAccessToken();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse)
                    {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(object!=null){
                            CommonTask.printLog("Demo",object.toString());

                            Intent intent = new Intent(LoginActivity.this, NavMainActivity.class);
                            intent.putExtra("obj", object.toString());
                            startActivity(intent);
                        }

                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString("Feilds","birthday,email,gender,name,id,first_name,last_name");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            onSuccess();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
            CommonTask.showToast(this,"An error occured in google sign in");
        }
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressbar() {

    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(LoginActivity.this, NavMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(String... msg) {
        CommonTask.showToast(this,msg[0]);
        CommonTask.printLog("TAG",msg[0]);
    }


}