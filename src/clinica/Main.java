package clinica;

import clinica.data.DatosCSV;
import clinica.model.Especialidad;
import clinica.model.EstadoTurno;
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.model.Turno;
import clinica.service.ClinicaService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ClinicaService servicio = new ClinicaService();
        DatosCSV.cargar(servicio);

        Scanner captura = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            Menu();
            System.out.print("Por favor seleccione una opcion: ");
            String opciones = captura.nextLine().trim();

            switch (opciones) {
                case "1" -> registrarPaciente(captura, servicio);
                case "2" -> registrarMedico(captura, servicio);
                case "3" -> asignarTurno(captura, servicio);
                case "4" -> listarTurnosDelDia(captura, servicio);
                case "5" -> cancelarTurno(captura, servicio);
                case "6" -> turnosPorMedico(captura, servicio);
                case "7" -> turnosPorPaciente(captura, servicio);
                case "8" -> cambiarEstado(captura, servicio);
                case "9" -> servicio.listarPacientes();
                case "10" -> servicio.listarMedicos();
                case "0" -> {
                    DatosCSV.guardar(servicio);
                    System.out.println("Hasta pronto. Datos guardados.");
                    salir = true;
                }
                default -> System.out.println("---------[ERROR]-----\nOpcion no valida.");
            }
            System.out.println();
        }
    }

    private static void registrarPaciente(Scanner captura, ClinicaService servicio) {
        System.out.println("\n--- REGISTRO DE PACIENTE ---");
        String cedula = pedirCedula(captura, "Cedula: ");
        String nombre = pedirSoloLetras(captura, "Nombre: ");
        String apellido = pedirSoloLetras(captura, "Apellido: ");
        String telefono = pedirTelefono(captura);
        try {
            Paciente p = new Paciente(cedula, nombre, apellido, telefono);
            servicio.registrarPaciente(p);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    private static void registrarMedico(Scanner captura, ClinicaService servicio) {
        System.out.println("\n--- REGISTRAR MEDICO ---");
        String nombre = pedirSoloLetras(captura, "Nombre: ");
        String apellido = pedirSoloLetras(captura, "Apellido: ");
        Especialidad esp = pedirEspecialidad(captura);
        try {
            Medico m = new Medico(nombre, apellido, esp);
            servicio.registrarMedico(m);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    private static void asignarTurno(Scanner captura, ClinicaService servicio) {
        System.out.println("\n--- ASIGNAR TURNO ---");
        String cedula = pedirCedula(captura, "Cedula del paciente: ");
        Paciente pac = servicio.buscarPorCedula(cedula);
        if (pac == null) {
            System.out.println("Error: no hay un paciente con esa cedula.");
            return;
        }

        String nomMed = pedirSoloLetras(captura, "Nombre del medico: ");
        String apeMed = pedirSoloLetras(captura, "Apellido del medico: ");
        Medico med = servicio.buscarPorNombreApellido(nomMed, apeMed);
        if (med == null) {
            System.out.println("Error: no hay un medico con ese nombre y apellido.");
            return;
        }

        while (true) {
            int anio = pedirEntero(captura, "Anio: ", 2020, 2100);
            int mes = pedirEntero(captura, "Mes (1-12): ", 1, 12);
            int dia = pedirEntero(captura, "Dia (1-31): ", 1, 31);
            int hora = pedirEntero(captura, "Hora (0-23): ", 0, 23);
            int minuto = pedirEntero(captura, "Minuto (0-59): ", 0, 59);
            try {
                LocalDateTime fechaHora = LocalDateTime.of(anio, mes, dia, hora, minuto);
                Turno turno = new Turno(pac, med, fechaHora);
                servicio.asignarTurno(turno);
                break;
            } catch (DateTimeException e) {
                System.out.println("Error: esa fecha no existe (revisa dia y mes).");
            }
        }
    }

    private static void listarTurnosDelDia(Scanner captura, ClinicaService servicio) {
        LocalDate fecha = null;
        while (fecha == null) {
            System.out.print("Ingrese fecha (YYYY-MM-DD): ");
            String fechaStr = captura.nextLine().trim();
            if (!fechaStr.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) {
                System.out.println("Formato invalido. Use YYYY-MM-DD (ejemplo: 2026-08-28).");
                continue;
            }
            try {
                fecha = LocalDate.parse(fechaStr);
            } catch (Exception e) {
                System.out.println("Formato invalido. Use YYYY-MM-DD (ejemplo: 2026-08-28).");
            }
        }
        List<Turno> lista = servicio.listarTurnosDelDia(fecha);
        if (lista.isEmpty()) {
            System.out.println("No hay turnos para esa fecha.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println(lista.get(i));
            }
        }
    }

    private static void cancelarTurno(Scanner captura, ClinicaService servicio) {
        int id = pedirEntero(captura, "ID del turno a cancelar: ", 1, 999999);
        servicio.cancelarTurno(id);
    }

    private static void turnosPorMedico(Scanner captura, ClinicaService servicio) {
        String nom = pedirSoloLetras(captura, "Nombre del medico: ");
        String ape = pedirSoloLetras(captura, "Apellido del medico: ");
        Medico med = servicio.buscarPorNombreApellido(nom, ape);
        if (med == null) {
            System.out.println("Medico no encontrado.");
            return;
        }
        List<Turno> lista = servicio.buscarPorMedico(med);
        if (lista.isEmpty()) {
            System.out.println("Ese medico no tiene turnos.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println(lista.get(i));
            }
        }
    }

    private static void turnosPorPaciente(Scanner captura, ClinicaService servicio) {
        String ced = pedirCedula(captura, "Cedula del paciente: ");
        Paciente pac = servicio.buscarPorCedula(ced);
        if (pac == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }
        List<Turno> lista = servicio.buscarPorPaciente(pac);
        if (lista.isEmpty()) {
            System.out.println("Ese paciente no tiene turnos.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println(lista.get(i));
            }
        }
    }

    private static void cambiarEstado(Scanner captura, ClinicaService servicio) {
        int id = pedirEntero(captura, "ID del turno: ", 1, 999999);
        EstadoTurno nuevoEstado = pedirEstado(captura);
        servicio.cambiarEstadoTurno(id, nuevoEstado);
    }

    private static String pedirCedula(Scanner captura, String texto) {
        while (true) {
            System.out.print(texto);
            String valor = captura.nextLine().trim();
            if (valor.matches("^[0-9]{6,10}$")) {
                return valor;
            }
            System.out.println("Error: la cedula solo numeros, entre 6 y 10 digitos.");
        }
    }

    private static String pedirSoloLetras(Scanner captura, String texto) {
        while (true) {
            System.out.print(texto);
            String valor = captura.nextLine().trim();
            if (valor.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+([ ]+[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$")) {
                return valor;
            }
            System.out.println("Error: solo letras, sin numeros ni simbolos.");
        }
    }

    private static String pedirTelefono(Scanner captura) {
        while (true) {
            System.out.print("Telefono: ");
            String valor = captura.nextLine().trim();
            if (valor.matches("^[0-9]{7,10}$")) {
                return valor;
            }
            System.out.println("Error: el telefono solo numeros, entre 7 y 10 digitos.");
        }
    }

    private static Especialidad pedirEspecialidad(Scanner captura) {
        while (true) {
            System.out.print("Especialidad (GENERAL, PEDIATRIA, CARDIOLOGIA, URGENCIAS): ");
            String valor = captura.nextLine().trim().toUpperCase();
            try {
                return Especialidad.valueOf(valor);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: especialidad no valida.");
            }
        }
    }

    private static EstadoTurno pedirEstado(Scanner captura) {
        while (true) {
            System.out.print("Nuevo estado (PENDIENTE, ATENDIDO, CANCELADO): ");
            String valor = captura.nextLine().trim().toUpperCase();
            try {
                return EstadoTurno.valueOf(valor);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: estado no valido. Use PENDIENTE, ATENDIDO o CANCELADO.");
            }
        }
    }

    private static int pedirEntero(Scanner captura, String texto, int min, int max) {
        while (true) {
            System.out.print(texto);
            String valor = captura.nextLine().trim();
            try {
                int n = Integer.parseInt(valor);
                if (n < min || n > max) {
                    System.out.println("Error: el numero debe estar entre " + min + " y " + max + ".");
                } else {
                    return n;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: debe escribir un numero, no letras.");
            }
        }
    }

    private static void Menu() {
        String separador = "=".repeat(50);
        System.out.println(separador);
        System.out.printf("| %-46s |\n", "              CLINICA DPJ - MENU");
        System.out.println(separador);
        System.out.printf("| %-46s |\n", "1. Registrar paciente");
        System.out.printf("| %-46s |\n", "2. Registrar medico");
        System.out.printf("| %-46s |\n", "3. Asignar turno");
        System.out.printf("| %-46s |\n", "4. Listar turnos del día");
        System.out.printf("| %-46s |\n", "5. Cancelar turno");
        System.out.printf("| %-46s |\n", "6. Ver turnos por medico");
        System.out.printf("| %-46s |\n", "7. Ver turnos por paciente");
        System.out.printf("| %-46s |\n", "8. Cambiar estado de turno");
        System.out.printf("| %-46s |\n", "9. Listar pacientes");
        System.out.printf("| %-46s |\n", "10. Listar medicos");
        System.out.printf("| %-46s |\n", "0. Salir");
        System.out.printf("| %-46s |\n", "RECUERDE QUE:");
        System.out.printf("| %-46s |\n", "AL SALIR LOS DATOS SERAN GUARDADOS");
        System.out.println(separador);
    }
}
