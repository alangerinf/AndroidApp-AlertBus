package ibao.alanger.alertbus.utilities;


public class Utilities {

    private static String TAG = Utilities.class.getSimpleName();

    public static final String URL_ROOT="http://35.167.15.182/AlertBus/EcoSac/Requests/";
    public static final String URL_AUTENTIFICATION=URL_ROOT+"autenticar.php";
    public static final String URL_BUSCARNUEVOS=URL_ROOT+"getViajes.php";
    public static final String URL_BUSCARTRABAJADOR=URL_ROOT+"getTrabajador.php";

    public static final String URL_UPLOAD_VIAJE =URL_ROOT+"insertViajes.php";
    public static final String URL_CHECK_VIAJE =URL_ROOT+"checkViajes.php";
    public static final String URL_CHANGES_VIAJES =URL_ROOT+"updatesViajes.php";
    public static final String URL_UPLOAD_TRACKING=URL_ROOT+"insertTracking.php";


    public static final String URL_SELECT_TRABAJADORES=URL_ROOT+"selectTrabajadores.php";
    public static final String URL_SELECT_RESTRICCIONES=URL_ROOT+"selectRestricciones.php";

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
            TABLE_LOGINDATA_TYPECOL_LASTNAME    ="varchar(50)",
            TABLE_LOGINDATA_COL_IDVIAJE         ="idViaje",
            TABLE_LOGINDATA_TYPECOL_IDVIAJE     ="INTEGER",
            TABLE_LOGINDATA_COL_TYPEUSER        ="typeUser",
            TABLE_LOGINDATA_TYPECOL_TYPEUSER    ="INTEGER";

    public static final String TABLE_TRACKING   ="tracking",
            TABLE_TRACKING_COL_ISUPDATE         ="isUpdate",
            TABLE_TRACKING_TYPECOL_ISUPDATE     ="BOOLEAN",
            TABLE_TRACKING_COL_LAT              ="latitud",
            TABLE_TRACKING_TYPECOL_LAT          ="VARCHAR(50)",
            TABLE_TRACKING_COL_LON              ="longitud",
            TABLE_TRACKING_TYPECOL_LON          ="VARCHAR(50)",
            TABLE_TRACKING_COL_BEARING          ="bearing",
            TABLE_TRACKING_TYPECOL_BEARING      ="VARCHAR(50)",
            TABLE_TRACKING_COL_SPEED            ="speed",
            TABLE_TRACKING_TYPECOL_SPEED        ="VARCHAR(50)",
            TABLE_TRACKING_COL_DATETIME         ="datetime",
            TABLE_TRACKING_TYPECOL_DATETIME     ="DATETIME";
    
    public static final String TABLE_PASAJERO   ="pasajero",
            TABLE_PASAJERO_COL_ID               ="idUser",
            TABLE_PASAJERO_TYPECOL_ID           ="INTEGER",
            TABLE_PASAJERO_COL_DNI              ="dni",
            TABLE_PASAJERO_TYPECOL_DNI          ="varchar(50)",
            TABLE_PASAJERO_COL_NAME             ="name",
            TABLE_PASAJERO_TYPECOL_NAME         ="varchar(50)",
            TABLE_PASAJERO_COL_IDVIAJE          ="idViaje",
            TABLE_PASAJERO_TYPECOL_IDVIAJE      ="INTEGER",
            TABLE_PASAJERO_COL_HORASUBIDA       ="hSubida",
            TABLE_PASAJERO_TYPECOL_HORASUBIDA   ="VARCHAR(100)",
            TABLE_PASAJERO_COL_HORABAJADA       ="hBajada",
            TABLE_PASAJERO_TYPECOL_HORABAJADA   ="VARCHAR(100)",
            TABLE_PASAJERO_COL_OBSERVACION      ="observacion",
            TABLE_PASAJERO_TYPECOL_OBSERVACION  ="VARCHAR(100)";

    public static final String TABLE_RESTRICCION="restriccion",
            TABLE_RESTRICCION_COL_ID            ="id",
            TABLE_RESTRICCION_TYPECOL_ID        ="INTEGER",
            TABLE_RESTRICCION_COL_NAME          ="name",
            TABLE_RESTRICCION_TYPECOL_NAME      ="varchar(50)",
            TABLE_RESTRICCION_COL_DESC          ="descripcion",
            TABLE_RESTRICCION_TYPECOL_DESC      ="varchar(200)",
            TABLE_RESTRICCION_COL_IDVIAJE       ="INTEGER",
            TABLE_RESTRICCION_TYPECOL_IDVIAJE   ="INTEGER";

