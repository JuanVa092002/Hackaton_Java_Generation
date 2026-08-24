package clinica.*;


import java.time.LocalDateTime;

public class Turno {

    private int id;
    private LocalDateTime fechaHora;
    private Paciente paciente;
    private Medico medico;
    private EstadoTurno estado;

    public Turno(int id, LocalDateTime fechaHora, Paciente paciente, Medico medico) {
        this.id = id;
        setFechaHora(fechaHora);
        setPaciente(paciente);
        setMedico(medico);
        this.estado = EstadoTurno.PENDIENTE;
    }

    public Turno(LocalDateTime fechaHora, Paciente paciente, Medico medico) {
        setFechaHora(fechaHora);
        setPaciente(paciente);
        setMedico(medico);
        this.estado = EstadoTurno.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null || !fechaHora.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora deben ser validas");
        }
        this.fechaHora = fechaHora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente es obligatorio");
        }
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("Seleccione un medico");
        }
        this.medico = medico;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno)) return false;

        Turno turno = (Turno) o;
        return fechaHora.equals(turno.fechaHora)
                && medico.equals(turno.medico);
    }

    @Override
    public String toString() {
        return "[" + estado + "] "
                + paciente.getNombre() + " " + paciente.getApellido()
                + " — Dr. " + medico.getNombre() + " " + medico.getApellido()
                + " (" + medico.getEspecialidad() + ")"
                + " — " + fechaHora;
    }
}