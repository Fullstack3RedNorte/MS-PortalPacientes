package com.rednorte.PortalPacientes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rednorte.PortalPacientes.model.ConsultaPaciente;
import com.rednorte.PortalPacientes.model.SolicitudResumen;
import com.rednorte.PortalPacientes.repository.ConsultaPacienteRepository;

@Service
public class ConsultaPacienteService {

    private final ConsultaPacienteRepository consultaPacienteRepository;

    public ConsultaPacienteService(ConsultaPacienteRepository consultaPacienteRepository) {
        this.consultaPacienteRepository = consultaPacienteRepository;
    }

    public void registrarConsulta(String rut, String ipOrigen) {
        ConsultaPaciente consulta = new ConsultaPaciente();
        consulta.registrarConsulta(rut, ipOrigen);
        consultaPacienteRepository.save(consulta);
    }

    public List<SolicitudResumen> obtenerPorRut(String rut) {
        return consultaPacienteRepository.findByRutPacienteOrderByFechaConsultaDesc(rut)
                .stream()
                .map(consulta -> {
                    SolicitudResumen resumen = new SolicitudResumen();
                    resumen.setEspecialidad("Medicina General");
                    resumen.setEstado("REGISTRADA");
                    resumen.setFechaRegistro(consulta.getFechaConsulta());
                    resumen.setFechaCita((LocalDateTime) null);
                    resumen.setDocumentosRequeridos("Carnet de identidad");
                    return resumen;
                })
                .toList();
    }
}
