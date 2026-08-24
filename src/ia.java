import co.generation.clinica.datos.DatosCSV;
import src.*;

import java.time.*;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        ClinicaService servicio = new ClinicaService();

        // 1. Cargar datos al iniciar
        DatosCSV.cargar(servicio);

        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = leerInt("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarPaciente(servicio);
                case 2 -> registrarMedico(servicio);
                case 3 -> asignarTurno(servicio);
                case 4 -> listarTurnosDelDia(servicio);
                case 5 -> cancelarTurno(servicio);
                case 6 -> verTurnosPorMedico(servicio);
                case 7 -> verTurnosPorPaciente(servicio);
                case 8 -> cambiarEstadoTurno(servicio);
                case 9 -> servicio.listarPacientes();
                case 10 -> servicio.listarMedicos();
                case 0 -> {
                    // Guardar datos al salir
                    DatosCSV.guardar(servicio);
                    System.out.println("Hasta pronto. Datos guardados.");
                    salir = true;
                }
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
            System.out.println(); // Línea en blanco para legibilidad
        }
    }

    private static void mostrarMenu() {
        System.out.println("==================================================");
        System.out.println("               CLINICAAPP - MENÚ                  ");
        System.out.println("==================================================");
        System.out.println(" 1. Registrar paciente");
        System.out.println(" 2. Registrar médico");
        System.out.println(" 3. Asignar turno");
        System.out.println(" 4. Listar turnos del día");
        System.out.println(" 5. Cancelar turno");
        System.out.println(" 6. Ver turnos por médico");
        System.out.println(" 7. Ver turnos por paciente");
        System.out.println(" 8. Cambiar estado de turno");
        System.out.println(" 9. Listar pacientes");
        System.out.println("10. Listar médicos");
        System.out.println(" 0. Salir");
        System.out.println("==================================================");
    }

    // --- MÉTODOS AUXILIARES DEL MENÚ ---

    private static void registrarPaciente(ClinicaService servicio) {
        System.out.println("\n--- REGISTRAR PACIENTE ---");
        String cedula = leerString("Cédula: ");
        String nombre = leerString("Nombre: ");
        String apellido = leerString("Apellido: ");
        String telefono = leerString("Teléfono (7 a 10 dígitos): ");

        try {
            // El ID se asigna automáticamente dentro de ClinicaService
            Paciente p = new Paciente(cedula, nombre, apellido, telefono);
            servicio.registrarPaciente(p);
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }

    private static void registrarMedico(ClinicaService servicio) {
        System.out.println("\n--- REGISTRAR MÉDICO ---");
        String nombre = leerString("Nombre: ");
        String apellido = leerString("Apellido: ");

        System.out.println("Especialidades disponibles:");
        Especialidad[] especialidades = Especialidad.values();
        for (int i = 0; i < especialidades.length; i++) {
            System.out.println((i + 1) + ". " + especialidades[i]);
        }

        int opEspec = leerInt("Seleccione especialidad (1-" + especialidades.length + "): ");
        if (opEspec < 1 || opEspec > especialidades.length) {
            System.out.println("Especialidad no válida.");
            return;
        }

        try {
            Medico m = new Medico(nombre, apellido, especialidades[opEspec - 1]);
            servicio.registrarMedico(m);
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }

    private static void asignarTurno(ClinicaService servicio) {
        System.out.println("\n--- ASIGNAR TURNO ---");
        String cedula = leerString("Cédula del paciente: ");
        Paciente pac = servicio.buscarPorCedula(cedula);
        if (pac == null) {
            System.out.println("Error: Paciente no encontrado con cédula: " + cedula);
            return;
        }

        String nomMed = leerString("Nombre del médico: ");
        String apeMed = leerString("Apellido del médico: ");
        Medico med = servicio.buscarPorNombreApellido(nomMed, apeMed);
        if (med == null) {
            System.out.println("Error: Médico no encontrado.");
            return;
        }

        System.out.println("Ingrese la fecha y hora del turno:");
        int anio = leerInt("Año (ej. 2026): ");
        int mes = leerInt("Mes (1-12): ");
        int dia = leerInt("Día (1-31): ");
        int hora = leerInt("Hora (0-23): ");
        int minuto = leerInt("Minuto (0-59): ");

        try {
            LocalDateTime fechaHora = LocalDateTime.of(anio, mes, dia, hora, minuto);
            Turno turno = new Turno(pac, med, fechaHora);
            servicio.asignarTurno(turno);
        } catch (Exception e) {
            System.out.println("Error al asignar turno: " + e.getMessage());
        }
    }

    private static void listarTurnosDelDia(ClinicaService servicio) {
        System.out.println("\n--- TURNOS DEL DÍA ---");
        String fechaStr = leerString("Ingrese la fecha (YYYY-MM-DD): ");
        try {
            LocalDate fecha = LocalDate.parse(fechaStr, DATE_FORMATTER);
            List<Turno> turnos = servicio.listarTurnosDelDia(fecha);
            if (turnos.isEmpty()) {
                System.out.println("No hay turnos registrados para esa fecha.");
            } else {
                turnos.forEach(System.out.println);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido. Debe ser YYYY-MM-DD.");
        }
    }

    private static void cancelarTurno(ClinicaService servicio) {
        System.out.println("\n--- CANCELAR TURNO ---");
        int idTurno = leerInt("ID del turno a cancelar: ");
        servicio.cancelarTurno(idTurno);
    }

    private static void verTurnosPorMedico(ClinicaService servicio) {
        System.out.println("\n--- TURNOS POR MÉDICO ---");
        String nombre = leerString("Nombre del médico: ");
        String apellido = leerString("Apellido del médico: ");

        Medico med = servicio.buscarPorNombreApellido(nombre, apellido);
        if (med == null) {
            System.out.println("Médico no encontrado.");
            return;
        }

        List<Turno> turnos = servicio.buscarPorMedico(med);
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos para este médico.");
        } else {
            turnos.forEach(System.out.println);
        }
    }

    private static void verTurnosPorPaciente(ClinicaService servicio) {
        System.out.println("\n--- TURNOS POR PACIENTE ---");
        String cedula = leerString("Cédula del paciente: ");

        Paciente pac = servicio.buscarPorCedula(cedula);
        if (pac == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        List<Turno> turnos = servicio.buscarPorPaciente(pac);
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos para este paciente.");
        } else {
            turnos.forEach(System.out.println);
        }
    }

    private static void cambiarEstadoTurno(ClinicaService servicio) {
        System.out.println("\n--- CAMBIAR ESTADO DE TURNO ---");
        int idTurno = leerInt("ID del turno: ");

        System.out.println("Estados disponibles:");
        EstadoTurno[] estados = EstadoTurno.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.println((i + 1) + ". " + estados[i]);
        }

        int opEstado = leerInt("Seleccione nuevo estado (1-" + estados.length + "): ");
        if (opEstado < 1 || opEstado > estados.length) {
            System.out.println("Estado no válido.");
            return;
        }

        servicio.cambiarEstadoTurno(idTurno, estados[opEstado - 1]);
    }

    // --- LECTURA SEGURA DE ENTRADAS ---

    private static String leerString(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número entero válido.");
            }
        }
    }
}