package pe.separala.com.separalape2;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

import pe.separala.com.separalape2.model.DAONegocio;


/**
 * {@link BaseAdapter} personalizado para el gridview
 */
public class GridAdapterComensales extends BaseAdapter {

    private final Context mContext;
    private final List<DAONegocio> items;
    private Activity activity;
    private DAONegocio item;
    private int cod;
    private boolean flagEliminar;

    public GridAdapterComensales(Context c, List<DAONegocio> items) {
        //Log.d(TAG,"ITEMS : " +  items.length);
        mContext = c;
        this.items = items;
    }

    @Override
    public int getCount() {
        // Decremento en 1, para no contar el header view

        return items.size();
    }

    @Override
    public DAONegocio getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //Log.d(TAG,"ITEMS :  LLEGO" );

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.item_cancha, viewGroup, false);

        }
        item = getItem(position);

        // Seteando Imagen
       ImageView image = (ImageView) view.findViewById(R.id.imgCancha);
        Glide.with(image.getContext()).load(item.getC_url_img()).into(image);

        // Seteando Nombre
        TextView name = (TextView) view.findViewById(R.id.nombre);
        name.setText(item.getC_dir_neg());

        // Seteando Descripción
        TextView descripcion = (TextView) view.findViewById(R.id.precio);
        descripcion.setText(item.getC_raz_soc_neg());

        MaterialFavoriteButton fav = (MaterialFavoriteButton) view.findViewById(R.id.fav_list);

        fav.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        Log.i("FAVORITE", "ESTADO: " + favorite + " ITEM:" + items.get(position).getC_raz_soc_neg());
                    }
                });
/*
        ImageButton fav = (ImageButton) view.findViewById(R.id.btn_go);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i("FAVORITOS", "click " + items.get(position).getC_raz_soc_neg());
            }
        });
*/
/*
        Button btn=(Button)view.findViewById(R.id.btnEliminar);
        Button.OnClickListener mOkOnClickListener = new Button.OnClickListener()
        {
            public void onClick(View v) {
                Log.v("ttttttt", ""+ items[position].getnCodUsuCom() );

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("ALERTA !!");
                alert.setMessage("Esta seguro de eliminar este comensal? Ya no podrá ver información relacionada.");
                alert.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        cod = items[position].getnCodUsuCom();
                        new eliminarComensal().execute();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
                //Toast.makeText(Context,""+item.getcApeCom(), Toast.LENGTH_LONG).show();
            }
        };
        btn.setOnClickListener(mOkOnClickListener);*/

        // Button button = (Button) view.findViewById(R.id.btnEliminar);
        //button.setOnClickListener(new view.OnClickListener(){});

        // Seteando Rating
       RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
        ratingBar.setRating(3);

        return view;
    }

}
