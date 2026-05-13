-- Datos de prueba para pacientes - Hibernate import.sql
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('12345678-9', NOW(), '192.168.1.100');
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('11111111-1', DATE_SUB(NOW(), INTERVAL 5 DAY), '192.168.1.101');
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('22222222-2', DATE_SUB(NOW(), INTERVAL 10 DAY), '192.168.1.102');
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('33333333-3', DATE_SUB(NOW(), INTERVAL 3 DAY), '192.168.1.103');
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('44444444-4', DATE_SUB(NOW(), INTERVAL 7 DAY), '192.168.1.104');
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('55555555-5', DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.105');
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('12345678-9', DATE_SUB(NOW(), INTERVAL 15 DAY), '192.168.1.100');
INSERT INTO consulta_paciente (rut_paciente, fecha_consulta, ip_origen) VALUES ('11111111-1', DATE_SUB(NOW(), INTERVAL 20 DAY), '192.168.1.101');
