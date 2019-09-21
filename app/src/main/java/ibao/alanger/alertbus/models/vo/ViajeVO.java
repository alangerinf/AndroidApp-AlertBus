package ibao.alanger.alertbus.models.vo;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViajeVO implements Serializable {
    private int id;//idPlanificion
    private int idWeb;//idViaje
    private String hInicio;
    private String hFin;
    private String placa;
    private String ruta;
    private int numPasajeros;
    private int capacidad;
    private String proveedor;
    private String comentario;
    private int status;
    private String conductor;
    private int numRestricciones;
    List<PasajeroVO> pasajeroVOList;
    List<RestriccionVO> restriccionVOList;

    public ViajeVO(){
        id=0;
        idWeb=0;
        hInicio="";
        hFin="";
        placa="";
        ruta="";
        numPasajeros=0;
        capacidad=0;
        proveedor="";
        comentario="";
        status=0;
        conductor="";
        numRestricciones=0;
        pasajeroVOList = new ArrayList<>();
        restriccionVOList = new ArrayList<>();

    }

    public int getNumRestricciones() {
        return numRestricciones;
    }

    public void setNumRestricciones(int numRestricciones) {
        this.numRestricciones = numRestricciones;
    }

    private String hConfirmado;


    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String gethInicio() {
        return hInicio;
    }

    public void sethInicio(String hInicio) {
        this.hInicio = hInicio;
    }

    public String gethFin() {
        return hFin;
    }

    public void sethFin(String hFin) {
        this.hFin = hFin;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getNumPasajeros() {
        return numPasajeros;
    }

    public void setNumPasajeros(int numPasajeros) {
        this.numPasajeros = numPasajeros;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String gethConfirmado() {
        return hConfirmado;
    }

    public void sethConfirmado(String hConfirmado) {
        this.hConfirmado = hConfirmado;
    }

    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public List<PasajeroVO> getPasajeroVOList() {
        return pasajeroVOList;
    }

    public void setPasajeroVOList(List<PasajeroVO> pasajeroVOList) {
        this.pasajeroVOList = pasajeroVOList;
    }

    public List<RestriccionVO> getRestriccionVOList() {
        return restriccionVOList;
    }

    public void setRestriccionVOList(List<RestriccionVO> restriccionVOList) {
        this.restriccionVOList = restriccionVOList;
    }

    public int getIdWeb() {
        return idWeb;
    }

    public void setIdWeb(int idWeb) {
        this.idWeb = idWeb;
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