    public static final String TABLE_VIAJE ="viaje",
            TABLE_VIAJE_COL_ID                  ="id",
            TABLE_VIAJE_TYPECOL_ID              ="INTEGER",
            TABLE_VIAJE_COL_IDWEB               ="idWeb",
            TABLE_VIAJE_TYPECOL_IDWEB           ="INTEGER",
            TABLE_VIAJE_COL_PROVEEDOR           ="proveedor",
            TABLE_VIAJE_TYPECOL_PROVEEDOR       ="VARCHAR(50)",
            TABLE_VIAJE_COL_PLACA               ="placa",
            TABLE_VIAJE_TYPECOL_PLACA           ="VARCHAR(50)",
            TABLE_VIAJE_COL_CONDUCTOR           ="conductor",
            TABLE_VIAJE_TYPECOL_CONDUCTOR       ="varchar(50)",
            TABLE_VIAJE_COL_CAPACIDAD           ="capacidad",
            TABLE_VIAJE_TYPECOL_CAPACIDAD       ="INTEGER",
            TABLE_VIAJE_COL_RUTA                ="ruta",
            TABLE_VIAJE_TYPECOL_RUTA            ="VARCHAR(50)",
            TABLE_VIAJE_COL_HORAPROGRAMADA      ="horaProgramada",
            TABLE_VIAJE_TYPECOL_HORAPROGRAMADA  ="VARCHAR(50)",
            TABLE_VIAJE_COL_HORAINICIO          ="horaInicio",
            TABLE_VIAJE_TYPECOL_HORAINICIO      ="VARCHAR(50)",
            TABLE_VIAJE_COL_HORAFIN             ="horaFin",
            TABLE_VIAJE_TYPECOL_HORAFIN         ="VARCHAR(50)",
            TABLE_VIAJE_COL_COMENTARIO          ="comentario",
            TABLE_VIAJE_TYPECOL_COMENTARIO      ="VARCHAR(200)",
            TABLE_VIAJE_COL_STATUS              ="status", // 0:recibido, 1:verificado, 2:sincronizado
            TABLE_VIAJE_TYPECOL_STATUS          ="INTEGER",
            TABLE_VIAJE_COL_HORACONFIRMADO      ="horaOk", // HORA EN LA  Q SE  VERIFICO
            TABLE_VIAJE_TYPECOL_HORACONFIRMADO  ="DATETIME",
            TABLE_VIAJE_COL_NUMPASAJEROS        ="numPasajero", // numero de pasajeros guardados al pasar por qr solo tendra esto la parte de supervisor
            TABLE_VIAJE_TYPECOL_NUMPASAJEROS    ="INTEGER",
            TABLE_VIAJE_COL_NUMRESTRICCIONES    ="numRestricciones", // numero de pasajeros guardados al pasar por qr solo tendra esto la parte de supervisor
            TABLE_VIAJE_TYPECOL_NUMRESTRICCIONES="INTEGER"
                    ;




    //SCRIPTS SQL CREATE TABLES
    public static final String CREATE_TABLE_VIAJE =
            " CREATE TABLE IF NOT EXISTS "+ TABLE_VIAJE +" ("+
                    TABLE_VIAJE_COL_ID              +" "+TABLE_VIAJE_TYPECOL_ID +" PRIMARY KEY , "+
                    TABLE_VIAJE_COL_IDWEB           +" "+TABLE_VIAJE_TYPECOL_IDWEB +" , "+
                    TABLE_VIAJE_COL_PROVEEDOR       +" "+TABLE_VIAJE_TYPECOL_PROVEEDOR +" , "+
                    TABLE_VIAJE_COL_PLACA           +" "+TABLE_VIAJE_TYPECOL_PLACA +" , "+
                    TABLE_VIAJE_COL_CONDUCTOR       +" "+TABLE_VIAJE_TYPECOL_CONDUCTOR +", "+
                    TABLE_VIAJE_COL_CAPACIDAD       +" "+ TABLE_VIAJE_TYPECOL_CAPACIDAD +", "+
                    TABLE_VIAJE_COL_RUTA            +" "+TABLE_VIAJE_TYPECOL_RUTA+", "+
                    TABLE_VIAJE_COL_HORAPROGRAMADA  +" "+TABLE_VIAJE_TYPECOL_HORAPROGRAMADA+", "+
                    TABLE_VIAJE_COL_HORAINICIO      +" "+TABLE_VIAJE_TYPECOL_HORAINICIO+", "+
                    TABLE_VIAJE_COL_HORAFIN         +" "+TABLE_VIAJE_TYPECOL_HORAFIN+", "+
                    TABLE_VIAJE_COL_COMENTARIO      +" "+TABLE_VIAJE_TYPECOL_COMENTARIO+", "+
                    TABLE_VIAJE_COL_STATUS          +" "+TABLE_VIAJE_TYPECOL_STATUS+", "+
                    TABLE_VIAJE_COL_NUMPASAJEROS    +" "+TABLE_VIAJE_TYPECOL_NUMPASAJEROS+", "+
                    TABLE_VIAJE_COL_NUMRESTRICCIONES    +" "+TABLE_VIAJE_TYPECOL_NUMRESTRICCIONES+", "+
                    TABLE_VIAJE_COL_HORACONFIRMADO  +" "+ TABLE_VIAJE_TYPECOL_HORACONFIRMADO +" "+
                    ")";

