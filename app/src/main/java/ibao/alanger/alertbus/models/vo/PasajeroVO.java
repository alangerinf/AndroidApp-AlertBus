package ibao.alanger.alertbus.models.vo;

public class PasajeroVO {
    private int id;
    private String name;
    private String dni;
    private int idViaje;
    private String hSubida;
    private String observacion;

    public PasajeroVO(){
        this.id=0;
        this.name="";
        this.dni="";
        this.idViaje=0;
        this.hSubida="";
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
}
