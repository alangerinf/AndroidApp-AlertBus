package ibao.alanger.alertbus.models.vo;

import java.util.ArrayList;
import java.util.List;

public class ViajeVO {
    private int id;
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

    public ViajeVO(){
        id=0;
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
}
