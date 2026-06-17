package cl.rednorte.ms_portal_pacientes.controller;

import cl.rednorte.ms_portal_pacientes.dto.response.PageResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudDetalleResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudResumenResponse;
import cl.rednorte.ms_portal_pacientes.service.PortalPacientesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas unitarias para `PortalPacientesController`.
 *
 * Cada prueba documenta el escenario esperado (Given/When/Then) y valida
 * la respuesta HTTP y el contenido JSON devuelto por el controlador.
 */
@WebMvcTest(PortalPacientesController.class)
class PortalPacientesControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PortalPacientesService service;


    @BeforeEach
    void setup() {
        service = Mockito.mock(PortalPacientesService.class);
        PortalPacientesController controller = new PortalPacientesController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Given: el servicio retorna una página con una solicitud.
     * When: se solicita GET /solicitudes?rutPaciente=123
     * Then: el controlador responde 200 OK y el JSON contiene el id esperado.
     */
    @Test
    void obtenerSolicitudes_returnsOkAndContent() throws Exception {
        SolicitudResumenResponse resumen = new SolicitudResumenResponse();
        resumen.setId(1L);
        resumen.setEspecialidad("Cardiología");
        PageResponse<SolicitudResumenResponse> page = new PageResponse<>(List.of(resumen), 1, 1, 0);

        Mockito.when(service.obtenerSolicitudes("123", 0, 20)).thenReturn(page);

        mockMvc.perform(get("/solicitudes").param("rutPaciente", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    /**
     * Given: no se provee `rutPaciente` en la petición.
     * When: se solicita GET /solicitudes sin el parámetro requerido.
     * Then: el framework valida la falta del parámetro y el endpoint responde 400 Bad Request.
     */
    @Test
    void obtenerSolicitudes_missingRutPaciente_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/solicitudes"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Given: se pasan parámetros `page` y `size` en la petición.
     * When: se invoca GET /solicitudes con dichos parámetros.
     * Then: el controlador delega al servicio con los valores de paginación recibidos.
     */
    @Test
    void obtenerSolicitudes_forwardsPaginationParametersToService() throws Exception {
        SolicitudResumenResponse resumen = new SolicitudResumenResponse();
        resumen.setId(1L);
        PageResponse<SolicitudResumenResponse> page = new PageResponse<>(List.of(resumen), 1, 1, 2);

        Mockito.when(service.obtenerSolicitudes("123", 2, 5)).thenReturn(page);

        mockMvc.perform(get("/solicitudes")
                .param("rutPaciente", "123")
                .param("page", "2")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(2));

        Mockito.verify(service).obtenerSolicitudes("123", 2, 5);
    }

    /**
     * Given: el servicio retorna un detalle válido para id 1.
     * When: se solicita GET /solicitudes/1?rutPaciente=123
     * Then: el controlador responde 200 OK y el JSON contiene el id del detalle.
     */
    @Test
    void obtenerDetalle_returnsOk() throws Exception {
        SolicitudDetalleResponse detalle = new SolicitudDetalleResponse();
        detalle.setId(1L);
        detalle.setEspecialidad("Cardiología");
        detalle.setFechaRegistro(LocalDateTime.now());

        Mockito.when(service.obtenerDetalle(1L, "123")).thenReturn(detalle);

        mockMvc.perform(get("/solicitudes/1").param("rutPaciente", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    /**
     * Given: el servicio lanza ResponseStatusException 404 para el id.
     * When: se solicita GET /solicitudes/1?rutPaciente=123
     * Then: el controlador propaga 404 Not Found.
     */
    @Test
    void obtenerDetalle_returnsNotFoundWhenServiceThrows404() throws Exception {
        Mockito.when(service.obtenerDetalle(1L, "123"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        mockMvc.perform(get("/solicitudes/1").param("rutPaciente", "123"))
                .andExpect(status().isNotFound());
    }
}
