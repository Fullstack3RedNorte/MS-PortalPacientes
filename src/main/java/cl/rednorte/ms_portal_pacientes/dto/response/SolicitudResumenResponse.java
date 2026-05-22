package cl.rednorte.ms_portal_pacientes.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudResumenResponse {

    private Long id;
    private String especialidad;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCita;
    private String documentosRequeridos;
}