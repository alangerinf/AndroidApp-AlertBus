package ibao.alanger.alertbus.models.vo.entitiesDB;

public class CalibreVO {
    private int id;
    private String name;
    private String pesoMin;
    private String pesoMax;
    private int idTipoCalibre;
    public CalibreVO(){
    }

    public String getPesoMin() {
        return pesoMin;
    }

    public void setPesoMin(String pesoMin) {
        this.pesoMin = pesoMin;
    }

    public String getPesoMax() {
        return pesoMax;
    }

    public void setPesoMax(String pesoMax) {
        this.pesoMax = pesoMax;
    }

    public int getIdTipoCalibre() {
        return idTipoCalibre;
    }

    public void setIdTipoCalibre(int idTipoCalibre) {
        this.idTipoCalibre = idTipoCalibre;
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
}
