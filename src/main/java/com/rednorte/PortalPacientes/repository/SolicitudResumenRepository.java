package com.rednorte.PortalPacientes.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rednorte.PortalPacientes.dto.SolicitudResumenDTO;

@Repository
public class SolicitudResumenRepository {

    private final ConsultaPacienteRepository consultaPacienteRepository;

    public SolicitudResumenRepository(ConsultaPacienteRepository consultaPacienteRepository) {
        this.consultaPacienteRepository = consultaPacienteRepository;
    }

    public List<SolicitudResumenDTO> obtenerPorRut(String rut) {
        return consultaPacienteRepository.findByRutPacienteOrderByFechaConsultaDesc(rut)
                .stream()
                .map(consulta -> {
                    SolicitudResumenDTO resumen = new SolicitudResumenDTO();
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
