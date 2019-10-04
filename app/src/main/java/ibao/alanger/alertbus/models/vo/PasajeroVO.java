package ibao.alanger.alertbus.models.vo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

public class PasajeroVO implements Serializable {
    private int id;
    private String name;
    private String dni;
    private int idViaje;
    private String hSubida;
    private String hBajada;
    private String observacion;

    public PasajeroVO(){
        this.id=0;
        this.name="";
        this.dni="";
        this.idViaje=0;
        this.hSubida="";
        this.hBajada="";
        this.observacion="";
    }

    public String gethSubida() {
        return hSubida;
    }

    public void sethSubida(String hSubida) {
        this.hSubida = hSubida;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String gethBajada() {
        return hBajada;
    }

    public void sethBajada(String hBajada) {
        this.hBajada = hBajada;
    }

    public String toString(){

        new Gson().toJson(
                this,
                new TypeToken<PasajeroVO>() {}.getType());
        return new Gson().toJson(this);
    }

}
