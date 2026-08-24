package clinica.data;

import clinica.model.Especialidad;
import clinica.model.EstadoTurno;
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.model.Turno;
import clinica.service.ClinicaService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatosCSV {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static File carpetaDatos() {
        File actual = new File(System.getProperty("user.dir"));
        File cursor = actual;
        for (int i = 0; i < 5; i++) {
            if (new File(cursor, "src").isDirectory()) {
                File datos = new File(cursor, "datos");
                datos.mkdirs();
                return datos;
            }
            File padre = cursor.getParentFile();
            if (padre == null) {
                break;
            }
            cursor = padre;
        }
        File datos = new File(actual, "datos");
        datos.mkdirs();
        return datos;
    }

    private static File archivo(String nombre) {
        return new File(carpetaDatos(), nombre);
    }

    public static void cargar(ClinicaService servicio) {
        carpetaDatos();
        cargarPacientes(servicio);
        cargarMedicos(servicio);
        cargarTurnos(servicio);
    }

    private static void cargarPacientes(ClinicaService servicio) {
        File f = archivo("pacientes.csv");
        if (!f.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }

                String[] p = linea.split(",", -1);

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

    private static void cargarMedicos(ClinicaService servicio) {
        File f = archivo("medicos.csv");
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] p = linea.split(",", -1);
                servicio.getMedicos().add(new Medico(
                        Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                        Especialidad.valueOf(p[3].trim())));
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cargarTurnos(ClinicaService servicio) {
        File f = archivo("turnos.csv");
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] p = linea.split(",", -1);
                Paciente pac = servicio.buscarPorCedula(p[1].trim());
                Medico med = servicio.buscarPorNombreApellido(p[2].trim(), p[3].trim());
                if (pac == null || med == null) {
                    continue;
                }
                servicio.getTurnos().add(new Turno(
                        Integer.parseInt(p[0].trim()), pac, med,
                        LocalDateTime.parse(p[4].trim(), FMT),
                        EstadoTurno.valueOf(p[5].trim())));
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void guardar(ClinicaService servicio) {
        carpetaDatos();
        guardarPacientes(servicio.getPacientes());
        guardarMedicos(servicio.getMedicos());
        guardarTurnos(servicio.getTurnos());
    }

    private static void guardarPacientes(List<Paciente> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo("pacientes.csv")))) {
            for (Paciente p : lista) {
                pw.println(p.getId() + "," + p.getCedula() + "," + p.getNombre() + "," + p.getApellido() + "," + p.getTelefono());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar pacientes: " + e.getMessage());
        }
    }

    private static void guardarMedicos(List<Medico> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo("medicos.csv")))) {
            for (Medico m : lista) {
                pw.println(m.getId() + "," + m.getNombre() + "," + m.getApellido() + "," + m.getEspecialidad());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar médicos: " + e.getMessage());
        }
    }

    private static void guardarTurnos(List<Turno> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo("turnos.csv")))) {
            for (Turno t : lista) {
                pw.println(t.getId() + "," +
                        t.getPaciente().getCedula() + "," +
                        t.getMedico().getNombre() + "," +
                        t.getMedico().getApellido() + "," +
                        t.getFechaHora().format(FMT) + "," +
                        t.getEstado());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar turnos: " + e.getMessage());
        }
    }
}
