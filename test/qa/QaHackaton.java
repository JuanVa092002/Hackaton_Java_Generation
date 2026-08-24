package qa;

import clinica.data.DatosCSV;
import clinica.model.Especialidad;
import clinica.model.EstadoTurno;
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.model.Turno;
import clinica.service.ClinicaService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class QaHackaton {

    private static int pass = 0;
    private static int fail = 0;

    public static void main(String[] args) {
        // modelo
        Paciente p1 = new Paciente("1020304050", "Maria", "Garcia", "3001234567");
        Paciente p2 = new Paciente("1020304050", "Otra", "Persona", "3111111111");
        check("Paciente equals por cedula", p1.equals(p2));
        check("Paciente esValido", p1.esValido());

        Medico m1 = new Medico("Carlos", "Perez", Especialidad.CARDIOLOGIA);
        Medico m2 = new Medico("carlos", "PEREZ", Especialidad.PEDIATRIA);
        check("Medico equals ignore case", m1.equals(m2));
        check("Medico esValido", m1.esValido());

        LocalDateTime fh = LocalDateTime.of(2027, 12, 15, 10, 30);
        Turno t1 = new Turno(p1, m1, fh);
        Turno t2 = new Turno(p2, m2, fh);
        check("Turno equals medico y fecha", t1.equals(t2));
        check("Turno nace PENDIENTE", t1.getEstado() == EstadoTurno.PENDIENTE);

        boolean telMalo = false;
        try {
            p1.setTelefono("7");
        } catch (IllegalArgumentException e) {
            telMalo = true;
        }
        check("telefono invalido lanza error", telMalo);

        boolean nombreMalo = false;
        try {
            p1.setNombre("  ");
        } catch (IllegalArgumentException e) {
            nombreMalo = true;
        }
        check("nombre vacio lanza error", nombreMalo);

        boolean espMala = false;
        try {
            m1.setEspecialidad(null);
        } catch (IllegalArgumentException e) {
            espMala = true;
        }
        check("especialidad null lanza error", espMala);

        // servicio
        ClinicaService s = new ClinicaService();
        Paciente p = new Paciente("1020304050", "Maria", "Garcia", "3001234567");
        s.registrarPaciente(p);
        s.registrarPaciente(new Paciente("1020304050", "Otra", "Persona", "3009999999"));
        check("no duplica paciente", s.getPacientes().size() == 1);

        Medico m = new Medico("Carlos", "Perez", Especialidad.CARDIOLOGIA);
        s.registrarMedico(m);
        s.registrarMedico(new Medico("CARLOS", "PEREZ", Especialidad.PEDIATRIA));
        check("no duplica medico", s.getMedicos().size() == 1);

        s.asignarTurno(new Turno(p, m, fh));
        check("asigna turno", s.getTurnos().size() == 1 && s.getTurnos().get(0).getEstado() == EstadoTurno.PENDIENTE);

        s.asignarTurno(new Turno(p, m, fh));
        check("no choca agenda", s.getTurnos().size() == 1);

        check("turnos del dia", s.listarTurnosDelDia(LocalDate.of(2027, 12, 15)).size() == 1);
        check("buscar por medico", s.buscarPorMedico(m).size() == 1);
        check("buscar por paciente", s.buscarPorPaciente(p).size() == 1);
        check("buscar por cedula", s.buscarPorCedula("1020304050") != null);
        check("buscar medico ignore case", s.buscarPorNombreApellido("carlos", "perez") != null);

        s.cambiarEstadoTurno(s.getTurnos().get(0).getId(), EstadoTurno.ATENDIDO);
        check("cambia a ATENDIDO", s.getTurnos().get(0).getEstado() == EstadoTurno.ATENDIDO);

        s.cancelarTurno(s.getTurnos().get(0).getId());
        check("no cancela ATENDIDO", s.getTurnos().get(0).getEstado() == EstadoTurno.ATENDIDO);

        // csv
        ClinicaService original = new ClinicaService();
        original.registrarPaciente(p);
        original.registrarMedico(m);
        original.asignarTurno(new Turno(p, m, fh));
        original.cambiarEstadoTurno(original.getTurnos().get(0).getId(), EstadoTurno.ATENDIDO);
        DatosCSV.guardar(original);

        ClinicaService recargado = new ClinicaService();
        DatosCSV.cargar(recargado);
        check("csv paciente", recargado.buscarPorCedula("1020304050") != null);
        check("csv medico", recargado.buscarPorNombreApellido("Carlos", "Perez") != null);
        check("csv turno ATENDIDO", recargado.getTurnos().size() == 1
                && recargado.getTurnos().get(0).getEstado() == EstadoTurno.ATENDIDO);

        System.out.println();
        System.out.println("Resultado: " + pass + " PASS, " + fail + " FAIL");
        if (fail > 0) {
            System.exit(1);
        }
    }

    private static void check(String nombre, boolean ok) {
        if (ok) {
            pass = pass + 1;
            System.out.println("PASS  " + nombre);
        } else {
            fail = fail + 1;
            System.out.println("FAIL  " + nombre);
        }
    }
}
