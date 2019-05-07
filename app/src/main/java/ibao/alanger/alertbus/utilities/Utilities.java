package ibao.alanger.alertbus.utilities;


public class Utilities {

    private static String TAG = Utilities.class.getSimpleName();

    public static final String URL_ROOT="http://35.167.15.182/AlertBus/EcoSac/Requests/";
    public static final String URL_AUTENTIFICATION=URL_ROOT+"autenticar.php";
    public static final String URL_BUSCARNUEVOS=URL_ROOT+"getViajes.php";

    public static final String URL_UPLOAD_CONFIRMARVIAJE=URL_ROOT+"insertFotos.php";

    public static final String DATABASE_NAME="data";

    public static final String TABLE_LOGINDATA="loginData",
            TABLE_LOGINDATA_COL_IDUSER          ="idUser",
            TABLE_LOGINDATA_TYPECOL_IDUSER      ="INTEGER",
            TABLE_LOGINDATA_COL_USER            ="user",
            TABLE_LOGINDATA_TYPECOL_USER        ="varchar(50)",
            TABLE_LOGINDATA_COL_PASSWORD        ="password",
            TABLE_LOGINDATA_TYPECOL_PASSWORD    ="varchar(50)",
            TABLE_LOGINDATA_COL_NAME            ="name",
            TABLE_LOGINDATA_TYPECOL_NAME        ="varchar(50)",
            TABLE_LOGINDATA_COL_LASTNAME        ="lastname",
            TABLE_LOGINDATA_TYPECOL_LASTNAME    ="varchar(50)";
    
    public static final String TABLE_PASAJERO   ="pasajero",
            TABLE_PASAJERO_COL_ID               ="idUser",
            TABLE_PASAJERO_TYPECOL_ID           ="INTEGER",
            TABLE_PASAJERO_COL_DNI              ="dni",
            TABLE_PASAJERO_TYPECOL_DNI          ="varchar(50)",
            TABLE_PASAJERO_COL_NAME             ="name",
            TABLE_PASAJERO_TYPECOL_NAME         ="varchar(50)",
            TABLE_PASAJERO_COL_IDVIAJE          ="idViaje",
            TABLE_PASAJERO_TYPECOL_IDVIAJE      ="INTEGER";
    
    
    public static final String TABLE_VIAJE ="viaje",
            TABLE_VIAJE_COL_ID                  ="id",
            TABLE_VIAJE_TYPECOL_ID              ="INTEGER",
            TABLE_VIAJE_COL_PROVEEDOR           ="proveedor",
            TABLE_VIAJE_TYPECOL_PROVEEDOR       ="VARCHAR(50)",
            TABLE_VIAJE_COL_PLACA               ="placa",
            TABLE_VIAJE_TYPECOL_PLACA           ="VARCHAR(50)",
            TABLE_VIAJE_COL_CONDUCTOR           ="conductor",
            TABLE_VIAJE_TYPECOL_CONDUCTOR       ="varchar(50)",
            TABLE_VIAJE_COL_CAPACIDAD           ="capacidad",
            TABLE_VIAJE_TYPECOL_CAPACIDAD       ="INTEGER",
            TABLE_VIAJE_COL_NUMPASAJEROS        ="numPasajeros",
            TABLE_VIAJE_TYPECOL_NUMPASAJEROS    ="INTEGER",
            TABLE_VIAJE_COL_RUTA                ="ruta",
            TABLE_VIAJE_TYPECOL_RUTA            ="VARCHAR(50)",
            TABLE_VIAJE_COL_HORAINICIO          ="horaInicio",
            TABLE_VIAJE_TYPECOL_HORAINICIO      ="VARCHAR(50)",
            TABLE_VIAJE_COL_HORAFIN             ="horaFin",
            TABLE_VIAJE_TYPECOL_HORAFIN         ="VARCHAR(50)",
            TABLE_VIAJE_COL_COMENTARIO          ="comentario",
            TABLE_VIAJE_TYPECOL_COMENTARIO      ="VARCHAR(200)",
            TABLE_VIAJE_COL_STATUS              ="status", // 0:recibido, 1:verificado, 2:sincronizado
            TABLE_VIAJE_TYPECOL_STATUS          ="INTEGER";

    //SCRIPTS SQL CREATE TABLES
    public static final String CREATE_TABLE_VIAJE =
            " CREATE TABLE IF NOT EXISTS "+ TABLE_VIAJE +" ("+
                    TABLE_VIAJE_COL_ID          +" "+TABLE_VIAJE_TYPECOL_ID +" PRIMARY KEY , "+
                    TABLE_VIAJE_COL_PROVEEDOR   +" "+TABLE_VIAJE_TYPECOL_PROVEEDOR +" , "+
                    TABLE_VIAJE_COL_PLACA       +" "+TABLE_VIAJE_TYPECOL_PLACA +" , "+
                    TABLE_VIAJE_COL_CONDUCTOR   +" "+TABLE_VIAJE_TYPECOL_CONDUCTOR +", "+
                    TABLE_VIAJE_COL_CAPACIDAD +" "+ TABLE_VIAJE_TYPECOL_CAPACIDAD +", "+
                    TABLE_VIAJE_COL_NUMPASAJEROS+" "+TABLE_VIAJE_TYPECOL_NUMPASAJEROS+", "+
                    TABLE_VIAJE_COL_RUTA        +" "+TABLE_VIAJE_TYPECOL_RUTA+", "+
                    TABLE_VIAJE_COL_HORAINICIO  +" "+TABLE_VIAJE_TYPECOL_HORAINICIO+", "+
                    TABLE_VIAJE_COL_HORAFIN     +" "+TABLE_VIAJE_TYPECOL_HORAFIN+", "+
                    TABLE_VIAJE_COL_COMENTARIO  +" "+TABLE_VIAJE_TYPECOL_COMENTARIO+", "+
                    TABLE_VIAJE_COL_STATUS      +" "+TABLE_VIAJE_TYPECOL_STATUS+" "+
                    ")";

    public static final String CREATE_TABLE_PASAJERO =
            " CREATE TABLE IF NOT EXISTS "+TABLE_PASAJERO+" (" +
                    TABLE_PASAJERO_COL_ID       +" "+TABLE_PASAJERO_TYPECOL_ID+" PRIMARY KEY AUTOINCREMENT," +
                    TABLE_PASAJERO_COL_DNI      +" "+TABLE_PASAJERO_TYPECOL_DNI+"," +
                    TABLE_PASAJERO_COL_NAME     +" "+TABLE_PASAJERO_TYPECOL_NAME+"," +
                    TABLE_PASAJERO_COL_IDVIAJE  +" "+ TABLE_PASAJERO_TYPECOL_IDVIAJE +" "+
                    ")";

    public static final String CREATE_TABLE_LOGINDATA =
            " CREATE TABLE IF NOT EXISTS "+TABLE_LOGINDATA+" (" +
                    TABLE_LOGINDATA_COL_IDUSER  +" "+TABLE_LOGINDATA_TYPECOL_IDUSER+" PRIMARY KEY  ," +
                    TABLE_LOGINDATA_COL_USER    +" "+TABLE_LOGINDATA_TYPECOL_USER+"," +
                    TABLE_LOGINDATA_COL_PASSWORD+" "+TABLE_LOGINDATA_TYPECOL_PASSWORD+"," +
                    TABLE_LOGINDATA_COL_NAME    +" "+TABLE_LOGINDATA_TYPECOL_NAME+","+
                    TABLE_LOGINDATA_COL_LASTNAME+" "+TABLE_LOGINDATA_TYPECOL_LASTNAME+" "+
                    ")";
}