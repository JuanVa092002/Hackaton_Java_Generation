package src.datos;
import src.model.*;
import src.service.ClinicaService;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; import java.util.List;
public class DatosCSV {
    private static final String DIR = "datos/";
    private static final String F_PACIENTES = DIR + "pacientes.csv";
    private static final String F_MEDICOS = DIR + "medicos.csv";
    private static final String F_TURNOS = DIR + "turnos.csv";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static void cargar(ClinicaService servicio) {
        new File(DIR).mkdirs();
        cargarPacientes(servicio); cargarMedicos(servicio); cargarTurnos(servicio);
    }
    private static void cargarPacientes(ClinicaService servicio) {
        File f = new File(F_PACIENTES); if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] p = linea.split(",", -1); // id,cedula,nombre,apellido,telefon
                o
                servicio.getPacientes().add(new Paciente(
                        Integer.parseInt(p[0].trim()),
                        p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim()));
            }
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }