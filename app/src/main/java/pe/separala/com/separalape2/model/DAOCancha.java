package pe.separala.com.separalape2.model;

import java.io.Serializable;

/**
 * Created by Alxndr on 03/07/2017.
 */

@SuppressWarnings("serial")
public class DAOCancha implements  Serializable {
    private int n_cod_det_neg;
    private int n_cod_neg;
    private String c_desc_cancha;
    private double n_largo_cancha;
    private double n_ancho_cancha;
    private int n_num_jug_recomen;
    private String c_est_cancha;
    private String c_color_cancha;

    public DAOCancha(){

    }

    public DAOCancha(int n_cod_det_neg, int n_cod_neg, String c_desc_cancha, double n_largo_cancha, double n_ancho_cancha, int n_num_jug_recomen, String c_est_cancha, String c_color_cancha) {
        this.n_cod_det_neg = n_cod_det_neg;
        this.n_cod_neg = n_cod_neg;
        this.c_desc_cancha = c_desc_cancha;
        this.n_largo_cancha = n_largo_cancha;
        this.n_ancho_cancha = n_ancho_cancha;
        this.n_num_jug_recomen = n_num_jug_recomen;
        this.c_est_cancha = c_est_cancha;
        this.c_color_cancha = c_color_cancha;
    }

    public int getN_cod_det_neg() {
        return n_cod_det_neg;
    }

    public void setN_cod_det_neg(int n_cod_det_neg) {
        this.n_cod_det_neg = n_cod_det_neg;
    }

    public int getN_cod_neg() {
        return n_cod_neg;
    }

    public void setN_cod_neg(int n_cod_neg) {
        this.n_cod_neg = n_cod_neg;
    }

    public String getC_desc_cancha() {
        return c_desc_cancha;
    }

    public void setC_desc_cancha(String c_desc_cancha) {
        this.c_desc_cancha = c_desc_cancha;
    }

    public double getN_largo_cancha() {
        return n_largo_cancha;
    }

    public void setN_largo_cancha(double n_largo_cancha) {
        this.n_largo_cancha = n_largo_cancha;
    }

    public double getN_ancho_cancha() {
        return n_ancho_cancha;
    }

    public void setN_ancho_cancha(double n_ancho_cancha) {
        this.n_ancho_cancha = n_ancho_cancha;
    }

    public int getN_num_jug_recomen() {
        return n_num_jug_recomen;
    }

    public void setN_num_jug_recomen(int n_num_jug_recomen) {
        this.n_num_jug_recomen = n_num_jug_recomen;
    }

    public String getC_est_cancha() {
        return c_est_cancha;
    }

    public void setC_est_cancha(String c_est_cancha) {
        this.c_est_cancha = c_est_cancha;
    }

    public String getC_color_cancha() {
        return c_color_cancha;
    }

    public void setC_color_cancha(String c_color_cancha) {
        this.c_color_cancha = c_color_cancha;
    }
}
