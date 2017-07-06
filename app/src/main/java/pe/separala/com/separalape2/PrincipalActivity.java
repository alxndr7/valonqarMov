package pe.separala.com.separalape2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.separala.com.separalape2.model.DAONegocio;

import static android.content.ContentValues.TAG;
import static pe.separala.com.separalape2.Constantes.OBTENER_CANCHAS;

public class PrincipalActivity extends AppCompatActivity {
    ListView list;
    GridAdapterComensales adapter;
    public  PrincipalActivity CustomListView = null;
    public boolean flagComensales;
    private DAONegocio[] jsonComensales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle("Separala.pe");
        CustomListView = this;
        list=(ListView)findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DAONegocio cancha =  jsonComensales[position];

                Log.i("MyActivity", "Selected Note: " + jsonComensales[position].getN_cod_neg());

                Intent explicit_intent = new Intent(PrincipalActivity.this, AboutCanchaActivity.class);
                explicit_intent.putExtra("Cancha",cancha);
                PrincipalActivity.this.startActivity(explicit_intent);
                //Log.d(TAG, "Selected Note: " + selActa.getFch_inspeccion());
                //Instanciamos el DialogForNote
                //DialogForNote newAddNote = new DialogForNote();

                //Seteamos la nota que deseamos actualizar
                //newAddNote.setmNoteForUpd(selNote);

                //Mostramos el DialogForNote asignadole el tag "addnote"
                //newAddNote.show(getSupportFragmentManager(), "addnote");

            }
        });


        new PrincipalActivity.GetComensales().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void listarCanchas(android.view.View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");

        Intent intent = new Intent(PrincipalActivity.this, ListarCanchasActivity.class);
        startActivity(intent);
        //finish();
    }

    public void listarCanchasMaps(android.view.View view) {
        //Log.d("MAIN", "LISTAR CATEGORIA");

        Intent intent = new Intent(PrincipalActivity.this, MapsTodosActivity.class);
        startActivity(intent);
        //finish();
    }

    private class GetComensales extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(PrincipalActivity.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

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

                    if(JsonCom.length() != 0){
                        jsonComensales = new DAONegocio[JsonCom.length()];
                        for (int i = 0; i < JsonCom.length(); i++) {
                            JSONObject c = JsonCom.getJSONObject(i);
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

                            //Log.e(TAG, "Parse JSON consumos :" + c.getString("cNomCom") + '/' + c.getString("cApeCom"));
/*
                        Log.e(TAG, "Parse JSON : " + nombre + '/' + dni + '/' + fecha + '/' + cantMenu );

                        Log.e(TAG, "Modelo  : " + jsonItemsConsumos[i].getNombre());

                        jsonItemsConsumos[i].setNombre(c.getString("cNomCom"));
                        jsonItemsConsumos[i].setDni(c.getString("cDniCom"));
                        jsonItemsConsumos[i].setFecha(c.getString("dFecDet"));
                        jsonItemsConsumos[i].setFecha(c.getString("nCantMenu"));*/
                        }

                        flagComensales = true;
                    }

                    else
                        flagComensales = false;


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

            if(!flagComensales){
                Toast.makeText(getApplicationContext(),
                        "Usuario o contraseña incorrecta, intentelo nuevamente!", Toast.LENGTH_LONG)
                        .show();
            }
            else {
                /**************** Create Custom Adapter *********/
                //adapter=new GridAdapterComensales(CustomListView, jsonComensales);
                //list.setAdapter(adapter);
            }

        }

           /* super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Login.this, contactList,
                    R.layout.list_item, new String[]{ "email","mobile"},
                    new int[]{R.id.email, R.id.mobile});
            lv.setAdapter(adapter);*/

    }


}
