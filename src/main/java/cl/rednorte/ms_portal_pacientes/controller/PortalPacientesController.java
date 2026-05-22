package cl.rednorte.ms_portal_pacientes.controller;

import cl.rednorte.ms_portal_pacientes.dto.response.PageResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudDetalleResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudResumenResponse;
import cl.rednorte.ms_portal_pacientes.service.PortalPacientesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portal/solicitudes")
@RequiredArgsConstructor
public class PortalPacientesController {

    private final PortalPacientesService portalPacientesService;

    @GetMapping
    public ResponseEntity<PageResponse<SolicitudResumenResponse>> obtenerSolicitudes(
            @RequestParam String rutPaciente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        return ResponseEntity.ok(
            portalPacientesService.obtenerSolicitudes(rutPaciente, page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDetalleResponse> obtenerDetalle(
            @PathVariable Long id,
            @RequestParam String rutPaciente) {

        return ResponseEntity.ok(
            portalPacientesService.obtenerDetalle(id, rutPaciente)
        );
    }
}