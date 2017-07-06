package pe.separala.com.separalape2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ExpoCode Tech http://expocodetech.com
 */
public class NegocioDBHelper extends SQLiteOpenHelper {

    //Definimos in Contructor para Instanciar el Databse Helper
    public NegocioDBHelper(Context context) {
        super(context, NegocioDBDef.DATABASE_NAME, null, NegocioDBDef.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creamos las tablas en la Base de datos
        db.execSQL(NegocioDBDef.MNEGOCIO_TABLE_CREATE);
        db.execSQL(NegocioDBDef.MUSUARIO_TABLE_CREATE);
        db.execSQL(NegocioDBDef.DCANCHAS_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //El método onUpgrade se ejecuta cada vez que recompilamos e instalamos la app con un
        //nuevo numero de version de base de datos (DATABASE_VERSION), de tal mamera que en este
        // método lo que hacemos es:
        // 1. Con esta sentencia borramos la tabla "notes"
        db.execSQL(NegocioDBDef.NEGOCIO_TABLE_DROP);
        db.execSQL(NegocioDBDef.MUSUARIO_TABLE_DROP);
        db.execSQL(NegocioDBDef.DCANCHAS_TABLE_DROP);


        // 2. Con esta sentencia llamamos de nuevo al método onCreate para que se cree de nuevo
        //la tabla "notes" la cual seguramente al cambair de versión ha tenido modifciaciones
        // en cuanto a su estructura de columnas
        this.onCreate(db);

    }


    /*
    * OPERACIONES CRUD (Create, Read, Update, Delete)
    * A partir de aqui se definen los metodos para insertar, leer, actualizar y borrar registros de
    * la base de datos (BD)
    * */

    public void insertNegocio(DAONegocio neg){
        //Con este método insertamos las notas nuevas que el usuario vaya creando

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos un obejto de tipo ContentValues para agregar los pares
        // de Claves de Columna y Valor
        ContentValues values = new ContentValues();
        values.put(NegocioDBDef.NEGOCIO.ID_COL, neg.getN_cod_neg()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.RAZ_SOC_NEG_COL, neg.getC_raz_soc_neg()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.RUC_NEG_COL, neg.getC_ruc_neg()); // Descripción
        values.put(NegocioDBDef.NEGOCIO.TEL_NEG_COL, neg.getC_tel_neg()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.DIR_NEG_COL, neg.getC_dir_neg()); // Titulo
        //values.put(InscampoDBDef.INSCAMPO.TIPO_CERCO_COL, acta.getTipo_cerco()); // Descripción
        values.put(NegocioDBDef.NEGOCIO.NUM_CANCHAS_COL, neg.getN_num_canchas());
        values.put(NegocioDBDef.NEGOCIO.NOM_REP_COL, neg.getC_nom_rep_neg());
        values.put(NegocioDBDef.NEGOCIO.TEL_REP_NEG_COL, neg.getC_tel_rep_neg());
        values.put(NegocioDBDef.NEGOCIO.LAT_NEG_COL, neg.getN_lat_neg());
        values.put(NegocioDBDef.NEGOCIO.LON_NEG_COL, neg.getN_lon_neg());
        values.put(NegocioDBDef.NEGOCIO.COMIDA_NEG_COL, neg.getN_comida_neg());
        //values.put(InscampoDBDef.INSCAMPO.HABITACION_COL, acta.getHabitacion()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.COCHERA_NEG_COL, neg.getN_cochera_neg());
        values.put(NegocioDBDef.NEGOCIO.PREC_MIN_COL, neg.getN_prec_min());
        values.put(NegocioDBDef.NEGOCIO.PREC_MAX_COL, neg.getN_prec_max());
        values.put(NegocioDBDef.NEGOCIO.HORA_INI_COL, neg.getT_hora_ini());
        values.put(NegocioDBDef.NEGOCIO.HORA_FIN_COL, neg.getT_hora_fin());
        values.put(NegocioDBDef.NEGOCIO.COD_DIS_COL, neg.getN_cod_dist());
        //values.put(InscampoDBDef.INSCAMPO.TIPO_DE_TECHO_COL, acta.getTipo_de_techo()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.URL_IMG_COL, neg.getC_url_img());
        values.put(NegocioDBDef.NEGOCIO.EST_NEG_COL, neg.getC_est_neg());

        // 3. Insertamos los datos en la tabla "notes"
        db.insert(NegocioDBDef.NEGOCIO.TABLE_NAME, null, values);

        // 4. Cerramos la conexión comn la BD
        db.close();
    }

    //Obtener uan Nota dado un ID
    public DAONegocio getNegocioById(int id){
        // Declaramos un objeto Inscampo para instanciarlo con el resultado del query
        DAONegocio aNeg = null;

        // 1. Obtenemos una reference de la BD con permisos de lectura
        SQLiteDatabase db = this.getReadableDatabase();

        //Definimos un array con los nombres de las columnas que deseamos sacar
        String[] COLUMNS = {
                NegocioDBDef.NEGOCIO.ID_COL,
                NegocioDBDef.NEGOCIO.RAZ_SOC_NEG_COL,
                NegocioDBDef.NEGOCIO.RUC_NEG_COL,
                NegocioDBDef.NEGOCIO.TEL_NEG_COL,
                NegocioDBDef.NEGOCIO.DIR_NEG_COL,
                NegocioDBDef.NEGOCIO.NUM_CANCHAS_COL,
                NegocioDBDef.NEGOCIO.NOM_REP_COL,
                NegocioDBDef.NEGOCIO.TEL_REP_NEG_COL,
                NegocioDBDef.NEGOCIO.LAT_NEG_COL,
                NegocioDBDef.NEGOCIO.LON_NEG_COL,
                NegocioDBDef.NEGOCIO.COMIDA_NEG_COL,
                NegocioDBDef.NEGOCIO.COCHERA_NEG_COL,
                NegocioDBDef.NEGOCIO.PREC_MIN_COL,
                NegocioDBDef.NEGOCIO.PREC_MAX_COL,
                NegocioDBDef.NEGOCIO.HORA_INI_COL,
                NegocioDBDef.NEGOCIO.HORA_FIN_COL,
                NegocioDBDef.NEGOCIO.COD_DIS_COL,
                NegocioDBDef.NEGOCIO.URL_IMG_COL,
                NegocioDBDef.NEGOCIO.EST_NEG_COL
        };


        // 2. Contruimos el query
        Cursor cursor =
                db.query(NegocioDBDef.NEGOCIO.TABLE_NAME,  //Nomre de la tabla
                        COLUMNS, // b. Nombre de las Columnas
                        " n_cod_neg = ?", // c. Columnas de la clausula WHERE
                        new String[] { String.valueOf(id) }, // d. valores de las columnas de la clausula WHERE
                        null, // e. Clausula Group by
                        null, // f. Clausula having
                        null, // g. Clausula order by
                        null); // h. Limte de regsitros

        // 3. Si hemos obtenido algun resultado entonces sacamos el primero de ellos ya que se supone
        //que ha de existir un solo registro para un id
        if (cursor != null) {
            cursor.moveToFirst();
            // 4. Contruimos el objeto Inscampo
            aNeg = new DAONegocio();
            aNeg.setN_cod_neg(Integer.parseInt(cursor.getString(0)));
            aNeg.setC_raz_soc_neg(cursor.getString(1));
            aNeg.setC_ruc_neg(cursor.getString(2));
            aNeg.setC_tel_neg(cursor.getString(3));
            aNeg.setC_dir_neg(cursor.getString(4));
            aNeg.setN_num_canchas(cursor.getInt(5));
            aNeg.setC_nom_rep_neg(cursor.getString(6));
            aNeg.setC_tel_rep_neg(cursor.getString(7));
            aNeg.setN_lat_neg(cursor.getDouble(8));
            aNeg.setN_lon_neg(cursor.getDouble(9));
            aNeg.setN_comida_neg(cursor.getInt(10));
            aNeg.setN_cochera_neg(cursor.getInt(11));
            aNeg.setN_prec_min(cursor.getDouble(12));
            aNeg.setN_prec_max(cursor.getDouble(13));
            aNeg.setT_hora_ini(cursor.getString(14));
            aNeg.setT_hora_fin(cursor.getString(15));
            aNeg.setN_cod_dist(cursor.getInt(16));
            aNeg.setC_url_img(cursor.getString(17));
            aNeg.setC_est_neg(cursor.getString(18));

        }

        // 5. Devolvemos le objeto Inscampo
        return aNeg;
    }

    // Obtener todas las notas
    public List<DAONegocio> getNegociosPorDistrito(int id) {
        //Instanciamos un Array para llenarlo con todos los objetos Notes que saquemos de la BD
        List<DAONegocio> negs = new ArrayList<DAONegocio>();

        // 1. Aramos un String con el query a ejecutar
        String query = "SELECT  * FROM " + NegocioDBDef.NEGOCIO.TABLE_NAME + " WHERE " + NegocioDBDef.NEGOCIO.COD_DIS_COL + " = " + id;

        // 2. Obtenemos una reference de la BD con permisos de escritura y ejecutamos el query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Iteramos entre cada uno de olos registros y agregarlos al array de Notas
        DAONegocio aNeg = null;
        if (cursor.moveToFirst()) {

            do {
                aNeg = new DAONegocio();

                aNeg.setN_cod_neg(Integer.parseInt(cursor.getString(0)));
                aNeg.setC_raz_soc_neg(cursor.getString(1));
                aNeg.setC_ruc_neg(cursor.getString(2));
                aNeg.setC_tel_neg(cursor.getString(3));
                aNeg.setC_dir_neg(cursor.getString(4));
                aNeg.setN_num_canchas(cursor.getInt(5));
                aNeg.setC_nom_rep_neg(cursor.getString(6));
                aNeg.setC_tel_rep_neg(cursor.getString(7));
                aNeg.setN_lat_neg(cursor.getDouble(8));
                aNeg.setN_lon_neg(cursor.getDouble(9));
                aNeg.setN_comida_neg(cursor.getInt(10));
                aNeg.setN_cochera_neg(cursor.getInt(11));
                aNeg.setN_prec_min(cursor.getDouble(12));
                aNeg.setN_prec_max(cursor.getDouble(13));
                aNeg.setT_hora_ini(cursor.getString(14));
                aNeg.setT_hora_fin(cursor.getString(15));
                aNeg.setN_cod_dist(cursor.getInt(16));
                aNeg.setC_url_img(cursor.getString(17));
                aNeg.setC_est_neg(cursor.getString(18));

                // Add book to books
                negs.add(aNeg);

            } while (cursor.moveToNext());
        }

        //Cerramos el cursor
        cursor.close();

        // Devolvemos las notas encontradas o un array vacio en caso de que no se encuentre nada
        return negs;
    }


    // Obtener todas las notas
    public List<DAONegocio> getAllNegocios() {
        //Instanciamos un Array para llenarlo con todos los objetos Notes que saquemos de la BD
        List<DAONegocio> negs = new ArrayList<DAONegocio>();

        // 1. Aramos un String con el query a ejecutar
        String query = "SELECT  * FROM " + NegocioDBDef.NEGOCIO.TABLE_NAME ;

        // 2. Obtenemos una reference de la BD con permisos de escritura y ejecutamos el query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Iteramos entre cada uno de olos registros y agregarlos al array de Notas
        DAONegocio aNeg = null;
        if (cursor.moveToFirst()) {

            do {
                aNeg = new DAONegocio();

                aNeg.setN_cod_neg(Integer.parseInt(cursor.getString(0)));
                aNeg.setC_raz_soc_neg(cursor.getString(1));
                aNeg.setC_ruc_neg(cursor.getString(2));
                aNeg.setC_tel_neg(cursor.getString(3));
                aNeg.setC_dir_neg(cursor.getString(4));
                aNeg.setN_num_canchas(cursor.getInt(5));
                aNeg.setC_nom_rep_neg(cursor.getString(6));
                aNeg.setC_tel_rep_neg(cursor.getString(7));
                aNeg.setN_lat_neg(cursor.getDouble(8));
                aNeg.setN_lon_neg(cursor.getDouble(9));
                aNeg.setN_comida_neg(cursor.getInt(10));
                aNeg.setN_cochera_neg(cursor.getInt(11));
                aNeg.setN_prec_min(cursor.getDouble(12));
                aNeg.setN_prec_max(cursor.getDouble(13));
                aNeg.setT_hora_ini(cursor.getString(14));
                aNeg.setT_hora_fin(cursor.getString(15));
                aNeg.setN_cod_dist(cursor.getInt(16));
                aNeg.setC_url_img(cursor.getString(17));
                aNeg.setC_est_neg(cursor.getString(18));

                // Add book to books
                negs.add(aNeg);

            } while (cursor.moveToNext());
        }

        //Cerramos el cursor
        cursor.close();

        // Devolvemos las notas encontradas o un array vacio en caso de que no se encuentre nada
        return negs;
    }
    //Actualizar los datos en una nota
    public int updateNegocio(DAONegocio neg) {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos el objeto ContentValues con las claves "columna"/valor
        // que se desean actualizar
        ContentValues values = new ContentValues();

        values.put(NegocioDBDef.NEGOCIO.RAZ_SOC_NEG_COL, neg.getC_raz_soc_neg()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.RUC_NEG_COL, neg.getC_ruc_neg()); // Descripción
        values.put(NegocioDBDef.NEGOCIO.TEL_NEG_COL, neg.getC_tel_neg()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.DIR_NEG_COL, neg.getC_dir_neg()); // Titulo
        //values.put(InscampoDBDef.INSCAMPO.TIPO_CERCO_COL, acta.getTipo_cerco()); // Descripción
        values.put(NegocioDBDef.NEGOCIO.NUM_CANCHAS_COL, neg.getN_num_canchas());
        values.put(NegocioDBDef.NEGOCIO.NOM_REP_COL, neg.getC_nom_rep_neg());
        values.put(NegocioDBDef.NEGOCIO.TEL_REP_NEG_COL, neg.getC_tel_rep_neg());
        values.put(NegocioDBDef.NEGOCIO.LAT_NEG_COL, neg.getN_lat_neg());
        values.put(NegocioDBDef.NEGOCIO.LON_NEG_COL, neg.getN_lon_neg());
        values.put(NegocioDBDef.NEGOCIO.COMIDA_NEG_COL, neg.getN_comida_neg());
        //values.put(InscampoDBDef.INSCAMPO.HABITACION_COL, acta.getHabitacion()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.COCHERA_NEG_COL, neg.getN_cochera_neg());
        values.put(NegocioDBDef.NEGOCIO.PREC_MIN_COL, neg.getN_prec_min());
        values.put(NegocioDBDef.NEGOCIO.PREC_MAX_COL, neg.getN_prec_max());
        values.put(NegocioDBDef.NEGOCIO.HORA_INI_COL, neg.getT_hora_ini());
        values.put(NegocioDBDef.NEGOCIO.HORA_FIN_COL, neg.getT_hora_fin());
        values.put(NegocioDBDef.NEGOCIO.COD_DIS_COL, neg.getN_cod_dist());
        //values.put(InscampoDBDef.INSCAMPO.TIPO_DE_TECHO_COL, acta.getTipo_de_techo()); // Titulo
        values.put(NegocioDBDef.NEGOCIO.URL_IMG_COL, neg.getC_url_img());
        values.put(NegocioDBDef.NEGOCIO.EST_NEG_COL, neg.getC_est_neg());


        // 3. Actualizamos el registro con el método update el cual devuelve la cantidad
        // de registros actualizados
        int i = db.update(NegocioDBDef.NEGOCIO.TABLE_NAME, //table
                values, // column/value
                NegocioDBDef.NEGOCIO.ID_COL+" = ?", // selections
                new String[] { String.valueOf(neg.getN_cod_neg()) }); //selection args

        // 4. Cerramos la conexión a la BD
        db.close();

        // Devolvemos la cantidad de registros actualizados
        return i;
    }

    // Borrar una Nota
    public void deleteNegocio(DAONegocio neg) {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Borramos el registro
        db.delete(NegocioDBDef.NEGOCIO.TABLE_NAME,
                NegocioDBDef.NEGOCIO.ID_COL+" = ?",
                new String[] { String.valueOf(neg.getN_cod_neg()) });

        // 3. Cerramos la conexión a la Bd
        db.close();
    }


    public void insertUsuario(DAOUsuario usu){
        //Con este método insertamos las notas nuevas que el usuario vaya creando

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos un obejto de tipo ContentValues para agregar los pares
        // de Claves de Columna y Valor
        ContentValues values = new ContentValues();
        values.put(NegocioDBDef.MUSUARIO.ID_USUARIO_COL, usu.getId_usuario()); // Titulo
        values.put(NegocioDBDef.MUSUARIO.NOMBRE_USUARIO_COL, usu.getNombre_usuario()); // Titulo
        values.put(NegocioDBDef.MUSUARIO.TOKEN_USUARIO_COL, usu.getToken_usuario()); // Descripción
        values.put(NegocioDBDef.MUSUARIO.EMAIL_USUARIO_COL, usu.getEmail_usuario()); // Titulo
        values.put(NegocioDBDef.MUSUARIO.FECHA_UPDATE_COL, usu.getFecha_update()); // Titulo

        // 3. Insertamos los datos en la tabla "notes"
        db.insert(NegocioDBDef.MUSUARIO.TABLE_NAME, null, values);

        // 4. Cerramos la conexión comn la BD
        db.close();
    }



    public DAOUsuario getUsuarioById(int id){
        // Declaramos un objeto Inscampo para instanciarlo con el resultado del query
        DAOUsuario aUsu = null;

        // 1. Obtenemos una reference de la BD con permisos de lectura
        SQLiteDatabase db = this.getReadableDatabase();

        //Definimos un array con los nombres de las columnas que deseamos sacar
        String[] COLUMNS = {
                NegocioDBDef.MUSUARIO.ID_USUARIO_COL,
                NegocioDBDef.MUSUARIO.NOMBRE_USUARIO_COL,
                NegocioDBDef.MUSUARIO.TOKEN_USUARIO_COL,
                NegocioDBDef.MUSUARIO.EMAIL_USUARIO_COL,
                NegocioDBDef.MUSUARIO.FECHA_UPDATE_COL,
        };


        // 2. Contruimos el query
        Cursor cursor =
                db.query(NegocioDBDef.NEGOCIO.TABLE_NAME,  //Nomre de la tabla
                        COLUMNS, // b. Nombre de las Columnas
                        " id_usuario = ?", // c. Columnas de la clausula WHERE
                        new String[] { String.valueOf(id) }, // d. valores de las columnas de la clausula WHERE
                        null, // e. Clausula Group by
                        null, // f. Clausula having
                        null, // g. Clausula order by
                        null); // h. Limte de regsitros

        // 3. Si hemos obtenido algun resultado entonces sacamos el primero de ellos ya que se supone
        //que ha de existir un solo registro para un id
        if (cursor != null) {
            cursor.moveToFirst();
            // 4. Contruimos el objeto Inscampo
            aUsu = new DAOUsuario();
            aUsu.setId_usuario(cursor.getString(0));
            aUsu.setNombre_usuario(cursor.getString(1));
            aUsu.setToken_usuario(cursor.getString(2));
            aUsu.setEmail_usuario(cursor.getString(3));
            aUsu.setFecha_update(cursor.getString(4));


        }

        // 5. Devolvemos le objeto Inscampo
        return aUsu;
    }


    public int updateUsuario(DAOUsuario usu) {

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos el objeto ContentValues con las claves "columna"/valor
        // que se desean actualizar
        ContentValues values = new ContentValues();

        values.put(NegocioDBDef.MUSUARIO.FECHA_UPDATE_COL, usu.getFecha_update()); // Titulo

        // 3. Actualizamos el registro con el método update el cual devuelve la cantidad
        // de registros actualizados
        int i = db.update(NegocioDBDef.NEGOCIO.TABLE_NAME, //table
                values, // column/value
                NegocioDBDef.MUSUARIO.ID_USUARIO_COL+" = ?", // selections
                new String[] { String.valueOf(usu.getId_usuario()) }); //selection args

        // 4. Cerramos la conexión a la BD
        db.close();

        // Devolvemos la cantidad de registros actualizados
        return i;
    }

    public List<DAOUsuario> getAllUsuarios() {
        //Instanciamos un Array para llenarlo con todos los objetos Notes que saquemos de la BD
        List<DAOUsuario> usus = new ArrayList<DAOUsuario>();

        // 1. Aramos un String con el query a ejecutar
        String query = "SELECT  * FROM " + NegocioDBDef.MUSUARIO.TABLE_NAME ;

        // 2. Obtenemos una reference de la BD con permisos de escritura y ejecutamos el query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Iteramos entre cada uno de olos registros y agregarlos al array de Notas
        DAOUsuario aUsu = null;
        if (cursor.moveToFirst()) {

            do {
                aUsu = new DAOUsuario();

                aUsu.setId_usuario(cursor.getString(0));
                aUsu.setNombre_usuario(cursor.getString(1));
                aUsu.setToken_usuario(cursor.getString(2));
                aUsu.setEmail_usuario(cursor.getString(3));
                aUsu.setFecha_update(cursor.getString(4));

                // Add book to books
                usus.add(aUsu);

            } while (cursor.moveToNext());
        }

        //Cerramos el cursor
        cursor.close();

        // Devolvemos las notas encontradas o un array vacio en caso de que no se encuentre nada
        return usus;
    }



    public void insertCancha(DAOCancha cancha){
        //Con este método insertamos las notas nuevas que el usuario vaya creando

        // 1. Obtenemos una reference de la BD con permisos de escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. Creamos un obejto de tipo ContentValues para agregar los pares
        // de Claves de Columna y Valor
        ContentValues values = new ContentValues();
        values.put(NegocioDBDef.DCANCHAS.ID_CANCHA, cancha.getN_cod_det_neg()); // Titulo
        values.put(NegocioDBDef.DCANCHAS.ID_NEGOCIO_COL, cancha.getN_cod_neg()); // Titulo
        values.put(NegocioDBDef.DCANCHAS.DESC_CANCHA_COL, cancha.getC_desc_cancha()); // Descripción
        values.put(NegocioDBDef.DCANCHAS.LARGO_COL, cancha.getN_largo_cancha()); // Titulo
        values.put(NegocioDBDef.DCANCHAS.ANCHO_COL, cancha.getN_ancho_cancha()); // Titulo
        values.put(NegocioDBDef.DCANCHAS.NUM_JUG_COL, cancha.getN_num_jug_recomen());
        values.put(NegocioDBDef.DCANCHAS.ESTADO_COL, cancha.getC_est_cancha());
        values.put(NegocioDBDef.DCANCHAS.COLOR_COL, cancha.getC_color_cancha());
        // 3. Insertamos los datos en la tabla "notes"
        db.insert(NegocioDBDef.DCANCHAS.TABLE_NAME, null, values);

        // 4. Cerramos la conexión comn la BD
        db.close();
    }


    public List<DAOCancha> getCanchasByNeg(int id_neg) {

        //Instanciamos un Array para llenarlo con todos los objetos Notes que saquemos de la BD
        List<DAOCancha> canchas = new ArrayList<DAOCancha>();

        // 1. Aramos un String con el query a ejecutar
        String query = "SELECT  * FROM " + NegocioDBDef.DCANCHAS.TABLE_NAME + " WHERE n_cod_neg = " + id_neg;

        // 2. Obtenemos una reference de la BD con permisos de escritura y ejecutamos el query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Iteramos entre cada uno de olos registros y agregarlos al array de Notas
        DAOCancha aCan = null;
        if (cursor.moveToFirst()) {

            do {
                aCan = new DAOCancha();

                aCan.setN_cod_det_neg(cursor.getInt(0));
                aCan.setN_cod_neg(cursor.getInt(1));
                aCan.setC_desc_cancha(cursor.getString(2));
                aCan.setN_ancho_cancha(cursor.getDouble(3));
                aCan.setN_largo_cancha(cursor.getDouble(4));
                aCan.setN_num_jug_recomen(cursor.getInt(5));
                aCan.setC_est_cancha(cursor.getString(6));
                aCan.setC_color_cancha(cursor.getString(7));

                // Add book to books
                canchas.add(aCan);

            } while (cursor.moveToNext());
        }

        //Cerramos el cursor
        cursor.close();

        // Devolvemos las notas encontradas o un array vacio en caso de que no se encuentre nada
        return canchas;
    }

    public List<DAOCancha> getAllCanchas() {
        //Instanciamos un Array para llenarlo con todos los objetos Notes que saquemos de la BD
        List<DAOCancha> canchas = new ArrayList<DAOCancha>();

        // 1. Aramos un String con el query a ejecutar
        String query = "SELECT  * FROM " + NegocioDBDef.DCANCHAS.TABLE_NAME ;

        // 2. Obtenemos una reference de la BD con permisos de escritura y ejecutamos el query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. Iteramos entre cada uno de olos registros y agregarlos al array de Notas
        DAOCancha aCancha = null;
        if (cursor.moveToFirst()) {

            do {
                aCancha = new DAOCancha();

                aCancha.setN_cod_det_neg(cursor.getInt(0));
                aCancha.setN_cod_neg(cursor.getInt(1));
                aCancha.setC_desc_cancha(cursor.getString(2));
                aCancha.setN_largo_cancha(cursor.getDouble(3));
                aCancha.setN_ancho_cancha(cursor.getDouble(4));
                aCancha.setN_num_jug_recomen(cursor.getInt(5));
                aCancha.setC_est_cancha(cursor.getString(6));
                aCancha.setC_color_cancha(cursor.getString(7));


                // Add book to books
                canchas.add(aCancha);

            } while (cursor.moveToNext());
        }

        //Cerramos el cursor
        cursor.close();

        // Devolvemos las notas encontradas o un array vacio en caso de que no se encuentre nada
        return canchas;
    }


}
