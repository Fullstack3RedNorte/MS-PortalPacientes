package cl.rednorte.ms_portal_pacientes.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistorialEstadoResponse {

    private String estadoAnterior;
    private String estadoNuevo;
    private String motivo;
    private LocalDateTime fechaCambio;
}