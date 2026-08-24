package interfaces;



import model.Medico;
import model.Paciente;
import model.Turno;

import java.time.LocalDate;
import  java.util.List;


public interface Consultable {
    List<Turno> listarTurnosDelDia(LocalDate fecha);

    List<Turno> buscarPorMedico(Medico medico);

    List<Turno> buscarPorPaciente(Paciente paciente);
}
