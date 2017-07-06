package pe.separala.com.separalape2.model;

import java.io.Serializable;

/**
 * Created by ExpoCode Tech http://expocodetech.com
 */
@SuppressWarnings("serial")
public class DAOUsuario implements Serializable {
    private String id_usuario;
    private String nombre_usuario;
    private String token_usuario;
    private String email_usuario;
    private String fecha_update;

    public DAOUsuario(){

    }

    public DAOUsuario(String id_usuario, String nombre_usuario, String token_usuario, String email_usuario, String fecha_update) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.token_usuario = token_usuario;
        this.email_usuario = email_usuario;
        this.fecha_update = fecha_update;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getToken_usuario() {
        return token_usuario;
    }

    public void setToken_usuario(String token_usuario) {
        this.token_usuario = token_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public String getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(String fecha_update) {
        this.fecha_update = fecha_update;
    }
}
