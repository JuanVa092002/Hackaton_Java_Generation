package clinica.data;

import clinica.service.ClinicaService;
import clinica.model.Paciente;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class DatosCSV {
    private static final String DIR = "datos/" + File.separator;
    private static final String F_PACIENTES = DIR + "pacientes.csv";
    private static final String F_MEDICOS = DIR + "medicos.csv";
    private static final String F_TURNOS = DIR + "turnos.csv";

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void cargar(ClinicaService servicio) {
        new File(DIR).mkdirs();
        cargarPacientes(servicio);
    }

    private static void cargarPacientes(ClinicaService servicio) {
        File f = new File(F_PACIENTES);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;

                String[] p = linea.split(",", -1);

                // CORRECCIÓN: Se añadieron los índices, [1], [2], [3], [4] a cada elemento del arreglo
                servicio.getPacientes().add(new Paciente(
                        Integer.parseInt(p[0].trim()),
                        p[1].trim(),
                        p[2].trim(),
                        p[3].trim(),
                        p[4].trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar pacientes: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error de formato numérico en el ID: " + e.getMessage());
        }
    }

    // CORRECCIÓN: Se añade el método guardar que invoca tu Main.java en la opción 0
    public static void guardar(ClinicaService servicio) {
        new File(DIR).mkdirs();
        File f = new File(F_PACIENTES);

        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (Paciente p : servicio.getPacientes()) {
                // Ajusta estos métodos según los getters reales de tu clase Paciente (ej: p.getId(), p.getCedula()...)
                pw.println(p.getId() + "," + p.getCedula() + "," + p.getNombre() + "," + p.getApellido() + "," + p.getTelefono());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar pacientes: " + e.getMessage());
        }
    }
}
