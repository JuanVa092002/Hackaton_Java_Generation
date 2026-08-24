package clinica.model;

import clinica.interfaces.Registrable;

public class Medico implements Registrable {

    private int id;
    private String nombre;
    private String apellido;
    private Especialidad especialidad;

    public Medico(int id, String nombre, String apellido, Especialidad especialidad) {
        this.id = id;
        setNombre(nombre);
        setApellido(apellido);
        setEspecialidad(especialidad);
    }

    public Medico(String nombre, String apellido, Especialidad especialidad) {
        setNombre(nombre);
        setApellido(apellido);
        setEspecialidad(especialidad);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío");
        }
        if (!nombre.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+([ ]+[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")) {
            throw new IllegalArgumentException("El nombre solo puede tener letras");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacio");
        }
        if (!apellido.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+([ ]+[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")) {
            throw new IllegalArgumentException("El apellido solo puede tener letras");
        }
        this.apellido = apellido.trim();
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            throw new IllegalArgumentException("La especialidad no puede estar vacia");
        }
        this.especialidad = especialidad;
    }

    @Override
    public String getDatosRegistro() {
        return toString();
    }

    @Override
    public boolean esValido() {
        return nombre != null && nombre.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+([ ]+[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")
                && apellido != null && apellido.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+([ ]+[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")
                && especialidad != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medico)) return false;

        Medico medico = (Medico) o;

        return nombre.equalsIgnoreCase(medico.nombre)
                && apellido.equalsIgnoreCase(medico.apellido);
    }

    @Override
    public String toString() {
        return "Dr. " + nombre + " " + apellido + " - " + especialidad;
    }
}