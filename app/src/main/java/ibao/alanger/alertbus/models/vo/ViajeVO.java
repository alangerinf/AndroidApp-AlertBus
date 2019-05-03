package ibao.alanger.alertbus.models.vo;

public class ViajeVO {
    private int id;
    private String hInicio;
    private String hFin;
    private String placa;
    private String ruta;
    private int numpasajeros;
    private float porCapacidad;
    private String proveedor;
    private String comentario;
    private int status;

    public ViajeVO(){
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

    public int getNumpasajeros() {
        return numpasajeros;
    }

    public void setNumpasajeros(int numpasajeros) {
        this.numpasajeros = numpasajeros;
    }

    public float getPorCapacidad() {
        return porCapacidad;
    }

    public void setPorCapacidad(float porCapacidad) {
        this.porCapacidad = porCapacidad;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
