package ibao.alanger.alertbus.models.vo;

import java.io.Serializable;

public class RestriccionVO implements Serializable {
    private int id;
    private String name;
    private String desc;

    private int idViaje;
    public RestriccionVO(){
        this.id=0;
        this.name="";
        this.desc="";
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }
}
