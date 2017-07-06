package pe.separala.com.separalape2.model;

import java.io.Serializable;

/**
 * Created by Alexander on 21/04/2017.
 */

@SuppressWarnings("serial")
public class DAOUbicacion implements Serializable{

    private int nCodUbi;
    private int nCodNeg;
    private Double lat;
    private Double lon;

    public DAOUbicacion(int nCodUbi, int nCodNeg, Double lat, Double lon) {
        this.nCodUbi = nCodUbi;
        this.nCodNeg = nCodNeg;
        this.lat = lat;
        this.lon = lon;
    }


    public int getnCodUbi() {
        return nCodUbi;
    }

    public void setnCodUbi(int nCodUbi) {
        this.nCodUbi = nCodUbi;
    }

    public int getnCodNeg() {
        return nCodNeg;
    }

    public void setnCodNeg(int nCodNeg) {
        this.nCodNeg = nCodNeg;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
