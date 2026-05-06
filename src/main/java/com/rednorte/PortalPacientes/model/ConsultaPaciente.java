package com.rednorte.PortalPacientes.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consulta_paciente")

public class ConsultaPaciente {
    @Id
    private Long id_consulta;

    private String rutPaciente;
    private LocalDateTime fechaConsulta;
    private String ipOrigen;

}