    public static final String CREATE_TABLE_PASAJERO =
            " CREATE TABLE IF NOT EXISTS "+TABLE_PASAJERO+" (" +
                    TABLE_PASAJERO_COL_ID           +" "+TABLE_PASAJERO_TYPECOL_ID+" PRIMARY KEY AUTOINCREMENT," +
                    TABLE_PASAJERO_COL_DNI          +" "+TABLE_PASAJERO_TYPECOL_DNI+"," +
                    TABLE_PASAJERO_COL_NAME         +" "+TABLE_PASAJERO_TYPECOL_NAME+"," +
                    TABLE_PASAJERO_COL_IDVIAJE      +" "+ TABLE_PASAJERO_TYPECOL_IDVIAJE +", "+
                    TABLE_PASAJERO_COL_HORASUBIDA   +" "+ TABLE_PASAJERO_TYPECOL_HORASUBIDA +", "+
                    TABLE_PASAJERO_COL_HORABAJADA   +" "+ TABLE_PASAJERO_TYPECOL_HORABAJADA +", "+
                    TABLE_PASAJERO_COL_OBSERVACION  +" "+ TABLE_PASAJERO_TYPECOL_OBSERVACION +" "+
                    ")";
    public static final String CREATE_TABLE_RESTRICCION =
            " CREATE TABLE IF NOT EXISTS "+TABLE_RESTRICCION+" (" +
                    TABLE_RESTRICCION_COL_ID           +" "+TABLE_RESTRICCION_TYPECOL_ID+" PRIMARY KEY AUTOINCREMENT," +
                    TABLE_RESTRICCION_COL_NAME         +" "+TABLE_RESTRICCION_TYPECOL_NAME+"," +
                    TABLE_RESTRICCION_COL_DESC         +" "+TABLE_RESTRICCION_TYPECOL_DESC+", " +
                    TABLE_RESTRICCION_COL_IDVIAJE      +" "+ TABLE_RESTRICCION_TYPECOL_IDVIAJE +" "+
                    ")";
    public static final String CREATE_TABLE_LOGINDATA =
            " CREATE TABLE IF NOT EXISTS "+TABLE_LOGINDATA+" (" +
                    TABLE_LOGINDATA_COL_IDUSER  +" "+TABLE_LOGINDATA_TYPECOL_IDUSER+" PRIMARY KEY  ," +
                    TABLE_LOGINDATA_COL_USER    +" "+TABLE_LOGINDATA_TYPECOL_USER+"," +
                    TABLE_LOGINDATA_COL_PASSWORD+" "+TABLE_LOGINDATA_TYPECOL_PASSWORD+"," +
                    TABLE_LOGINDATA_COL_NAME    +" "+TABLE_LOGINDATA_TYPECOL_NAME+","+
                    TABLE_LOGINDATA_COL_LASTNAME+" "+TABLE_LOGINDATA_TYPECOL_LASTNAME+", "+
                    TABLE_LOGINDATA_COL_IDVIAJE+" "+TABLE_LOGINDATA_TYPECOL_IDVIAJE+", "+//ID DEL VIAJE ACTUAL
                    TABLE_LOGINDATA_COL_TYPEUSER+" "+TABLE_LOGINDATA_TYPECOL_TYPEUSER+" "+
                    ")";






    public static final String CREATE_TABLE_TRACKING =
            " CREATE TABLE IF NOT EXISTS "+TABLE_TRACKING+" (" +
                    TABLE_TRACKING_COL_ISUPDATE+" "+TABLE_TRACKING_TYPECOL_ISUPDATE+","+
                    TABLE_TRACKING_COL_LAT +" "+TABLE_TRACKING_TYPECOL_LAT+" NOT NULL," +
                    TABLE_TRACKING_COL_LON +" "+TABLE_TRACKING_TYPECOL_LON+" NOT NULL," +
                    TABLE_TRACKING_COL_BEARING +" "+TABLE_TRACKING_TYPECOL_BEARING+" NOT NULL," +
                    TABLE_TRACKING_COL_SPEED +" "+TABLE_TRACKING_TYPECOL_SPEED+" NOT NULL," +
                    TABLE_TRACKING_COL_DATETIME +" "+TABLE_TRACKING_TYPECOL_DATETIME +" DEFAULT (datetime('now','localtime')) "+
                    ")";

}