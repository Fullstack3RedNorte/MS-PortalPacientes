package com.rednorte.PortalPacientes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rednorte.PortalPacientes.model.ConsultaPaciente;

public interface ConsultaPacienteRepository extends JpaRepository<ConsultaPaciente, Long> {
    List<ConsultaPaciente> findByRutPacienteOrderByFechaConsultaDesc(String rutPaciente);
}
