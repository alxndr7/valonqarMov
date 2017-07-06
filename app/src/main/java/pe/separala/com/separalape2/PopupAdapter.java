package pe.separala.com.separalape2;

import com.bumptech.glide.Glide;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import pe.separala.com.separalape2.model.DAONegocio;

/**
 * Created by Alexander on 21/05/2017.
 */

public class PopupAdapter implements InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;
    private TextView textView1,textDir;
    private DAONegocio inf;
    private ImageView imgCancha;

    PopupAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.info_window, null);

        }
        inf = (DAONegocio) marker.getTag();
        imgCancha = (ImageView) popup.findViewById(R.id.imgCancha);
        Glide.with(imgCancha.getContext()).load(inf.getC_url_img()).into(imgCancha);

        textView1 = (TextView) popup.findViewById(R.id.textView1);
        textView1.setText(inf.getC_raz_soc_neg());
        textDir = (TextView) popup.findViewById(R.id.textDir);
        textDir.setText(marker.getTitle());

        return(popup);
    }
}
