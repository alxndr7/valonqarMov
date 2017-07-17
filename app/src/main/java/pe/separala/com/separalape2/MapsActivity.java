package pe.separala.com.separalape2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.separala.com.separalape2.model.DAONegocio;
import pe.separala.com.separalape2.model.NegocioDBHelper;

public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

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
    private TextView txt_nombre;
    private ImageView img_neg;
    private int tmp_cont = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txt_nombre = (TextView) findViewById(R.id.nom_neg);
        img_neg = (ImageView) findViewById(R.id.img_neg);

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


        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.4f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

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
    public boolean onMarkerClick(Marker arg0) {
       DAONegocio neg = allNegocios.get(Integer.parseInt(arg0.getTag().toString()));

        txt_nombre.setText(neg.getC_raz_soc_neg());
        Glide.with(img_neg.getContext()).load(neg.getC_url_img()).into(img_neg);

        Toast.makeText(this, "Posicion: " + arg0.getId() + " , " +arg0.getTag() , Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        MapsInitializer.initialize(this);
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
                           String title,int cod_neg) {
        Marker m = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                m.setTag(tmp_cont++);
    }

}