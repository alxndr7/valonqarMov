package pe.separala.com.separalape2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.separala.com.separalape2.model.DAOCancha;
import pe.separala.com.separalape2.model.DAOEventos;
import pe.separala.com.separalape2.model.DAONegocio;
import pe.separala.com.separalape2.model.NegocioDBHelper;

import static android.content.ContentValues.TAG;
import static pe.separala.com.separalape2.Constantes.OBTENER_EVENTOS_POR_CANCHA;

public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private static final String TAG = "DemoActivity";
    private GoogleMap mMap;
    private List<DAONegocio> allNegocios;
    private NegocioDBHelper mDbHelper;
    private LinearLayout mLayoutTab;
    private Bitmap smallMarker;
    List<String> spinnerArray =  new ArrayList<String>();
    private boolean needsInit=false;
    private Spinner sItems;
    private int idDistTmp = 0;
    private SlidingUpPanelLayout mLayout;
    private TextView txt_nombre,txt_dir,txt_telefono, txt_horario, txt_precio,flag_comida,flag_cochera,flag_calendario;
    private ImageView img_neg;
    private int tmp_cont = 0;
    private List<DAOCancha> listCanchas;
    private DAOEventos[] jsonEventos;
    private int idNegocio;
    private boolean flagEventos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txt_nombre = (TextView) findViewById(R.id.nom_neg);
        img_neg = (ImageView) findViewById(R.id.img_neg);
        txt_dir = (TextView) findViewById(R.id.dir_neg);
        txt_horario = (TextView) findViewById(R.id.txt_horario);
        txt_telefono = (TextView) findViewById(R.id.txt_telefono);
        txt_precio = (TextView) findViewById(R.id.txt_precio);
        flag_calendario = (TextView) findViewById(R.id.flag_calendar);
        flag_cochera = (TextView) findViewById(R.id.flag_cochera);
        flag_comida = (TextView) findViewById(R.id.flag_comida);

        mDbHelper = new NegocioDBHelper(this);
        listCanchas = new ArrayList<DAOCancha>();

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


        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.3f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {

        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
       DAONegocio neg = allNegocios.get(Integer.parseInt(arg0.getTag().toString()));

        idNegocio = neg.getN_cod_neg();
        txt_nombre.setText(neg.getC_raz_soc_neg());
        Glide.with(img_neg.getContext()).load(neg.getC_url_img()).into(img_neg);
        txt_dir.setText(neg.getC_dir_neg());

        listCanchas = mDbHelper.getCanchasByNeg(neg.getN_cod_neg());
        txt_telefono.setText(neg.getC_tel_neg());
        txt_horario.setText(neg.getT_hora_ini() + " - " + neg.getT_hora_fin());
        txt_precio.setText(neg.getN_prec_min() + " - " + neg.getN_prec_max() + " soles");
        flag_cochera.setText((neg.getN_cochera_neg() == 1) ? "SI" : "NO");
        flag_comida.setText((neg.getN_comida_neg() == 1) ? "SI" : "NO");

        String id_tr="trCancha", id_desc = "tvDescC", id_dim = "tvDimC", id_jug_re = "tvNumJugC", id_color = "tvColorC";
        int res_id_tr,res_id_desc,res_id_dim,res_id_num, res_id_color;
        TableRow tr;
        TextView tvdes,tvdim,tvnumjug,tvcolor;
        Log.i("NUMCANCHAS", "Tamaño:" + listCanchas.size());

        for(int i=1;i<=5;i++){
            id_tr="trCancha";
            id_tr = id_tr+ i;
            res_id_tr = getResources().getIdentifier(id_tr, "id" , getPackageName());
            tr = (TableRow) findViewById(res_id_tr);
            tr.setVisibility(View.GONE);
        }

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

        Toast.makeText(this, "Posicion: " + arg0.getId() + " , " +arg0.getTag() , Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        MapsInitializer.initialize(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        allNegocios = mDbHelper.getAllNegocios();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int i=0;i<allNegocios.size();i++){
            LatLng point = new LatLng(allNegocios.get(i).getN_lat_neg(), allNegocios.get(i).getN_lon_neg());
            addMarker(mMap, point.latitude, point.longitude,allNegocios.get(i).getC_raz_soc_neg(), allNegocios.get(i).getN_cod_neg());
            builder.include(point);

        }

        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);

    }

    public void verCalendario(android.view.View view) {
        Log.i("VERCALENDARIO", "LISTAR CATEGORIA");

        new MapsActivity.getEventos().execute();
        //finish();
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
        } else {

            mLayoutTab.setVisibility(View.GONE);
        }
    }


    public void onItemSelected(AdapterView<?> parent,
                               View view, int pos, long id) {
        Toast.makeText(getApplicationContext(), "The planet is " +
                parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           String title,int cod_neg) {
        Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                m.setTag(tmp_cont++);
    }


    private class getEventos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MapsActivity.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = OBTENER_EVENTOS_POR_CANCHA + "/" + idNegocio;
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

                Intent explicit_intent = new Intent(MapsActivity.this, CalendarioActivity.class);
                explicit_intent.putExtra("flagEventos",0);
                explicit_intent.putExtra("id_neg", idNegocio);
                MapsActivity.this.startActivity(explicit_intent);
            }
            else {
                /**************** Create Custom Adapter *********/
                Intent explicit_intent = new Intent(MapsActivity.this, CalendarioActivity.class);
                explicit_intent.putExtra("flagEventos",1);
                explicit_intent.putExtra("Eventos", jsonEventos);
                explicit_intent.putExtra("length", jsonEventos.length);
                explicit_intent.putExtra("id_neg", idNegocio);
                MapsActivity.this.startActivity(explicit_intent);
            }

        }

    }


}