package pe.separala.com.separalape2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pe.separala.com.separalape2.model.DAOCancha;
import pe.separala.com.separalape2.model.DAONegocio;
import pe.separala.com.separalape2.model.DAOUsuario;
import pe.separala.com.separalape2.model.NegocioDBHelper;
import pe.separala.com.separalape2.utils.InternetConnection;
import pe.separala.com.separalape2.utils.SessionManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.content.ContentValues.TAG;
import static pe.separala.com.separalape2.Constantes.OBTENER_CANCHAS;

public class ListarCanchasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView list;
    GridAdapterComensales adapter;
    public  ListarCanchasActivity CustomListView = null;
    public boolean flagComensales;
    private DAONegocio[] jsonComensales;
    private ProgressDialog pDialog;
    private List<DAONegocio> allNegocios;
    private List<DAOUsuario> allUsuarios;
    private NegocioDBHelper mDbHelper;
    Context mContext;
    SessionManager session;
    ImageView avatarUsuario;
    TextView nomUsu;
    private DrawerLayout drawer;
    private static final int PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_canchas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = getApplicationContext();
        String ciudad="nn";
        if(checkPermission())
            ciudad = getCity();

        Log.i("PERMISOS", "CIUDAD : " + ciudad);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        mDbHelper = new NegocioDBHelper(this);

        allUsuarios = mDbHelper.getAllUsuarios();

        Log.i("LOGINUSUARIOS", "USUARIOS NUM : " + allUsuarios.size());

        CustomListView = this;
        list=(ListView)findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DAONegocio cancha =  allNegocios.get(position);
                Intent explicit_intent = new Intent(ListarCanchasActivity.this, AboutCanchaActivity.class);
                explicit_intent.putExtra("Cancha",cancha);
                ListarCanchasActivity.this.startActivity(explicit_intent);
                //Log.d(TAG, "Selected Note: " + selActa.getFch_inspeccion());
                //Instanciamos el DialogForNote
                //DialogForNote newAddNote = new DialogForNote();

                //Seteamos la nota que deseamos actualizar
                //newAddNote.setmNoteForUpd(selNote);

                //Mostramos el DialogForNote asignadole el tag "addnote"
                //newAddNote.show(getSupportFragmentManager(), "addnote");

            }
        });

        // Session class instance
        session = new SessionManager(getApplicationContext());

        session.checkLogin();
        // get user data from session

       HashMap<String, String> user = session.getUserDetails();
        Log.i("IDFB", user.get(SessionManager.ID_USUARIO));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        View hView =  navigationView.getHeaderView(0);
