package pe.separala.com.separalape2.model;

import java.io.Serializable;

/**
 * Created by Alexander on 25/05/2017.
 */
@SuppressWarnings("serial")
public class DAOEventos implements Serializable{
    private int n_cod_even;
    private int n_cod_neg;
    private int n_cod_cancha;
    private String c_desc_even;
    private String fecha_inicio;
    private String fecha_fin;
    private String color;
    private String icon;

    public DAOEventos(int n_cod_even, int n_cod_neg, int n_cod_cancha, String c_desc_even, String fecha_inicio, String fecha_fin, String color, String icon) {
        this.n_cod_even = n_cod_even;
        this.n_cod_neg = n_cod_neg;
        this.n_cod_cancha = n_cod_cancha;
        this.c_desc_even = c_desc_even;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.color = color;
        this.icon = icon;
    }

    public int getN_cod_even() {
        return n_cod_even;
    }

    public void setN_cod_even(int n_cod_even) {
        this.n_cod_even = n_cod_even;
    }

    public int getN_cod_neg() {
        return n_cod_neg;
    }

    public void setN_cod_neg(int n_cod_neg) {
        this.n_cod_neg = n_cod_neg;
    }

    public int getN_cod_cancha() {
        return n_cod_cancha;
    }

    public void setN_cod_cancha(int n_cod_cancha) {
        this.n_cod_cancha = n_cod_cancha;
    }

    public String getC_desc_even() {
        return c_desc_even;
    }

    public void setC_desc_even(String c_desc_even) {
        this.c_desc_even = c_desc_even;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
