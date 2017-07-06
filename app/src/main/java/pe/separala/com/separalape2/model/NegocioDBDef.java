package pe.separala.com.separalape2.model;

/**
 * Created by ExpoCode Tech http://expocodetech.com
 */
public class NegocioDBDef {
    //Nombre del esquema de Base de Datos
    public static final String DATABASE_NAME = "separala";
    //Version de la Base de Datos (Este parámetro es importante  )
    public static final int DATABASE_VERSION = 1;

    //Una clase estatica en la que se definen las propiedaes que determinan la tabla Notes
    // y sus 4 columnas
    public static class NEGOCIO {
        //Nombre de la tabla
        public static final String TABLE_NAME = "MNEGOCIO";
        //Nombre de las Columnas que contiene la tabla
        public static final String ID_COL = "n_cod_neg";
        public static final String RAZ_SOC_NEG_COL = "c_raz_soc_neg";
        public static final String RUC_NEG_COL = "c_ruc_neg";
        public static final String TEL_NEG_COL = "c_tel_neg";
        public static final String DIR_NEG_COL = "c_dir_neg";
        public static final String NUM_CANCHAS_COL = "n_num_canchas";
        public static final String NOM_REP_COL = "c_nom_rep_neg";
        //public static final String TIPO_CERCO_COL = "tipo_cerco";
        public static final String TEL_REP_NEG_COL = "c_tel_rep_neg";
        public static final String LAT_NEG_COL= "n_lat_neg";
        public static final String LON_NEG_COL = "n_lon_neg";
        public static final String COMIDA_NEG_COL = "n_comida_neg";
        public static final String COCHERA_NEG_COL = "n_cochera_neg";
        public static final String PREC_MIN_COL = "n_prec_min";
        //public static final String HABITACION_COL = "habitación";
        public static final String PREC_MAX_COL = "n_prec_max";
        public static final String HORA_INI_COL = "t_hora_ini";
        public static final String HORA_FIN_COL = "t_hora_fin";
        public static final String COD_DIS_COL = "n_cod_dist";
        public static final String URL_IMG_COL = "c_url_img";
        public static final String EST_NEG_COL = "c_est_neg";

    }

    public static class MUSUARIO {
        //Nombre de la tabla
        public static final String TABLE_NAME = "MUSUARIO";
        //Nombre de las Columnas que contiene la tabla
        public static final String ID_USUARIO_COL = "id_usuario";
        public static final String NOMBRE_USUARIO_COL = "nombre_usuario";
        public static final String TOKEN_USUARIO_COL = "token_usu";
        public static final String EMAIL_USUARIO_COL = "email_usu";
        public static final String FECHA_UPDATE_COL = "fecha_update";

    }

    public static class DCANCHAS {
        //Nombre de la tabla
        public static final String TABLE_NAME = "DCANCHAS";
        //Nombre de las Columnas que contiene la tabla
        public static final String ID_CANCHA = "n_cod_det_neg";
        public static final String ID_NEGOCIO_COL = "n_cod_neg";
        public static final String DESC_CANCHA_COL = "c_desc_cancha";
        public static final String LARGO_COL= "n_largo_cancha";
        public static final String ANCHO_COL = "n_ancho_cancha";
        public static final String NUM_JUG_COL = "n_num_jug_recomen";
        public static final String ESTADO_COL = "c_est_cancha";
        public static final String COLOR_COL = "c_color_cancha";


    }

    //Setencia SQL que permite crear la tabla Notes
    public static final String MNEGOCIO_TABLE_CREATE =
            "CREATE TABLE " + NEGOCIO.TABLE_NAME + " (" +
                    NEGOCIO.ID_COL + " INTEGER PRIMARY KEY, " +
                    NEGOCIO.RAZ_SOC_NEG_COL + " TEXT, " +
                    NEGOCIO.RUC_NEG_COL + " TEXT, " +
                    NEGOCIO.TEL_NEG_COL + " TEXT, " +
                    NEGOCIO.DIR_NEG_COL + " TEXT, " +
                    NEGOCIO.NUM_CANCHAS_COL + " INTEGER, " +
                    NEGOCIO.NOM_REP_COL + " TEXT, " +
                   // INSCAMPO.TIPO_CERCO_COL + " INTEGER, " +
                    NEGOCIO.TEL_REP_NEG_COL + " TEXT, " +
                    NEGOCIO.LAT_NEG_COL + " REAL, " +
                    NEGOCIO.LON_NEG_COL + " REAL, " +
                    NEGOCIO.COMIDA_NEG_COL + " INTEGER, " +
                    NEGOCIO.COCHERA_NEG_COL + " INTEGER, " +
                    NEGOCIO.PREC_MIN_COL + " REAL, " +
                    //INSCAMPO.HABITACION_COL + " INTEGER, " +
                    NEGOCIO.PREC_MAX_COL + " REAL, " +
                    NEGOCIO.HORA_INI_COL + " TEXT, " +
                    NEGOCIO.HORA_FIN_COL + " TEXT, " +
                    NEGOCIO.COD_DIS_COL + " INTEGER, " +
                    NEGOCIO.URL_IMG_COL + " TEXT, " +
                    NEGOCIO.EST_NEG_COL + " TEXT);";

    public static final String MUSUARIO_TABLE_CREATE =
            "CREATE TABLE " + MUSUARIO.TABLE_NAME + " (" +
                    MUSUARIO.ID_USUARIO_COL+ " TEXT PRIMARY KEY, " +
                    MUSUARIO.NOMBRE_USUARIO_COL + " TEXT, " +
                    MUSUARIO.TOKEN_USUARIO_COL + " TEXT, " +
                    MUSUARIO.EMAIL_USUARIO_COL+ " TEXT, " +
                    MUSUARIO.FECHA_UPDATE_COL + " TEXT);";

    public static final String DCANCHAS_TABLE_CREATE =
            "CREATE TABLE " + DCANCHAS.TABLE_NAME + " (" +
                    DCANCHAS.ID_CANCHA+ " INTEGER PRIMARY KEY, " +
                    DCANCHAS.ID_NEGOCIO_COL + " INTEGER, " +
                    DCANCHAS.DESC_CANCHA_COL + " TEXT, " +
                    DCANCHAS.LARGO_COL+ " REAL, " +
                    DCANCHAS.ANCHO_COL+ " REAL, " +
                    DCANCHAS.NUM_JUG_COL+ " INTEGER, " +
                    DCANCHAS.ESTADO_COL+ " TEXT, " +
                    DCANCHAS.COLOR_COL + " TEXT);";

    //Setencia SQL que permite eliminar la tabla Notes
    public static final String NEGOCIO_TABLE_DROP = "DROP TABLE IF EXISTS " + NEGOCIO.TABLE_NAME;

    public static final String MUSUARIO_TABLE_DROP = "DROP TABLE IF EXISTS " + MUSUARIO.TABLE_NAME;

    public static final String DCANCHAS_TABLE_DROP = "DROP TABLE IF EXISTS " + DCANCHAS.TABLE_NAME;
}
