package ibao.alertbus.ecosac.models.vo;


public class TrackingVO {
    private String latitud;
    private String longitud;
    private String dateTime;
    private String speed;
    private String bearing;
    private boolean isUpdate;
    public TrackingVO(){
        latitud="";
        longitud="";
        dateTime="";
        speed="";
        bearing="";
        isUpdate=false;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }
}
