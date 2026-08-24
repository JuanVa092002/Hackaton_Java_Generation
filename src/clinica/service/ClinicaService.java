package clinica.service;

import clinica.interfaces.Consultable;
import clinica.model.EstadoTurno;
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.model.Turno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClinicaService implements Consultable {

    private List<Paciente> pacientes = new ArrayList<Paciente>();
    private List<Medico> medicos = new ArrayList<Medico>();
    private List<Turno> turnos = new ArrayList<Turno>();

    // DatosCSV hace .add() sobre estas listas, por eso no devuelvo una copia
    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void registrarPaciente(Paciente p) {
        if (p == null || p.esValido() == false) {
            System.out.println("Error: el paciente no es valido. Revisa cedula, nombre, apellido y telefono.");
            return;
        }
        // contains usa el equals de Paciente (la cédula)
        if (pacientes.contains(p)) {
            System.out.println("Error: ya existe un paciente con esa cedula.");
            return;
        }
        p.setId(siguienteIdPacientes());
        pacientes.add(p);
        System.out.println("Paciente registrado: " + p);
    }

    public Paciente buscarPorCedula(String cedula) {
        if (cedula == null) {
            return null;
        }
        for (int i = 0; i < pacientes.size(); i++) {
            Paciente p = pacientes.get(i);
            if (cedula.equals(p.getCedula())) {
                return p;
            }
        }
        return null;
    }

    public void listarPacientes() {
        if (pacientes.size() == 0) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        List<Paciente> copia = new ArrayList<Paciente>();
        for (int i = 0; i < pacientes.size(); i++) {
            copia.add(pacientes.get(i));
        }
        // ordeno una copia para no mover la lista original
        for (int i = 0; i < copia.size() - 1; i++) {
            for (int j = i + 1; j < copia.size(); j++) {
                Paciente a = copia.get(i);
                Paciente b = copia.get(j);
                int porApellido = a.getApellido().compareTo(b.getApellido());
                if (porApellido > 0) {
                    copia.set(i, b);
                    copia.set(j, a);
                } else if (porApellido == 0 && a.getNombre().compareTo(b.getNombre()) > 0) {
                    copia.set(i, b);
                    copia.set(j, a);
                }
            }
        }
        for (int i = 0; i < copia.size(); i++) {
            System.out.println(copia.get(i));
        }
    }

    public void registrarMedico(Medico m) {
        if (m == null || m.esValido() == false) {
            System.out.println("Error: el medico no es valido. Revisa nombre, apellido y especialidad.");
            return;
        }
        if (medicos.contains(m)) {
            System.out.println("Error: ya existe un medico con ese nombre y apellido.");
            return;
        }
        m.setId(siguienteIdMedicos());
        medicos.add(m);
        System.out.println("Medico registrado: " + m);
    }

    public Medico buscarPorNombreApellido(String nombre, String apellido) {
        if (nombre == null || apellido == null) {
            return null;
        }
        for (int i = 0; i < medicos.size(); i++) {
            Medico m = medicos.get(i);
            if (m.getNombre().equalsIgnoreCase(nombre) && m.getApellido().equalsIgnoreCase(apellido)) {
                return m;
            }
        }
        return null;
    }

    public void listarMedicos() {
        if (medicos.size() == 0) {
            System.out.println("No hay medicos registrados.");
            return;
        }
        List<Medico> copia = new ArrayList<Medico>();
        for (int i = 0; i < medicos.size(); i++) {
            copia.add(medicos.get(i));
        }
        for (int i = 0; i < copia.size() - 1; i++) {
            for (int j = i + 1; j < copia.size(); j++) {
                Medico a = copia.get(i);
                Medico b = copia.get(j);
                int porEsp = a.getEspecialidad().compareTo(b.getEspecialidad());
                if (porEsp > 0) {
                    copia.set(i, b);
                    copia.set(j, a);
                } else if (porEsp == 0 && a.getApellido().compareTo(b.getApellido()) > 0) {
                    copia.set(i, b);
                    copia.set(j, a);
                }
            }
        }
        for (int i = 0; i < copia.size(); i++) {
            System.out.println(copia.get(i));
        }
    }

    public void asignarTurno(Turno t) {
        if (t == null || t.getPaciente() == null || t.getMedico() == null || t.getFechaHora() == null) {
            System.out.println("Error: el turno no es valido.");
            return;
        }

        Paciente paciente = buscarPorCedula(t.getPaciente().getCedula());
        if (paciente == null) {
            System.out.println("Error: el paciente no esta registrado.");
            return;
        }

        Medico medico = buscarPorNombreApellido(t.getMedico().getNombre(), t.getMedico().getApellido());
        if (medico == null) {
            System.out.println("Error: el medico no esta registrado.");
            return;
        }

        // equals de Turno compara médico y fechaHora
        if (turnos.contains(t)) {
            System.out.println("Error: el medico ya tiene un turno en esa fecha y hora.");
            return;
        }

        t.setId(siguienteIdTurnos());
        turnos.add(t);
        System.out.println("Turno asignado: " + t);
    }

    public void cancelarTurno(int idTurno) {
        Turno turno = buscarTurnoPorId(idTurno);
        if (turno == null) {
            System.out.println("Turno no encontrado.");
            return;
        }
        if (turno.getEstado() == EstadoTurno.ATENDIDO || turno.getEstado() == EstadoTurno.CANCELADO) {
            System.out.println("No se puede cancelar un turno en estado " + turno.getEstado() + ".");
            return;
        }
        turno.setEstado(EstadoTurno.CANCELADO);
        System.out.println("Turno cancelado: " + turno);
    }

    public void cambiarEstadoTurno(int idTurno, EstadoTurno nuevo) {
        Turno turno = buscarTurnoPorId(idTurno);
        if (turno == null) {
            System.out.println("Turno no encontrado.");
            return;
        }
        if (nuevo == null) {
            System.out.println("Error: el estado no puede ser nulo.");
            return;
        }
        turno.setEstado(nuevo);
        System.out.println("Estado actualizado a " + nuevo + ": " + turno);
    }

    @Override
    public List<Turno> listarTurnosDelDia(LocalDate fecha) {
        List<Turno> delDia = new ArrayList<Turno>();
        if (fecha == null) {
            return delDia;
        }
        for (int i = 0; i < turnos.size(); i++) {
            Turno t = turnos.get(i);
            if (t.getFechaHora() != null) {
                if (fecha.equals(t.getFechaHora().toLocalDate())) {
                    delDia.add(t);
                }
            }
        }
        for (int i = 0; i < delDia.size() - 1; i++) {
            for (int j = i + 1; j < delDia.size(); j++) {
                Turno a = delDia.get(i);
                Turno b = delDia.get(j);
                if (a.getFechaHora().compareTo(b.getFechaHora()) > 0) {
                    delDia.set(i, b);
                    delDia.set(j, a);
                }
            }
        }
        return delDia;
    }

    @Override
    public List<Turno> buscarPorMedico(Medico medico) {
        List<Turno> resultado = new ArrayList<Turno>();
        if (medico == null) {
            return resultado;
        }
        for (int i = 0; i < turnos.size(); i++) {
            Turno t = turnos.get(i);
            if (medico.equals(t.getMedico())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    @Override
    public List<Turno> buscarPorPaciente(Paciente paciente) {
        List<Turno> resultado = new ArrayList<Turno>();
        if (paciente == null) {
            return resultado;
        }
        for (int i = 0; i < turnos.size(); i++) {
            Turno t = turnos.get(i);
            if (paciente.equals(t.getPaciente())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    private Turno buscarTurnoPorId(int idTurno) {
        for (int i = 0; i < turnos.size(); i++) {
            Turno t = turnos.get(i);
            if (t.getId() == idTurno) {
                return t;
            }
        }
        return null;
    }

    private int siguienteIdPacientes() {
        int max = 0;
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() > max) {
                max = pacientes.get(i).getId();
            }
        }
        return max + 1;
    }

    private int siguienteIdMedicos() {
        int max = 0;
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId() > max) {
                max = medicos.get(i).getId();
            }
        }
        return max + 1;
    }

    private int siguienteIdTurnos() {
        int max = 0;
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId() > max) {
                max = turnos.get(i).getId();
            }
        }
        return max + 1;
    }
}
