package pe.separala.com.separalape2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.separala.com.separalape2.model.DAOCancha;
import pe.separala.com.separalape2.model.DAOEventos;
import pe.separala.com.separalape2.model.DAONegocio;
import pe.separala.com.separalape2.model.NegocioDBHelper;

import static android.content.ContentValues.TAG;
import static pe.separala.com.separalape2.Constantes.OBTENER_CANCHAS;


/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
public class CalendarioActivity extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private int current_mounth = 0;
    private WeekView mWeekView;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private DAOEventos[] arrayEventos;
    private int size_eventos;
    private NegocioDBHelper mDbHelper;
    private List<DAOCancha> listCanchas;
    private int cod_neg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        mDbHelper = new NegocioDBHelper(this);
        listCanchas = new ArrayList<DAOCancha>();

        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos

            if((int) extras.getSerializable("flagEventos") == 1) {
                size_eventos = (int) extras.getSerializable("length");
                arrayEventos = new DAOEventos[size_eventos];
                arrayEventos = (DAOEventos[]) extras.getSerializable("Eventos");
                cod_neg = (int) extras.getSerializable("id_neg");
                Log.i("CALENDARIO", "ON CREATE: " + size_eventos);
            }
            else
                cod_neg = (int) extras.getSerializable("id_neg");

        }

        listCanchas = mDbHelper.getCanchasByNeg(cod_neg);

        Button btnTmp;
        String id;

        for (int i = 1; i <= listCanchas.size(); i++) {
                id = "cancha";
                id = id + i;
            int resID = getResources().getIdentifier(id, "id" , getPackageName());
            btnTmp = (Button) findViewById(resID);
            btnTmp.setVisibility(View.VISIBLE);
            btnTmp.setText(listCanchas.get(i-1).getC_desc_cancha() + " / " + listCanchas.get(i-1).getN_num_jug_recomen() + " Pers.");
            btnTmp.setBackgroundColor(Color.parseColor(listCanchas.get(i-1).getC_color_cancha()));
        }

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        //mWeekView.notifyDatasetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){/*
            case R.id.action_today:
                mWeekView.goToToday();
                return true;*/
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        current_mounth = Calendar.getInstance().get(Calendar.MONTH);

        // Populate the week view with some events.
            Log.i("onMonthChange2", newYear + "/" + newMonth + " current:" + current_mounth);
        /*
        String date = "2014-09-11";
    String dt[]=date.split("-");
    Calendar cal=Calendar.getInstance();
    cal.add(Calendar.DATE, Integer.parseInt(dt[2]));
    cal.add(Calendar.MONTH,Integer.parseInt(dt[1]));
    cal.add(Calendar.YEAR,Integer.parseInt(dt[0]));
*/
        if(current_mounth == newMonth){
            Log.i("ifonMonthChange2", newYear + "/" + newMonth);
            listarEventos();
            return events;
        }
        else
            return new ArrayList<WeekViewEvent>();
