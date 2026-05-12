package com.rednorte.PortalPacientes.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaPacienteDTO {
    private String rutPaciente;
    private LocalDateTime fechaConsulta;
    private String ipOrigen;
}
