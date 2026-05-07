package com.rednorte.PortalPacientes.model;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SolicitudResumen {
    private String especialidad;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCita;
    private String documentosRequeridos;
}
