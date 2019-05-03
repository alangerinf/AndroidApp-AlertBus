package ibao.alanger.alertbus.models.vo;

public class PasajeroVO {
    private int id;
    private String name;
    private String dni;
    private int idViaje;

    public PasajeroVO(){
    }

    public PasajeroVO(int id, String name,String dni) {
        this.id = id;
        this.name = name;
        this.dni = dni;
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
