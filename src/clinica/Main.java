package clinica;

import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        // ClinicaServece servicio = new ClinicaServece();
        // DatosCSV.cargar(servicio);
        Scanner captura = new Scanner(System.in);
        boolean salir = false;
        while(!salir){
            // menu();
            System.out.println("Porfavor selecione una opcion: ");
            String opciones = captura.nextLine().trim();
            switch (opciones){
                case "1" ->{
                    System.out.println("\nRegistro de Paciente");
                    System.out.println("Cedula: ");
                    String cedula = captura.nextLine();
                    System.out.println("Nombre: ");
                    String nombre = captura.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();
                    Paciente p = new Paciente(cedula, nombre, apellido, telefono);
                    servicio.resgistrarPaciente(p);
                }
                case "2" ->{
                    System.out.println("\n--- REGISTRAR MÉDICO ---");
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = scanner.nextLine();
                    System.out.print("Especialidad (GENERAL, PEDIATRIA, CARDIOLOGIA, URGENCIAS): ");
                    String espStr = scanner.nextLine().trim().toUpperCase();
                    try {
                        Especialidad esp = Especialidad.valueOf(espStr);
                        Medico m = new Medico(nombre, apellido, esp);
                        servicio.registrarMedico(m);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Especialidad no válida.");
                    }
                }
                case "3" ->{

                }
                case "4" ->{

                }
                case "5" ->{

                }
                case "6" ->{

                }
                case "7" ->{

                }
                case "8" ->{

                }
                case "9" ->{

                }
                case "10" ->{

                }

            }

        }
        captura.close();
    }
}