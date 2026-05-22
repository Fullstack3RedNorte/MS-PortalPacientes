package cl.rednorte.ms_portal_pacientes.service;

import cl.rednorte.ms_portal_pacientes.dto.response.PageResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudDetalleResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudResumenResponse;

public interface PortalPacientesService {

    PageResponse<SolicitudResumenResponse> obtenerSolicitudes(String rutPaciente, int page, int size);
    SolicitudDetalleResponse obtenerDetalle(Long id, String rutPaciente);
}