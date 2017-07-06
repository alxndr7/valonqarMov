package pe.separala.com.separalape2;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

import pe.separala.com.separalape2.model.DAOUsuario;
import pe.separala.com.separalape2.model.NegocioDBHelper;
import pe.separala.com.separalape2.utils.SessionManager;

public class Login extends Activity {
    private TextView info;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private boolean isLoggedIn = false;
    private String email,name;
    private NegocioDBHelper mDbHelper;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("user_friends");
        mDbHelper = new NegocioDBHelper(this);
        session = new SessionManager(getApplicationContext());

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        AccessToken accessToken = loginResult.getAccessToken();
                        Profile profile = Profile.getCurrentProfile();

                        if(loginResult.getAccessToken() != null) {
                            //Log.i(TAG, "Access Token:: " + loginResult.getAccessToken());
                            //facebookSuccess();
                            String fbId = profile.getId();
                            String fbAccessToken = loginResult.getAccessToken().getToken();

                            Intent i = new Intent(Login.this,ListarCanchasActivity.class);
                            startActivity(i);
                        }

                        Calendar fecha = Calendar.getInstance();

                        DAOUsuario usu = new DAOUsuario(
                                loginResult.getAccessToken().getUserId(),
                                profile.getName(),
                                loginResult.getAccessToken().getToken(),
                                "correo@correo.com",
                                fecha.get(Calendar.YEAR) + "-" + (fecha.get(Calendar.MONTH)+1) + "-" + fecha.get(Calendar.DATE)
                                );

                        mDbHelper.insertUsuario(usu);
                        onLoginSuccess(usu);

                        info.setText(
                                "User ID: "
                                        + loginResult.getAccessToken().getUserId()
                                        + "\n" +
                                        "Auth Token: "
                                        + loginResult.getAccessToken().getToken()

                        );
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void validarLoginFb(android.view.View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if(loggedIn){
            Intent i = new Intent(Login.this,ListarCanchasActivity.class);
            startActivity(i);
        }
    }

    public void onLoginSuccess(DAOUsuario usu) {

        session.createLoginSession(usu.getNombre_usuario(), usu.getFecha_update(),usu.getId_usuario());
        // Staring MainActivity
        Intent i = new Intent(getApplicationContext(), ListarCanchasActivity.class);
        startActivity(i);

        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;
/*
        String email = _usuario.getText().toString();
        String password = _password.getText().toString();

        if (email.isEmpty()) {
            _usuario.setError("Ingrese un usuario válido");
            valid = false;
        } else {
            _usuario.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _password.setError("Entre 4 y 10 caracteres alfanuméricos");
            valid = false;
        } else {
            _password.setError(null);
        }
*/
        return valid;
    }

}
