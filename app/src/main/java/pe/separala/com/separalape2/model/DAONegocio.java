package pe.separala.com.separalape2.model;

import java.io.Serializable;

/**
 * Created by Alexander on 21/04/2017.
 */

@SuppressWarnings("serial")
public class DAONegocio implements Serializable{

    private int n_cod_neg;
    private String c_raz_soc_neg;
    private String c_ruc_neg;
    private String c_tel_neg;
    private String c_dir_neg;
    private int n_num_canchas;
    private String c_nom_rep_neg;
    private String c_tel_rep_neg;
    private Double n_lat_neg;
    private Double n_lon_neg;
    private int n_comida_neg;
    private int n_cochera_neg;
    private Double n_prec_min;
    private Double n_prec_max;
    private String t_hora_ini;
    private String t_hora_fin;
    private int n_cod_dist;
    private String c_url_img;
    private String c_est_neg;

    public DAONegocio(){

    }

    public DAONegocio(int n_cod_neg, String c_raz_soc_neg, String c_ruc_neg, String c_tel_neg, String c_dir_neg, int n_num_canchas, String c_nom_rep_neg, String c_tel_rep_neg, Double n_lat_neg, Double n_lon_neg, int n_comida_neg, int n_cochera_neg, Double n_prec_min, Double n_prec_max, String t_hora_ini, String t_hora_fin, int n_cod_dist, String c_url_img, String c_est_neg) {
        this.n_cod_neg = n_cod_neg;
        this.c_raz_soc_neg = c_raz_soc_neg;
        this.c_ruc_neg = c_ruc_neg;
        this.c_tel_neg = c_tel_neg;
        this.c_dir_neg = c_dir_neg;
        this.n_num_canchas = n_num_canchas;
        this.c_nom_rep_neg = c_nom_rep_neg;
        this.c_tel_rep_neg = c_tel_rep_neg;
        this.n_lat_neg = n_lat_neg;
        this.n_lon_neg = n_lon_neg;
        this.n_comida_neg = n_comida_neg;
        this.n_cochera_neg = n_cochera_neg;
        this.n_prec_min = n_prec_min;
        this.n_prec_max = n_prec_max;
        this.t_hora_ini = t_hora_ini;
        this.t_hora_fin = t_hora_fin;
        this.n_cod_dist = n_cod_dist;
        this.c_url_img = c_url_img;
        this.c_est_neg = c_est_neg;
    }

    public int getN_cod_neg() {
        return n_cod_neg;
    }

    public void setN_cod_neg(int n_cod_neg) {
        this.n_cod_neg = n_cod_neg;
    }

    public String getC_raz_soc_neg() {
        return c_raz_soc_neg;
    }

    public void setC_raz_soc_neg(String c_raz_soc_neg) {
        this.c_raz_soc_neg = c_raz_soc_neg;
    }

    public String getC_ruc_neg() {
        return c_ruc_neg;
    }

    public void setC_ruc_neg(String c_ruc_neg) {
        this.c_ruc_neg = c_ruc_neg;
    }

    public String getC_tel_neg() {
        return c_tel_neg;
    }

    public void setC_tel_neg(String c_tel_neg) {
        this.c_tel_neg = c_tel_neg;
    }

    public String getC_dir_neg() {
        return c_dir_neg;
    }

    public void setC_dir_neg(String c_dir_neg) {
        this.c_dir_neg = c_dir_neg;
    }

    public int getN_num_canchas() {
        return n_num_canchas;
    }

    public void setN_num_canchas(int n_num_canchas) {
        this.n_num_canchas = n_num_canchas;
    }

    public String getC_nom_rep_neg() {
        return c_nom_rep_neg;
    }

    public void setC_nom_rep_neg(String c_nom_rep_neg) {
        this.c_nom_rep_neg = c_nom_rep_neg;
    }

    public String getC_tel_rep_neg() {
        return c_tel_rep_neg;
    }

    public void setC_tel_rep_neg(String c_tel_rep_neg) {
        this.c_tel_rep_neg = c_tel_rep_neg;
    }

    public Double getN_lat_neg() {
        return n_lat_neg;
    }

    public void setN_lat_neg(Double n_lat_neg) {
        this.n_lat_neg = n_lat_neg;
    }

    public Double getN_lon_neg() {
        return n_lon_neg;
    }

    public void setN_lon_neg(Double n_lon_neg) {
        this.n_lon_neg = n_lon_neg;
    }

    public int getN_comida_neg() {
        return n_comida_neg;
    }

    public void setN_comida_neg(int n_comida_neg) {
        this.n_comida_neg = n_comida_neg;
    }

    public int getN_cochera_neg() {
        return n_cochera_neg;
    }

    public void setN_cochera_neg(int n_cochera_neg) {
        this.n_cochera_neg = n_cochera_neg;
    }

    public Double getN_prec_min() {
        return n_prec_min;
    }

    public void setN_prec_min(Double n_prec_min) {
        this.n_prec_min = n_prec_min;
    }

    public Double getN_prec_max() {
        return n_prec_max;
    }

    public void setN_prec_max(Double n_prec_max) {
        this.n_prec_max = n_prec_max;
    }

    public String getT_hora_ini() {
        return t_hora_ini;
    }

    public void setT_hora_ini(String t_hora_ini) {
        this.t_hora_ini = t_hora_ini;
    }

    public String getT_hora_fin() {
        return t_hora_fin;
    }

    public void setT_hora_fin(String t_hora_fin) {
        this.t_hora_fin = t_hora_fin;
    }

    public int getN_cod_dist() {
        return n_cod_dist;
    }

    public void setN_cod_dist(int n_cod_dist) {
        this.n_cod_dist = n_cod_dist;
    }

    public String getC_url_img() {
        return c_url_img;
    }

    public void setC_url_img(String c_url_img) {
        this.c_url_img = c_url_img;
    }

    public String getC_est_neg() {
        return c_est_neg;
    }

    public void setC_est_neg(String c_est_neg) {
        this.c_est_neg = c_est_neg;
    }
}
