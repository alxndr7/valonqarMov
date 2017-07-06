package pe.separala.com.separalape2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pe.separala.com.separalape2.model.DAOCancha;
import pe.separala.com.separalape2.model.DAONegocio;
import pe.separala.com.separalape2.model.DAOEventos;
import pe.separala.com.separalape2.model.DAOUbicacion;
import pe.separala.com.separalape2.model.NegocioDBHelper;

import static android.content.ContentValues.TAG;
import static pe.separala.com.separalape2.Constantes.OBTENER_EVENTOS_POR_CANCHA;
import static pe.separala.com.separalape2.Constantes.OBTENER_UBICACION_CANCHA;

public class AboutCanchaActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    private DAONegocio cancha;
    private int idCancha;
    private double lat,lon;
    private DAOUbicacion ubi;
    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleMapOptions googleMapOptions;
    MapFragment mapFragment;
    private Bitmap smallMarker;
    private DAOEventos[] jsonEventos;
    private boolean flagEventos = false;
    private NegocioDBHelper mDbHelper;
    private List<DAOCancha> listCanchas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_cancha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDbHelper = new NegocioDBHelper(this);
        listCanchas = new ArrayList<DAOCancha>();

        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos
            cancha = (DAONegocio) extras.getSerializable("Cancha");
            idCancha = cancha.getN_cod_neg();
            listCanchas = mDbHelper.getCanchasByNeg(idCancha);

            String id_tr="trCancha", id_desc = "tvDescC", id_dim = "tvDimC", id_jug_re = "tvNumJugC", id_color = "tvColorC";
            int res_id_tr,res_id_desc,res_id_dim,res_id_num, res_id_color;
            TableRow tr;
            TextView tvdes,tvdim,tvnumjug,tvcolor;

            for (int i = 1; i <= listCanchas.size(); i++) {
                id_tr="trCancha"; id_desc = "tvDescC"; id_dim = "tvDimC"; id_jug_re = "tvNumJugC"; id_color = "tvColorC";
                id_tr = id_tr+ i; id_desc =id_desc +i; id_dim = id_dim +i; id_jug_re = id_jug_re +i ; id_color = id_color +i;
                Log.i("MyActivity", "ON CREATE DESC: " + listCanchas.get(i-1).getC_desc_cancha());
                Log.i("MyActivity", "ON CREATE IDS: " + id_tr + "/" + id_desc + "/" + id_dim + "/" + id_jug_re + "/" + id_color);

                res_id_tr = getResources().getIdentifier(id_tr, "id" , getPackageName());
                tr = (TableRow) findViewById(res_id_tr);
                tr.setVisibility(View.VISIBLE);

                res_id_desc = getResources().getIdentifier(id_desc, "id" , getPackageName());
                tvdes = (TextView) findViewById(res_id_desc);
                tvdes.setText(listCanchas.get(i-1).getC_desc_cancha());

                res_id_dim = getResources().getIdentifier(id_dim, "id" , getPackageName());
                tvdim = (TextView) findViewById(res_id_dim);
                tvdim.setText(listCanchas.get(i-1).getN_largo_cancha() + "m x " + listCanchas.get(i-1).getN_ancho_cancha() +"m");

                res_id_num = getResources().getIdentifier(id_jug_re, "id" , getPackageName());
                tvnumjug = (TextView)findViewById(res_id_num);
                tvnumjug.setText(listCanchas.get(i-1).getN_num_jug_recomen() + "Pers.");

                res_id_color = getResources().getIdentifier(id_color, "id" , getPackageName());
                tvcolor = (TextView) findViewById(res_id_color);
                tvcolor.setBackgroundColor(Color.parseColor(listCanchas.get(i-1).getC_color_cancha()));


            }
        }

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        TextView tvNombre = (TextView) findViewById(R.id.tvNomNeg);
        tvNombre.setText(cancha.getC_raz_soc_neg());
        TextView direccion = (TextView) findViewById(R.id.tvDireccion);
        direccion.setText(cancha.getC_dir_neg());
        TextView horario = (TextView) findViewById(R.id.tvHorario);
        horario.setText(cancha.getT_hora_ini() + " - " + cancha.getT_hora_fin());
        TextView telefono =(TextView) findViewById(R.id.tvTelefono);
        telefono.setText(cancha.getC_tel_neg());
        TextView precios = (TextView) findViewById(R.id.tvPrecios);
        precios.setText(cancha.getN_prec_min() + " - " + cancha.getN_prec_max() + " Soles");

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put(cancha.getC_raz_soc_neg(), cancha.getC_url_img());
        url_maps.put(cancha.getC_raz_soc_neg(), cancha.getC_url_img());

       /* HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Nuestro Local Av. Ejercito",R.drawable.tablonlocal);
        file_maps.put("Hoy domingo Caldo de Lomos",R.drawable.caldolomos);*/

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        int height = 70;
        int width = 50;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.markerseparala);
        Bitmap b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        new AboutCanchaActivity.GetUbicacion().execute();

        mapFragment = MapFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fmap, mapFragment)
                .commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(cancha.getN_lat_neg(),cancha.getN_lon_neg());
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(cancha.getC_raz_soc_neg())
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMapOptions = new GoogleMapOptions().liteMode(true);
                googleMap.setMapType(googleMapOptions.getMapType());

            }
        });



    }

    public void verEnMapa(View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");

        Intent intent = new Intent(AboutCanchaActivity.this, ComoLlegarActivity.class);
        intent.putExtra("ubicacion",ubi);
        AboutCanchaActivity.this.startActivity(intent);
        //finish();
    }

    public void verCalendario(android.view.View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");

        new AboutCanchaActivity.getEventos().execute();
        //finish();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        // Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


    private class GetUbicacion extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(AboutCanchaActivity.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = OBTENER_UBICACION_CANCHA + '/' + idCancha ;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray JsonCom = jsonObj.getJSONArray("ubicacion");

                    if(JsonCom.length() != 0){

                        for (int i = 0; i < JsonCom.length(); i++) {
                            JSONObject c = JsonCom.getJSONObject(i);
                            ubi = new DAOUbicacion(
                                    c.getInt("nCodUbi"),
                                    c.getInt("nCodNeg"),
                                    c.getDouble("fLatNeg"),
                                    c.getDouble("fLonNeg")
                            );
                            Log.e(TAG, "(LAT,LON): (" + lat + "," + lon + ")");
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



            Toast.makeText(getApplicationContext(),
                        "Usuario o contraseña incorrecta, intentelo nuevamente!", Toast.LENGTH_LONG)
                        .show();

        }


    }


    private class getEventos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(AboutCanchaActivity.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = OBTENER_EVENTOS_POR_CANCHA + "/" + idCancha;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray JsonCom = jsonObj.getJSONArray("eventos");

                    if(JsonCom.length() != 0){
                        jsonEventos = new DAOEventos[JsonCom.length()];
                        for (int i = 0; i < JsonCom.length(); i++) {
                            JSONObject c = JsonCom.getJSONObject(i);
                            jsonEventos[i] = new DAOEventos(
                                    c.getInt("id"),
                                    c.getInt("n_cod_neg"),
                                    c.getInt("n_cod_cancha"),
                                    c.getString("title"),
                                    c.getString("start"),
                                    c.getString("end"),
                                    c.getString("color"),
                                    c.getString("icon")
                            );

                            //Log.e(TAG, "Parse JSON consumos :" + c.getString("cNomCom") + '/' + c.getString("cApeCom"));
/*
                        Log.e(TAG, "Parse JSON : " + nombre + '/' + dni + '/' + fecha + '/' + cantMenu );

                        Log.e(TAG, "Modelo  : " + jsonItemsConsumos[i].getNombre());

                        jsonItemsConsumos[i].setNombre(c.getString("cNomCom"));
                        jsonItemsConsumos[i].setDni(c.getString("cDniCom"));
                        jsonItemsConsumos[i].setFecha(c.getString("dFecDet"));
                        jsonItemsConsumos[i].setFecha(c.getString("nCantMenu"));*/
                        }

                        flagEventos = true;
                    }

                    else
                        flagEventos = false;


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

            if(!flagEventos){
                Toast.makeText(getApplicationContext(),
                        "No se encontraron eventos para esta semana!", Toast.LENGTH_LONG)
                        .show();

                Intent explicit_intent = new Intent(AboutCanchaActivity.this, CalendarioActivity.class);
                explicit_intent.putExtra("flagEventos",0);
                explicit_intent.putExtra("id_neg", idCancha);
                AboutCanchaActivity.this.startActivity(explicit_intent);
            }
            else {
                /**************** Create Custom Adapter *********/
                Intent explicit_intent = new Intent(AboutCanchaActivity.this, CalendarioActivity.class);
                explicit_intent.putExtra("flagEventos",1);
                explicit_intent.putExtra("Eventos", jsonEventos);
                explicit_intent.putExtra("length", jsonEventos.length);
                explicit_intent.putExtra("id_neg", idCancha);
                AboutCanchaActivity.this.startActivity(explicit_intent);
            }

        }

    }



}
