package model;
import interfaces.Registrable;

public class Paciente implements Registrable {  //este registrable de interfaces para revisar con cesar
    private int id;
    private String cedula;
    private String nombre;
    private String apellido;
    private String telefono;


    public Paciente(int id, String cedula, String nombre, String apellido, String telefono) {  //este para reconstruir un paciente desde CSV
        this.id = id;
        setCedula(cedula);
        setNombre(nombre);
        setApellido(apellido);
        setTelefono(telefono);
    }

    public Paciente(String cedula, String nombre, String apellido, String telefono) { //Y ESTE SIRE PARA CUANDO SEREGISTRA UN PACIENTE NUEVO
        setCedula(cedula);
        setNombre(nombre);
        setApellido(apellido);
        setTelefono(telefono);
    }



    public int getId() {   //getter permite consultar
        return id;
    }

    public void setId(int id) {  //y el setter permite cambiar los datos
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {  //comporbar que no haya enviado un null  y elimianr espacios de los extremos
            throw new IllegalArgumentException("La cédula no puede ser nula ni vacía");
        }
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo ni vacío");
        }
        this.apellido = apellido.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("^[0-9]{7,10}$")) {
            throw new IllegalArgumentException("El teléfono debe tener entre 7 y 10 dígitos");
        }
        this.telefono = telefono;
    }

    @Override
    public String getDatosRegistro() {
        return toString();
    }

    @Override
    public boolean esValido() {      //cuando los datos del paciente estan bien o mal escritos true o false
        return cedula != null && !cedula.trim().isEmpty()
                && nombre != null && !nombre.trim().isEmpty()
                && apellido != null && !apellido.trim().isEmpty()
                && telefono != null && telefono.matches("^[0-9]{7,10}$");
    }

    @Override
    public boolean equals(Object o) {  //si hay 2 pacientes iguales equals
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;

        Paciente paciente = (Paciente) o;
        return cedula.equals(paciente.cedula);
    }



    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + cedula + " - " + telefono;
    }

}