/*
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);*/


    }



    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        //Toast.makeText(CalendarioActivity.this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        Toast.makeText(CalendarioActivity.this, "Evento de " +  df.format(event.getStartTime().getTime()) + " a " +  df.format(event.getEndTime().getTime()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(CalendarioActivity.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    public void verEventos(android.view.View view) {
        Log.i("verEventos", "ONCLICK");
        //finish();
        String tmp_fecha;
        String[] parts_datetime;
        String[] parts_date;
        String[] parts_time;

        events = new ArrayList<WeekViewEvent>();
        Calendar startTime;
        Calendar endTime;
        WeekViewEvent event;

        for(int i=0;i<size_eventos;i++){
            startTime = Calendar.getInstance();
            parts_datetime = arrayEventos[i].getFecha_inicio().split(" ");
            parts_date = parts_datetime[0].split("-");
            parts_time = parts_datetime[1].split(":");
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts_time[0]));
            startTime.set(Calendar.MINUTE, Integer.parseInt(parts_time[1]));
            startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts_date[2]));
            startTime.set(Calendar.MONTH, Integer.parseInt(parts_date[1]) - 1);
            startTime.set(Calendar.YEAR, Integer.parseInt(parts_date[0]));

            endTime = Calendar.getInstance();
            parts_datetime = arrayEventos[i].getFecha_fin().split(" ");
            parts_date = parts_datetime[0].split("-");
            parts_time = parts_datetime[1].split(":");

            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts_time[0]));
            endTime.set(Calendar.MINUTE, Integer.parseInt(parts_time[1]));
            endTime.set(Calendar.MONTH, Integer.parseInt(parts_date[1])-1);
            endTime.set(Calendar.DATE, Integer.parseInt(parts_date[2]));
            endTime.set(Calendar.YEAR, Integer.parseInt(parts_date[0]));

            event = new WeekViewEvent(arrayEventos[i].getN_cod_even(), arrayEventos[i].getC_desc_even(), startTime, endTime);

            Log.i("verEventos","LUEGO DE EVENTO");
            switch(arrayEventos[i].getColor()) {
                case "#9C27B0":
                    event.setColor(getResources().getColor(R.color.color_cancha1));
                    break;
                case "#009688":
                    event.setColor(getResources().getColor(R.color.color_cancha2));
                    break;
                case "#CDDC39":
                    event.setColor(getResources().getColor(R.color.color_cancha3));
                    break;
                case "#4CAF50":
                    event.setColor(getResources().getColor(R.color.color_cancha4));
                    break;
                case "#FF9800":
                    event.setColor(getResources().getColor(R.color.color_cancha5));
                    break;
                default:
                    event.setColor(getResources().getColor(R.color.cancha_default));
                    break;
            }
            Log.i("verEventos","LUEGO DE COLOR EVENTO");
            events.add(event);
        }

        mWeekView.notifyDatasetChanged();
    }

    public void listarEventos() {
        Log.i("verEventos", "ONCLICK");
        //finish();
        String tmp_fecha;
        String[] parts_datetime;
        String[] parts_date;
        String[] parts_time;

        events = new ArrayList<WeekViewEvent>();
        Calendar startTime;
        Calendar endTime;
        WeekViewEvent event;

        for(int i=0;i<size_eventos;i++){
            startTime = Calendar.getInstance();
            parts_datetime = arrayEventos[i].getFecha_inicio().split(" ");
            parts_date = parts_datetime[0].split("-");
            parts_time = parts_datetime[1].split(":");
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts_time[0]));
            startTime.set(Calendar.MINUTE, Integer.parseInt(parts_time[1]));
            startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts_date[2]));
            startTime.set(Calendar.MONTH, Integer.parseInt(parts_date[1]) - 1);
            startTime.set(Calendar.YEAR, Integer.parseInt(parts_date[0]));

            endTime = Calendar.getInstance();
            parts_datetime = arrayEventos[i].getFecha_fin().split(" ");
            parts_date = parts_datetime[0].split("-");
            parts_time = parts_datetime[1].split(":");

            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts_time[0]));
            endTime.set(Calendar.MINUTE, Integer.parseInt(parts_time[1]));
            endTime.set(Calendar.MONTH, Integer.parseInt(parts_date[1])-1);
            endTime.set(Calendar.DATE, Integer.parseInt(parts_date[2]));
            endTime.set(Calendar.YEAR, Integer.parseInt(parts_date[0]));

            event = new WeekViewEvent(arrayEventos[i].getN_cod_even(), arrayEventos[i].getC_desc_even(), startTime, endTime);
            event.setColor(Color.parseColor(arrayEventos[i].getColor()));

            Log.i("verEventos","LUEGO DE EVENTO");
            /*
            switch(arrayEventos[i].getColor()) {
                case "#9C27B0":
                    event.setColor(getResources().getColor(R.color.color_cancha1));
                    break;
                case "#009688":
                    event.setColor(getResources().getColor(R.color.color_cancha2));
                    break;
                case "#CDDC39":
                    event.setColor(getResources().getColor(R.color.color_cancha3));
                    break;
                case "#4CAF50":
                    event.setColor(getResources().getColor(R.color.color_cancha4));
                    break;
                case "#FF9800":
                    event.setColor(getResources().getColor(R.color.color_cancha5));
                    break;
                default:
                    event.setColor(getResources().getColor(R.color.cancha_default));
                    break;
            }*/
            Log.i("verEventos","LUEGO DE COLOR EVENTO");
            events.add(event);
        }

        mWeekView.notifyDatasetChanged();
    }
}
