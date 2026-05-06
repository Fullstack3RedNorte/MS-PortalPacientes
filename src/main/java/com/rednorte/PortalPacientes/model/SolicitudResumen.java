package com.rednorte.PortalPacientes.model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitud_resumen")

public class SolicitudResumen {
    private String especialidad;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCita;
    private String docsRequeridos;
}
