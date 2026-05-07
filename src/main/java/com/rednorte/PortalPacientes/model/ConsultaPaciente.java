package com.rednorte.PortalPacientes.model;

import java.util.List;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rutPaciente;
    private LocalDateTime fechaConsulta;
    private String ipOrigen;

    public void registrarConsulta(String rut, String ipOrigen) {
        this.rutPaciente = rut;
        this.ipOrigen = ipOrigen;
        this.fechaConsulta = LocalDateTime.now();
    }

    public List<SolicitudResumen> obtenerPorRut(String rut) {
        throw new UnsupportedOperationException(
                "obtenerPorRut requiere una capa de persistencia o un repositorio para consultar la base de datos");
    }

}