/*        avatarUsuario = (ImageView) hView.findViewById(R.id.imageUsuario);
        String url = "https://graph.facebook.com/" + user.get(SessionManager.ID_USUARIO) + "/picture?type=small";
        Glide.with(this).load(url).into(avatarUsuario);
*/
        ProfilePictureView profilePicture = (ProfilePictureView) hView.findViewById(R.id.friendProfilePicture);
        profilePicture.setProfileId(user.get(SessionManager.ID_USUARIO));
        nomUsu = (TextView) hView.findViewById(R.id.txtvNombreUsuario);
        nomUsu.setText("Hola " + user.get(SessionManager.KEY_NAME));

        toggle.syncState();



        //String url = "https://graph.facebook.com/" + user.get(SessionManager.ID_USUARIO) + "/picture?type=small";

        if (InternetConnection.checkConnection(mContext)) {
            new ListarCanchasActivity.GetComensales().execute();
        }
        else {
            allNegocios = mDbHelper.getAllNegocios();
            adapter=new GridAdapterComensales(CustomListView, allNegocios);
            list.setAdapter(adapter);
        }

        /*
        String url = "http://blog.on.com/wp-content/uploads/2014/01/profile-pic.jpg";
        Log.i("URLIMAGE1", url);

        Log.i("URLIMAGE2", url);
        Glide.with(this).load(url).into(avatarUsuario);
        Log.i("URLIMAGE3", url);*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            session.logoutUser();
            LoginManager.getInstance().logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){

                    }
                    else {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("Los permisos son requeridos",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ListarCanchasActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawer.closeDrawers();

                        return true;
                    }
                });
    }

    public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;
        try
        {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=small");
            Log.i("BITMAPIMAGE1", imageURL.toString());
        bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            Log.i("BITMAPIMAGE2", imageURL.toString());

        }
         catch(IOException ie) {
            ie.printStackTrace();
             Log.i("BITMAPIMAGE3",ie.toString());
        }

        return bitmap;
    }


    public void listarCanchasMaps(android.view.View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");

        Intent intent = new Intent(ListarCanchasActivity.this, MapsTodosActivity.class);
        startActivity(intent);
        //finish();
    }

    public String getCity(){

        String ciudad = "";

        if(checkPermission()) {

            //TO get the location,manifest file is added with 2 permissions
            //Location Manager is used to figure out which location provider needs to be used.
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //Best location provider is decided by the criteria
            Criteria criteria = new Criteria();
            //location manager will take the best location from the criteria
            locationManager.getBestProvider(criteria, true);

            //nce  you  know  the  name  of  the  LocationProvider,  you  can  call getLastKnownPosition() to  find  out  where  you  were  recently.
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            Log.d("Tag", "1");
            List<Address> addresses;

            try {
                addresses = gcd.getFromLocation(-15.490419, -70.129484, 1);
                if (addresses.size() > 0)

                {
                    //while(locTextView.getText().toString()=="Location") {
                    ciudad = addresses.get(0).getLocality().toString();
                    // }
                }

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        return ciudad;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }


    private class GetComensales extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ListarCanchasActivity.this);
            progressDialog.setMessage(getString(R.string.string_title_upload_progressbar_));
            progressDialog.show();
            allNegocios = mDbHelper.getAllNegocios();
            adapter=new GridAdapterComensales(CustomListView, allNegocios);
            list.setAdapter(adapter);

            Toast.makeText(ListarCanchasActivity.this,"Json Data is downloading" + allNegocios.size(), Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(ListarCanchasActivity.this,"CANCEL " + allNegocios.size(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = OBTENER_CANCHAS ;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray JsonCom = jsonObj.getJSONArray("canchas");
                    JSONArray JsonCanNeg = jsonObj.getJSONArray("canchas_negocio");

                    if(JsonCom.length() != 0 && JsonCanNeg.length() != 0){
                        jsonComensales = new DAONegocio[JsonCom.length()];
                        for (int i = 0; i < JsonCom.length(); i++) {

                            JSONObject c = JsonCom.getJSONObject(i);
                            if (!allNegocios.contains(c.getInt("n_cod_neg"))) {
                                jsonComensales[i] = new DAONegocio(
                                        c.getInt("n_cod_neg"),
                                        c.getString("c_raz_soc_neg"),
                                        c.getString("c_ruc_neg"),
                                        c.getString("c_tel_neg"),
                                        c.getString("c_dir_neg"),
                                        c.getInt("n_num_canchas"),
                                        c.getString("c_nom_rep_neg"),
                                        c.getString("c_tel_rep_neg"),
                                        c.getDouble("n_lat_neg"),
                                        c.getDouble("n_lon_neg"),
                                        c.getInt("n_comida_neg"),
                                        c.getInt("n_cochera_neg"),
                                        c.getDouble("n_prec_min"),
                                        c.getDouble("n_prec_max"),
                                        c.getString("t_hora_ini"),
                                        c.getString("t_hora_fin"),
                                        c.getInt("n_cod_dist"),
                                        c.getString("c_url_img"),
                                        c.getString("c_est_neg")
                                );

                                mDbHelper.insertNegocio(jsonComensales[i]);
                            }
                        }

                        flagComensales = true;

                        for (int i = 0; i < JsonCanNeg.length(); i++) {

                            JSONObject c = JsonCanNeg.getJSONObject(i);
                                DAOCancha cancha = new DAOCancha(
                                        c.getInt("n_cod_det_neg"),
                                        c.getInt("n_cod_neg"),
                                        c.getString("c_desc_cancha"),
                                        c.getDouble("n_largo_cancha"),
                                        c.getDouble("n_ancho_cancha"),
                                        c.getInt("n_num_jug_recomen"),
                                        c.getString("c_est_cancha"),
                                        c.getString("c_color_cancha")

                                );

                                mDbHelper.insertCancha(cancha);
                        }

                    }

                    else
                        flagComensales = false;


                } catch (final JSONException e) {
                    progressDialog.show();
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                flagComensales = false;
                progressDialog.show();
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if(!flagComensales){
                adapter=new GridAdapterComensales(CustomListView, allNegocios);
                list.setAdapter(adapter);
                Toast.makeText(getApplicationContext(),
                        "Usuario o contraseña incorrecta, intentelo nuevamente!", Toast.LENGTH_LONG)
                        .show();
            }
            else {
                allNegocios = mDbHelper.getAllNegocios();
                /**************** Create Custom Adapter *********/
                adapter=new GridAdapterComensales(CustomListView, allNegocios);
                list.setAdapter(adapter);
            }

        }

           /* super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Login.this, contactList,
                    R.layout.list_item, new String[]{ "email","mobile"},
                    new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);*/

    }



}