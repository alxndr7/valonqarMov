package pe.separala.com.separalape2;

/*import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;*/

/*premkumarnew80@gmail.com*/

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.separala.com.separalape2.model.DAONegocio;
import pe.separala.com.separalape2.model.NegocioDBHelper;

import static android.content.ContentValues.TAG;
import static pe.separala.com.separalape2.Constantes.OBTENER_CANCHAS_POR_DISTRITO;
import static pe.separala.com.separalape2.Constantes.OBTENER_UBICACIONES;

public class MapsTodosActivity extends FragmentActivity implements OnMapReadyCallback,OnInfoWindowClickListener,OnMarkerClickListener {
    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    private String ImageUrl = "https://s3.amazonaws.com/uifaces/faces/twitter/jsa/128.jpg";
    private LatLng point;
    private Spinner sItems;
    private ArrayAdapter<String> adapter;
    private int idDistTmp = 0;
    List<String> spinnerArray =  new ArrayList<String>();
    private int[] idsDistritos;
    private LinearLayout mLayoutTab;
    private boolean needsInit=false;
    private Bitmap smallMarker;
    private DAONegocio[] infoCanchas;
    private int c = 0;
    private boolean flagPorDist = false;
    private List<DAONegocio> allNegocios;
    private NegocioDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_todos);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDbHelper = new NegocioDBHelper(this);

        if (savedInstanceState == null) {
            needsInit=true;
        }
        // you need to have a list of data that you want the spinner to display
        sItems = (Spinner) findViewById(R.id.spDistrito);
        spinnerArray.add("Distrito");
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("SPINNER", "Posicion: " + position);
                idDistTmp = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int height = 70;
        int width = 50;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.markerseparala);
        Bitmap b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.i("ONCLICK" ,marker.getTitle() );
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        //Toast.makeText(this, "Posicion: " + arg0.getId() + " , " + infoCanchas[Integer.parseInt(arg0.getTitle())].getcNomNeg(), Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        MapsInitializer.initialize(this);
        //addCustomMarker();
        mMap.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
/*
        adapter = new ArrayAdapter<String>(MapsTodosActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);*/
        allNegocios = mDbHelper.getAllNegocios();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int i=0;i<allNegocios.size();i++){
            LatLng point = new LatLng(allNegocios.get(i).getN_lat_neg(), allNegocios.get(i).getN_lon_neg());
            addMarker(mMap, point.latitude, point.longitude,allNegocios.get(i).getC_raz_soc_neg(), R.string.practice_x3);
            builder.include(point);

        }

        //mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        //mMap.setOnInfoWindowClickListener(MapsTodosActivity.this);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);


        //new MapsTodosActivity.getUbicaciones().execute();

        // Let's add a couple of markers*/
