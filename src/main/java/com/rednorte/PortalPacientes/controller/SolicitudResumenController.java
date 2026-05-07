package com.rednorte.PortalPacientes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rednorte.PortalPacientes.model.SolicitudResumen;
import com.rednorte.PortalPacientes.service.SolicitudResumenService;

@RestController
@RequestMapping("/api/solicitudes-resumen")
public class SolicitudResumenController {

    private final SolicitudResumenService solicitudResumenService;

    public SolicitudResumenController(SolicitudResumenService solicitudResumenService) {
        this.solicitudResumenService = solicitudResumenService;
    }

    @GetMapping("/{rut}")
    public List<SolicitudResumen> obtenerPorRut(@PathVariable String rut) {
        return solicitudResumenService.obtenerPorRut(rut);
    }
}
