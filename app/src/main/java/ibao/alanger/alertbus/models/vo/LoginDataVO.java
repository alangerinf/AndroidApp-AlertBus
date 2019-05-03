package ibao.alanger.alertbus.models.vo;

public class LoginDataVO {

    private int id;
    private String codigo;

    private String user;
    private String password;

    private String name;
    private String lastName;



    public int getId() {
        return id;
    }


    public String getCodigo() {
        return codigo;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
