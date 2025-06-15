package configuracion;

public class Contactos {

    // Nombre de la base de datos
    public static final String NameDB = "Examen";

    // Tabla de la base de datos
    public static final String TablaContacto = "contacto";

    // Campos de la tabla
    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String pais = "pais";

    // DDL CREATE
    public static final String CreateTableContacto = "CREATE TABLE " + TablaContacto + " ( " +
            id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            nombre + " TEXT, " +
            nota + " TEXT, " +
            telefono + " TEXT"+
            pais+"TEXT, )";

    // DROP TABLE
    public static final String DROPTableContacto = "DROP TABLE IF EXISTS " + TablaContacto;

    // DML SELECT
    public static final String SelectContacto = "SELECT * FROM " + TablaContacto;
}
