package ibao.alanger.alertbus.models.vo.entitiesDB;

public class CategoriaVO {
    private int id;
    private String name;

    public CategoriaVO(){
    }

    public CategoriaVO(int id, String name) {
        this.id = id;
        this.name = name;
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
