package com.rhrmaincard.todoapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.activitys.LoginActivity;
import com.rhrmaincard.todoapp.activitys.RegistrationActivity;
import com.rhrmaincard.todoapp.utils.CommonTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public class UserProfile extends Fragment implements View.OnClickListener {
    ImageView userPic;
    TextView userName, email;
    Button logOutBtn, edit, register;
    GoogleSignInClient mGoogleSignInClient;

    public UserProfile() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

        userName = view.findViewById(R.id.user_name);
        userPic = view.findViewById(R.id.user_pic);
        email = view.findViewById(R.id.user_email);
        logOutBtn = view.findViewById(R.id.logOut_btn);
        edit = view.findViewById(R.id.edit_profile);
        register = view.findViewById(R.id.register);

        edit.setOnClickListener(this);
        logOutBtn.setOnClickListener(this);
        register.setOnClickListener(this);

        /**
         * Getting data from sharedpreferencees  if the exists
         */
        if(!CommonTask.getDataFromSharedPreferences(requireContext(),"First_Name").isEmpty() &&
                !CommonTask.getDataFromSharedPreferences(requireContext(),"Last_Name").isEmpty() &&
                !CommonTask.getDataFromSharedPreferences(requireContext(),"Email").isEmpty())
        {
            userName.setText(CommonTask.getDataFromSharedPreferences(requireContext(),"First_Name").toString()
                    +" "+CommonTask.getDataFromSharedPreferences(requireContext(),"Last_Name").toString());
            email.setText(CommonTask.getDataFromSharedPreferences(requireContext(),"Email").toString());
            edit.setVisibility(View.VISIBLE);
        }

        /**
         *  Getting data from facebook sign in account if the exists
         */

        else if(getActivity().getIntent().getStringExtra("obj")!=null){
            try{
                JSONObject object = new JSONObject(getActivity().getIntent().getStringExtra("obj"));
                userName.setText(String.format(object.getString("name")));
                String id  = String.format(object.getString("id"));
                Picasso.get().load(String.valueOf("https://graph.facebook.com/" + id + "/picture?type=large"))
                        .into(userPic);

                edit.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                register.setVisibility(View.GONE);
                 }
            catch (Exception e){
                e.printStackTrace();
             }
        }
        else {
            edit.setVisibility(View.GONE);
            register.setVisibility(View.VISIBLE);
        }
        /**
        *  Getting data from google sign in account if the exists
        */

       if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            userName.setText(personName.toString());
            email.setText(personEmail.toString());
            Picasso.get().load(String.valueOf(personPhoto)).into(userPic);
            edit.setVisibility(View.GONE);
            register.setVisibility(View.GONE);
        }





    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logOut_btn:
                if( AccessToken.getCurrentAccessToken()!=null){
                    LoginManager.getInstance().logOut();
                }
                else{
                    signOut();
                }
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.edit_profile:
                NavHostFragment.findNavController(UserProfile.this)
                        .navigate(R.id.action_userProfile_to_editProfileFragment);
                break;

            case R.id.register:
                Intent regIntent = new Intent(getContext(), RegistrationActivity.class);
                startActivity(regIntent);
                break;
        }

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                LoginManager.getInstance().logOut();
            }
        }
    };


}