-- Datos de prueba Epic 4. Levantá la app una vez antes de ejecutar esto.

USE Grupoestelar;

INSERT INTO persona (nombre, apellido, eliminado) VALUES ('Juan', 'Pérez', 0);
INSERT INTO persona (nombre, apellido, eliminado) VALUES ('María', 'González', 0);
INSERT INTO persona (nombre, apellido, eliminado) VALUES ('Carlos', 'López', 0);

INSERT INTO propiedad (direccion, ciudad, estado, eliminado) VALUES ('Los Pinos', 'Oro Verde', 'DISPONIBLE', 0);
INSERT INTO propiedad (direccion, ciudad, estado, eliminado) VALUES ('Las Calandrias', 'Paraná', 'ALQUILADA', 0);
INSERT INTO propiedad (direccion, ciudad, estado, eliminado) VALUES ('Los Cardenales', 'Santa Fe', 'DISPONIBLE', 0);

INSERT INTO contrato (propiedad_id, inquilino_id, fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento, descripcion, estado, eliminado)
VALUES (2, 1, '2025-01-01', 12, 150000.00, 10, 'Alquiler departamento', 'ACTIVO', 0);

INSERT INTO contrato (propiedad_id, inquilino_id, fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento, descripcion, estado, eliminado)
VALUES (1, 2, '2025-03-01', 6, 120000.00, 5, 'Contrato en borrador', 'BORRADOR', 0);

INSERT INTO contrato (propiedad_id, inquilino_id, fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento, descripcion, estado, eliminado)
VALUES (3, 3, '2024-01-01', 12, 100000.00, 15, 'Contrato finalizado', 'FINALIZADO', 0);

INSERT INTO factura (fecha_emision, fecha_vencimiento, importe, estado, eliminada, concepto_facturado, contrato_id)
VALUES ('2025-06-01', '2025-06-15', 25000.00, 'PENDIENTE', 0, 'Cargo por expensas extraordinarias', 1);

INSERT INTO factura (fecha_emision, fecha_vencimiento, importe, estado, eliminada, fecha_pago, medio, importe_pagado, interes, concepto_facturado, contrato_id)
VALUES ('2025-05-01', '2025-05-15', 18000.00, 'PAGADA', 0, '2025-05-10', 'TRANSFERENCIA', 18000.00, 0.00, 'Reparación plomería', 1);

INSERT INTO factura (fecha_emision, fecha_vencimiento, importe, estado, eliminada, concepto_facturado, contrato_id)
VALUES ('2025-04-01', '2025-04-15', 12000.00, 'VENCIDA', 0, 'Multa por mora', 1);

INSERT INTO historial_estado_factura (estado, fecha_hora, factura_id)
VALUES ('PENDIENTE', '2025-06-01 10:00:00', 1);

INSERT INTO historial_estado_factura (estado, fecha_hora, factura_id)
VALUES ('PENDIENTE', '2025-05-01 10:00:00', 2);

INSERT INTO historial_estado_factura (estado, fecha_hora, factura_id)
VALUES ('PAGADA', '2025-05-10 14:30:00', 2);

INSERT INTO historial_estado_factura (estado, fecha_hora, factura_id)
VALUES ('PENDIENTE', '2025-04-01 10:00:00', 3);

INSERT INTO historial_estado_factura (estado, fecha_hora, factura_id)
VALUES ('VENCIDA', '2025-04-16 09:00:00', 3);