/*
        if (needsInit) {
            CameraUpdate center=
                    CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044,
                            -73.98180484771729));
            CameraUpdate zoom= CameraUpdateFactory.zoomTo(15);

            map.moveCamera(center);
            map.animateCamera(zoom);
        }

        addMarker(map, 40.748963847316034, -73.96807193756104,
                R.string.un, R.string.united_nations);
        addMarker(map, 40.76866299974387, -73.98268461227417,
                R.string.lincoln_center,
                R.string.lincoln_center_snippet);
        addMarker(map, 40.765136435316755, -73.97989511489868,
                R.string.carnegie_hall, R.string.practice_x3);
        addMarker(map, 40.70686417491799, -74.01572942733765,
                R.string.downtown_club, R.string.heisman_trophy);

        map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);*/
    }
    public void canchasPorDistrito(View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");

        //new MapsTodosActivity.getCanchasPorDistrito().execute();
    }
    public void showHideOptions(View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");

        mLayoutTab = (LinearLayout)findViewById(R.id.llOpciones);

        if (mLayoutTab.getVisibility() == View.GONE) {
            mLayoutTab.setVisibility(View.VISIBLE);
            /*
            mLayoutTab.animate()
                    .translationY(mLayoutTab.getHeight()).alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            mLayoutTab.setVisibility(View.VISIBLE);
                            mLayoutTab.setAlpha(0.0f);
                        }
                    });*/
        } else {
          /*  mLayoutTab.animate()
                    .translationY(mLayoutTab.getHeight()).alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mLayoutTab.setVisibility(View.GONE);
                        }
                    });*/
            mLayoutTab.setVisibility(View.GONE);
        }
    }


    public void onItemSelected(AdapterView<?> parent,
                               View view, int pos, long id) {
        Toast.makeText(getApplicationContext(), "The planet is " +
                parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           String title, int snippet) {
        Log.i("ADDMARKER" , "constante: " + c);
        Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .snippet(getString(snippet))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        m.setTag("Prueba");
    }

    /*
    private class getUbicaciones extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MapsTodosActivity.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = OBTENER_UBICACIONES ;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray JsonCom = jsonObj.getJSONArray("ubicaciones");
                    infoCanchas = new DAONegocio[JsonCom.length()];
                    if(JsonCom.length() != 0){
                        //jsonComensales = new DAONegocio[JsonCom.length()];
                        latlngs = new ArrayList<>();
                        for (int i = 0; i < JsonCom.length(); i++) {
                            JSONObject c = JsonCom.getJSONObject(i);
                            latlngs.add(new LatLng(c.getDouble("fLatNeg"),c.getDouble("fLonNeg")));
                            infoCanchas[i] = new DAONegocio(
                                    c.getInt("nCodNeg"),
                                    c.getString("cNomNeg"),
                                    c.getString("cNumNeg"),
                                    c.getDouble("fPrecMin"),
                                    c.getDouble("fPrecMax"),
                                    c.getString("cUrlImg"),
                                    c.getString("tHoraIni"),
                                    c.getString("tHorafin"));

                        }

                    }
                    // Getting JSON Array node
                    JSONArray JsonDist = jsonObj.getJSONArray("distritos");
                    idsDistritos =  new int[JsonDist.length()];
                    if(JsonDist.length() != 0){
                        //jsonComensales = new DAONegocio[JsonCom.length()];
                        for (int i = 0; i < JsonDist.length(); i++) {
                            JSONObject c = JsonDist.getJSONObject(i);
                            spinnerArray.add(c.getString("cNomDist"));
                            idsDistritos[i] = c.getInt("nCodDist");
                        }
                    }



                } catch (final JSONException e) {
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

            adapter = new ArrayAdapter<String>(MapsTodosActivity.this, android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sItems.setAdapter(adapter);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (final LatLng point : latlngs) {

                addMarker(mMap, point.latitude, point.longitude,
                        R.string.carnegie_hall, R.string.practice_x3);

                builder.include(point);
            }
            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            mMap.setOnInfoWindowClickListener(MapsTodosActivity.this);
            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            mMap.animateCamera(cu);

        }
    }

    private class getCanchasPorDistrito extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MapsTodosActivity.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = OBTENER_CANCHAS_POR_DISTRITO  + "/" + idsDistritos[idDistTmp-1];
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray JsonCom = jsonObj.getJSONArray("ubicaciones");
                    infoCanchas = new DAONegocio[JsonCom.length()];
                    if(JsonCom.length() != 0){
                        //jsonComensales = new DAONegocio[JsonCom.length()];
                        latlngs = new ArrayList<>();
                        for (int i = 0; i < JsonCom.length(); i++) {
                            JSONObject c = JsonCom.getJSONObject(i);
                            latlngs.add(new LatLng(c.getDouble("fLatNeg"),c.getDouble("fLonNeg")));
                            infoCanchas[i] = new DAONegocio(c.getInt("nCodNeg"),c.getString("cNomNeg"),c.getString("cNumNeg"),c.getDouble("fPrecMin"),c.getDouble("fPrecMax"),c.getString("cUrlImg"),c.getString("tHoraIni"),c.getString("tHorafin"));

                        }
                        flagPorDist = true;
                    }
                    else
                        flagPorDist = false;


                } catch (final JSONException e) {
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
            mMap.clear();
            c=0;
            if(flagPorDist) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (final LatLng point : latlngs) {

                    addMarker(mMap, point.latitude, point.longitude,
                            R.string.carnegie_hall, R.string.practice_x3);

                    builder.include(point);
                }
                mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
                mMap.setOnInfoWindowClickListener(MapsTodosActivity.this);

                LatLngBounds bounds = builder.build();

                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                mMap.animateCamera(cu);
            }

            else
                Toast.makeText(getApplicationContext(),
                        "No se encontraron coincidencias",
                        Toast.LENGTH_LONG).show();
        }


    }
*/
}


