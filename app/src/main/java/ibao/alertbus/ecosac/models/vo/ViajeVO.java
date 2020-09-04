package ibao.alertbus.ecosac.models.vo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private int capacidad;
    private String empresa;
    private String proveedor;
    private String comentario;
    private int status;
    private String conductor;
    private String hProgramada;
    private int numPasajerosRegistrados;
    private int numRestriccionesRegistradas;
    private String hConfirmado;

    List<PasajeroVO> pasajeroVOList;
    List<RestriccionVO> restriccionVOList;

    public ViajeVO(){
        id=0;
        numPasajerosRegistrados=0;
        idWeb=0;
        hInicio="";
        hFin="";
        placa="";
        ruta="";

        hProgramada="";
        capacidad=0;
        empresa="";
        proveedor="";
        comentario="";
        status=0;
        conductor="";
        pasajeroVOList = new ArrayList<>();
        restriccionVOList = new ArrayList<>();

    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

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

    public String gethProgramada() {
        return hProgramada;
    }

    public void sethProgramada(String hProgramada) {
        this.hProgramada = hProgramada;
    }

    public int getNumPasajerosRegistrados() {
        return numPasajerosRegistrados;
    }

    public void setNumPasajerosRegistrados(int numPasajerosRegistrados) {
        this.numPasajerosRegistrados = numPasajerosRegistrados;
    }

    public int getNumRestriccionesRegistradas() {
        return numRestriccionesRegistradas;
    }

    public void setNumRestriccionesRegistradas(int numRestriccionesRegistradas) {
        this.numRestriccionesRegistradas = numRestriccionesRegistradas;
    }

    public String toString(){

        new Gson().toJson(
                this,
                new TypeToken<ViajeVO>() {}.getType());
        return new Gson().toJson(this);
    }

    public String toStringQR(){

        ViajeVO vo = new ViajeVO();

        vo.setId(this.id);
        vo.setIdWeb(this.idWeb);
        vo.sethInicio(this.gethInicio());
        vo.sethFin(this.gethFin());
        vo.setPlaca(this.getPlaca());
        vo.setRuta(this.getRuta());
        vo.sethProgramada(this.gethProgramada());
        vo.setCapacidad(this.getCapacidad());
        vo.setEmpresa(this.getEmpresa());
        vo.setProveedor(this.getProveedor());
        vo.setConductor(this.getConductor());
        vo.setNumPasajerosRegistrados(this.getPasajeroVOList().size());
        vo.setNumRestriccionesRegistradas(this.getRestriccionVOList().size());

        new Gson().toJson(
                this,
                new TypeToken<ViajeVO>() {}.getType());
        return new Gson().toJson(vo);

    }

    public String getEmpresa() {
        return this.empresa;
    }

    int trasbordo = 0;
    public void setTrasbordo(int i) {
        trasbordo = i;
    }
}
