package com.ibao.alanger.agroq.models.vo.entitiesInternal;

public class LoginDataVO {

    private int id;
    private String codigo;

    private String user;
    private String password;

    private String name;
    private String lastName;

    private int idTipoProceso;
    private String nameTypeProcess;

    private String ListIdTipoProcesos;

    private int idCultivo;

    private int idPlanta;

    public int getId() {
        return id;
    }

    public int getIdTipoProceso() {
        return idTipoProceso;
    }

    public void setIdTipoProceso(int idTipoProceso) {
        this.idTipoProceso = idTipoProceso;
    }

    public String getNameTypeProcess() {
        return nameTypeProcess;
    }

    public void setNameTypeProcess(String nameTypeProcess) {
        this.nameTypeProcess = nameTypeProcess;
    }

    public String getListIdTipoProcesos() {
        return ListIdTipoProcesos;
    }

    public void setListIdTipoProcesos(String listIdTipoProcesos) {
        ListIdTipoProcesos = listIdTipoProcesos;
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

    public int getIdCultivo() {
        return idCultivo;
    }

    public void setIdCultivo(int idCultivo) {
        this.idCultivo = idCultivo;
    }

    public int getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(int idPlanta) {
        this.idPlanta = idPlanta;
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
