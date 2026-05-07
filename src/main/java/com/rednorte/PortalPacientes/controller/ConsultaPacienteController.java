package com.rednorte.PortalPacientes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rednorte.PortalPacientes.model.SolicitudResumen;
import com.rednorte.PortalPacientes.service.ConsultaPacienteService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaPacienteController {

    private final ConsultaPacienteService consultaPacienteService;

    public ConsultaPacienteController(ConsultaPacienteService consultaPacienteService) {
        this.consultaPacienteService = consultaPacienteService;
    }

    @PostMapping("/{rut}")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrarConsulta(@PathVariable String rut, HttpServletRequest request) {
        consultaPacienteService.registrarConsulta(rut, request.getRemoteAddr());
    }

    @GetMapping("/{rut}/resumen")
    public List<SolicitudResumen> obtenerResumenPorRut(@PathVariable String rut) {
        return consultaPacienteService.obtenerPorRut(rut);
    }
}
