package configuracion;

public class Contacto {

    private int id;
    private String nombre;
    private String nota;
    private String telefono;
    private byte[] imagen;
    private String pais;

    public Contacto() {
}

    public Contacto(int id, String nombre,String pais, String nota, String telefono,byte[] imagen) {
        this.id = id;
        this.nombre = nombre;
        this.nota = nota;
        this.telefono = telefono;
        this.pais = pais;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombre;
    }

    public void setNombres(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota= nota;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais= pais;
    }


    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

}




