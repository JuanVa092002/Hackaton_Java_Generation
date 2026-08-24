package clinica;

import clinica.data.DatosCSV;
import clinica.model.*;
import clinica.service.ClinicaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ClinicaService servicio = new ClinicaService();
        DatosCSV.cargar(servicio);

        Scanner captura = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            Menu();
            System.out.print("Por favor seleccione una opción: ");
            String opciones = captura.nextLine().trim();

            switch (opciones) {
                case "1" -> {
                    System.out.println("\n--- REGISTRO DE PACIENTE ---");
                    System.out.print("Cédula: ");
                    String cedula = captura.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = captura.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = captura.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = captura.nextLine();

                    Paciente p = new Paciente(cedula, nombre, apellido, telefono);
                    servicio.registrarPaciente(p);
                }
                case "2" -> {
                    System.out.println("\n--- REGISTRAR MÉDICO ---");
                    System.out.print("Nombre: ");
                    String nombre = captura.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = captura.nextLine();
                    System.out.print("Especialidad (GENERAL, PEDIATRIA, CARDIOLOGIA, URGENCIAS): ");
                    String espStr = captura.nextLine().trim().toUpperCase();

                    try {
                        Especialidad esp = Especialidad.valueOf(espStr);
                        Medico m = new Medico(nombre, apellido, esp);
                        servicio.registrarMedico(m);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Especialidad no válida.");
                    }
                }
                case "3" -> {
                    System.out.println("\n--- ASIGNAR TURNO ---");
                    System.out.print("Cédula del paciente: ");
                    String cedula = captura.nextLine().trim();
                    Paciente pac = servicio.buscarPorCedula(cedula);

                    System.out.print("Nombre del médico: ");
                    String nomMed = captura.nextLine().trim();
                    System.out.print("Apellido del médico: ");
                    String apeMed = captura.nextLine().trim();
                    Medico med = servicio.buscarPorNombreApellido(nomMed, apeMed);

                    if (pac == null || med == null) {
                        System.out.println("Error: Paciente o médico no encontrado.");
                        break;
                    }

                    System.out.print("Año: ");
                    int anio = Integer.parseInt(captura.nextLine().trim());
                    System.out.print("Mes (1-12): ");
                    int mes = Integer.parseInt(captura.nextLine().trim());
                    System.out.print("Día (1-31): ");
                    int dia = Integer.parseInt(captura.nextLine().trim());
                    System.out.print("Hora (0-23): ");
                    int hora = Integer.parseInt(captura.nextLine().trim());
                    System.out.print("Minuto (0-59): ");
                    int minuto = Integer.parseInt(captura.nextLine().trim());

                    LocalDateTime fechaHora = LocalDateTime.of(anio, mes, dia, hora, minuto);
                    Turno turno = new Turno(pac, med, fechaHora);
                    servicio.asignarTurno(turno);
                }
                case "4" -> {
                    System.out.print("Ingrese fecha (YYYY-MM-DD): ");
                    String fechaStr = captura.nextLine().trim();
                    try {
                        servicio.listarTurnosDelDia(LocalDate.parse(fechaStr)).forEach(System.out.println);
                    } catch (Exception e) {
                        System.out.println("Formato de fecha inválido. Debe ser YYYY-MM-DD.");
                    }
                }
                case "5" -> {
                    System.out.print("ID del turno a cancelar: ");
                    int id = Integer.parseInt(captura.nextLine().trim());
                    servicio.cancelarTurno(id);
                }
                case "6" -> {
                    System.out.print("Nombre del médico: ");
                    String nom = captura.nextLine().trim();
                    System.out.print("Apellido del médico: ");
                    String ape = captura.nextLine().trim();
                    Medico med = servicio.buscarPorNombreApellido(nom, ape);
                    if (med != null) {
                        servicio.buscarPorMedico(med).forEach(System.out.println);
                    } else {
                        System.out.println("Médico no encontrado.");
                    }
                }
                case "7" -> {
                    System.out.print("Cédula del paciente: ");
                    String ced = captura.nextLine().trim();
                    Paciente pac = servicio.buscarPorCedula(ced);
                    if (pac != null) {
                        servicio.buscarPorPaciente(pac).forEach(System.out.println);
                    } else {
                        System.out.println("Paciente no encontrado.");
                    }
                }
                case "8" -> {
                    System.out.print("ID del turno: ");
                    int id = Integer.parseInt(captura.nextLine().trim());
                    System.out.print("Nuevo estado (PENDIENTE, ATENDIDO, CANCELADO): ");
                    String estadoStr = captura.nextLine().trim().toUpperCase();

                    try {
                        EstadoTurno nuevoEstado = EstadoTurno.valueOf(estadoStr);
                        servicio.cambiarEstadoTurno(id, nuevoEstado);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Estado no válido.");
                    }
                }
                case "9" -> servicio.listarPacientes();
                case "10" -> servicio.listarMedicos();
                case "0" -> {
                    DatosCSV.guardar(servicio);
                    System.out.println("Hasta pronto. Datos guardados.");
                    salir = true;
                }
                default -> System.out.println("---------[ERROR]-----\nOpción no válida.");
            }
            System.out.println();
        }
    }

    private static void Menu() {
        String separador = "=".repeat(50);
        System.out.println(separador);
        System.out.printf("| %-46s |\n", "CLINICAAPP - MENÚ");
        System.out.println(separador);
        System.out.printf("| %-46s |\n", "1. Registrar paciente");
        System.out.printf("| %-46s |\n", "2. Registrar médico");
        System.out.printf("| %-46s |\n", "3. Asignar turno");
        System.out.printf("| %-46s |\n", "4. Listar turnos del día");
        System.out.printf("| %-46s |\n", "5. Cancelar turno");
        System.out.printf("| %-46s |\n", "6. Ver turnos por médico");
        System.out.printf("| %-46s |\n", "7. Ver turnos por paciente");
        System.out.printf("| %-46s |\n", "8. Cambiar estado de turno");
        System.out.printf("| %-46s |\n", "9. Listar pacientes");
        System.out.printf("| %-46s |\n", "10. Listar médicos");
        System.out.printf("| %-46s |\n", "0. Salir");
        System.out.println(separador);
    }
}