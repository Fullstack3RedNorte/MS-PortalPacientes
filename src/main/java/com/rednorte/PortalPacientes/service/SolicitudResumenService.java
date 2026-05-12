package com.rednorte.PortalPacientes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rednorte.PortalPacientes.dto.SolicitudResumenDTO;
import com.rednorte.PortalPacientes.repository.SolicitudResumenRepository;

@Service
public class SolicitudResumenService {

    private final SolicitudResumenRepository solicitudResumenRepository;

    public SolicitudResumenService(SolicitudResumenRepository solicitudResumenRepository) {
        this.solicitudResumenRepository = solicitudResumenRepository;
    }

    public List<SolicitudResumenDTO> obtenerPorRut(String rut) {
        return solicitudResumenRepository.obtenerPorRut(rut);
    }
}
