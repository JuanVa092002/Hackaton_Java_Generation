package interfaces;

<<<<<<< HEAD
import model.Medico;
import model.Paciente;
import model.Turno;
=======
import clinica.model.Medico;
import clinica.model.Paciente;
import clinica.model.Turno;
>>>>>>> main

import java.time.LocalDate;
import  java.util.List;


public interface Consultable {
    List<Turno> listarTurnosDelDia(LocalDate fecha);

    List<Turno> buscarPorMedico(Medico medico);

    List<Turno> buscarPorPaciente(Paciente paciente);
}
