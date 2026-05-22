package cl.rednorte.ms_portal_pacientes.service.impl;

import cl.rednorte.ms_portal_pacientes.dto.response.PageResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudDetalleResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudResumenResponse;
import cl.rednorte.ms_portal_pacientes.service.PortalPacientesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortalPacientesServiceImpl implements PortalPacientesService {

    private final RestTemplate restTemplate;

    @Value("${ms.lista-espera.base-url}")
    private String listaEsperaBaseUrl;

    @Override
    public PageResponse<SolicitudResumenResponse> obtenerSolicitudes(
            String rutPaciente, int page, int size) {

        String url = listaEsperaBaseUrl + "/solicitudes?rutPaciente=" + rutPaciente
                + "&page=" + page + "&size=" + size;

        try {
            PageResponse<SolicitudResumenResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<SolicitudResumenResponse>>() {}
            ).getBody();

            return response;

        } catch (Exception e) {
            log.error("Error consultando MS Lista de Espera: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Servicio no disponible temporalmente");
        }
    }

    @Override
    public SolicitudDetalleResponse obtenerDetalle(Long id, String rutPaciente) {

        String url = listaEsperaBaseUrl + "/solicitudes/" + id;

        try {
            SolicitudDetalleResponse response = restTemplate.getForObject(
                url, SolicitudDetalleResponse.class);

            if (response == null) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Solicitud no encontrada");
            }

            return response;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error consultando detalle: {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Servicio no disponible temporalmente");
        }
    }
}