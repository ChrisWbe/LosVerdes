package com.christianquintero.loginlosverdes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.InputStream;

import javax.xml.transform.URIResolver;

public class myLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private LoginButton buttonFacebook;
    private CallbackManager callbackManager;

    private SignInButton buttonGoogle;
    private GoogleApiClient mgoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    Menu registrar;

    private ImageView myPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        FacebookSdk.sdkInitialize(getApplicationContext());
        //maneja los resultados de intentos de inicio de sesion
        callbackManager=CallbackManager.Factory.create();
        setContentView(R.layout.activity_my_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        buttonFacebook=(LoginButton)findViewById(R.id.loginFacebookButton);
        myPhoto=(ImageView)findViewById(R.id.imagenLogin);

        //EN FACEBOOK
        buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                myPhoto.setVisibility(View.VISIBLE);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {

                            Profile profile = Profile.getCurrentProfile();
                            Picasso.with(myLogin.this).load(profile.getProfilePictureUri(200,200)).into(myPhoto);


                        }catch (Exception e){
                            Log.e("E-MainActivity", "getFaceBook" + e.toString());

                        }
                    }
                });
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login canceled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_LONG).show();

            }
        });



        //GMAIL
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mgoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        buttonGoogle=(SignInButton)findViewById(R.id.loginGoogleButton);
        buttonGoogle.setSize(SignInButton.SIZE_WIDE);
        buttonGoogle.setScopes(gso.getScopeArray());

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void handlesSignInResult(GoogleSignInResult result){
        Log.d(TAG, "handlesSignInResult:"+result.isSuccess());
        if(result.isSuccess()){
            myPhoto.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Inicio se sesion exitoso", Toast.LENGTH_LONG).show();
            GoogleSignInAccount acct = result.getSignInAccount();
            if(acct.getPhotoUrl() != null){
                Uri personalPhoto = acct.getPhotoUrl();
                new DownloadImageTask(myPhoto).execute(personalPhoto.toString());
            }else{
                Toast.makeText(this, "Sin imagen de perfil", Toast.LENGTH_LONG).show();
            }
            //finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlesSignInResult(result);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mgoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //esta clase descarga la imagen y la lleva a mi layout
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
