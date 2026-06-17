package cl.rednorte.ms_portal_pacientes.service.impl;

import cl.rednorte.ms_portal_pacientes.dto.response.PageResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudDetalleResponse;
import cl.rednorte.ms_portal_pacientes.dto.response.SolicitudResumenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
/**
 * Pruebas unitarias para `PortalPacientesServiceImpl`.
 *
 * Cada prueba sigue el patrón Given/When/Then:
 * - Given: condiciones y mocks iniciales
 * - When: llamada al método bajo prueba
 * - Then: aserciones sobre el resultado o la excepción esperada
 */
class PortalPacientesServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PortalPacientesServiceImpl service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "listaEsperaBaseUrl", "http://localhost:8085");
    }

        /**
         * Given: RestTemplate devuelve una PageResponse con una única solicitud.
         * When: se invoca `obtenerSolicitudes` con rut válido y paging básico.
         * Then: se retorna la página con el contenido esperado (size 1 y id 1).
         */
        @Test
        void obtenerSolicitudes_success() {
        SolicitudResumenResponse resumen = new SolicitudResumenResponse();
        resumen.setId(1L);
        resumen.setEspecialidad("Cardiología");
        PageResponse<SolicitudResumenResponse> page = new PageResponse<>(List.of(resumen), 1, 1, 0);

        ResponseEntity<PageResponse<SolicitudResumenResponse>> respEntity = new ResponseEntity<>(page, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), isNull(), Mockito.<ParameterizedTypeReference<PageResponse<SolicitudResumenResponse>>>any()
        )).thenReturn(respEntity);

        PageResponse<SolicitudResumenResponse> result = service.obtenerSolicitudes("12345678-9", 0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
    }

        /**
         * Given: RestTemplate devuelve una ResponseEntity con body null.
         * When: se invoca `obtenerSolicitudes`.
         * Then: el método devuelve null (comportamiento actual) en vez de lanzar excepción.
         */
        @Test
        void obtenerSolicitudes_nullBody_returnsNull() {
                ResponseEntity<PageResponse<SolicitudResumenResponse>> respEntity = new ResponseEntity<>(null, HttpStatus.OK);

                Mockito.when(restTemplate.exchange(
                                anyString(), eq(HttpMethod.GET), isNull(), Mockito.<ParameterizedTypeReference<PageResponse<SolicitudResumenResponse>>>any()
                )).thenReturn(respEntity);

                PageResponse<SolicitudResumenResponse> result = service.obtenerSolicitudes("999", 1, 10);

                assertNull(result);
        }

        /**
         * Given: RestTemplate será invocado con una URL construida a partir de rut/page/size.
         * When: se llama a `obtenerSolicitudes`.
         * Then: la URL pasada al RestTemplate contiene el `rutPaciente`, `page` y `size`.
         */
        @Test
        void obtenerSolicitudes_buildsUrlWithRutAndPagination() {
                SolicitudResumenResponse resumen = new SolicitudResumenResponse();
                resumen.setId(2L);
                PageResponse<SolicitudResumenResponse> page = new PageResponse<>(List.of(resumen), 1, 1, 3);

                ResponseEntity<PageResponse<SolicitudResumenResponse>> respEntity = new ResponseEntity<>(page, HttpStatus.OK);

                ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

                Mockito.when(restTemplate.exchange(
                                urlCaptor.capture(), eq(HttpMethod.GET), isNull(), Mockito.<ParameterizedTypeReference<PageResponse<SolicitudResumenResponse>>>any()
                )).thenReturn(respEntity);

                PageResponse<SolicitudResumenResponse> result = service.obtenerSolicitudes("5555-6", 3, 7);

                assertNotNull(result);
                String usedUrl = urlCaptor.getValue();
                assertTrue(usedUrl.contains("rutPaciente=5555-6"));
                assertTrue(usedUrl.contains("page=3"));
                assertTrue(usedUrl.contains("size=7"));
        }

        /**
         * Given: RestTemplate lanza una excepción (simula servicio externo caído).
         * When: se invoca `obtenerSolicitudes`.
         * Then: se propaga un `ResponseStatusException` con status 503.
         */
        @Test
        void obtenerSolicitudes_serviceUnavailableOnException() {
        Mockito.when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("connection error"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.obtenerSolicitudes("123", 0, 20));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatusCode());
    }

        /**
         * Given: RestTemplate devuelve un `SolicitudDetalleResponse` no nulo.
         * When: se invoca `obtenerDetalle` con id existente.
         * Then: se retorna el detalle con el id esperado.
         */
        @Test
        void obtenerDetalle_success() {
        SolicitudDetalleResponse detalle = new SolicitudDetalleResponse();
        detalle.setId(1L);
        detalle.setEspecialidad("Cardiología");
        detalle.setFechaRegistro(LocalDateTime.now());

        Mockito.when(restTemplate.getForObject(anyString(), eq(SolicitudDetalleResponse.class)))
                .thenReturn(detalle);

        SolicitudDetalleResponse result = service.obtenerDetalle(1L, "12345678-9");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

        /**
         * Given: RestTemplate devuelve null (detalle no encontrado en servicio externo).
         * When: se invoca `obtenerDetalle`.
         * Then: se lanza `ResponseStatusException` con status 404.
         */
        @Test
        void obtenerDetalle_notFoundWhenNull() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(SolicitudDetalleResponse.class)))
                .thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.obtenerDetalle(1L, "123"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

        /**
         * Given: RestTemplate lanza una excepción al solicitar el detalle.
         * When: se invoca `obtenerDetalle`.
         * Then: se lanza `ResponseStatusException` con status 503.
         */
        @Test
        void obtenerDetalle_serviceUnavailableOnException() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(SolicitudDetalleResponse.class)))
                .thenThrow(new RuntimeException("down"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.obtenerDetalle(1L, "123"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatusCode());
    }
}
