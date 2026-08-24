package clinica.interfaces;

import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.model.Turno;

import java.time.LocalDate;
import java.util.*;

public interface Consultable {

    List<Turno> listarTurnosDelDia(LocalDate fecha);

    List<Turno> buscarPorMedico(Medico medico);

    List<Turno> buscarPorPaciente(Paciente paciente);
}