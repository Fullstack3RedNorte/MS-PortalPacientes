package cl.rednorte.ms_portal_pacientes.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SolicitudDetalleResponse {

    private Long id;
    private String especialidad;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCita;
    private List<HistorialEstadoResponse> historial;
}