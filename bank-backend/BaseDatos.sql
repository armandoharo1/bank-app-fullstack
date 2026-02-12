BEGIN;

-- Limpieza
DROP TABLE IF EXISTS movimiento;
DROP TABLE IF EXISTS cuenta;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS persona;

-- 1. PERSONA
-- Mapea a: Persona.java  (Inheritance JOINED)
CREATE TABLE persona (
  persona_id      BIGSERIAL PRIMARY KEY,
  nombre          VARCHAR(120) NOT NULL,
  genero          VARCHAR(20)  NOT NULL,
  edad            INT         NOT NULL CHECK (edad >= 0),
  identificacion  VARCHAR(30)  NOT NULL UNIQUE,
  direccion       VARCHAR(200) NOT NULL,
  telefono        VARCHAR(30)  NOT NULL
);

-- 2. CLIENTE (JOINED: PK=FK hacia persona)
-- Mapea a: Cliente.java (@PrimaryKeyJoinColumn(name="cliente_id"))
CREATE TABLE cliente (
  cliente_id     BIGINT PRIMARY KEY,
  client_key     VARCHAR(40)  NOT NULL UNIQUE,
  password_hash  VARCHAR(200) NOT NULL,
  estado         BOOLEAN      NOT NULL,
  CONSTRAINT fk_cliente_persona
    FOREIGN KEY (cliente_id) REFERENCES persona(persona_id)
    ON DELETE CASCADE
);

-- 3. CUENTA
-- Mapea a: Cuenta.java PK numero_cuenta + FK cuenta.cliente_id -> cliente.cliente_id
CREATE TABLE cuenta (
  numero_cuenta    VARCHAR(30)   PRIMARY KEY,
  tipo_cuenta      VARCHAR(30)   NOT NULL,
  saldo_inicial    NUMERIC(14,2) NOT NULL CHECK (saldo_inicial >= 0),
  saldo_disponible NUMERIC(14,2) NOT NULL CHECK (saldo_disponible >= 0),
  estado           BOOLEAN       NOT NULL,
  cliente_id       BIGINT        NOT NULL,
  CONSTRAINT fk_cuenta_cliente
    FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id)
    ON DELETE RESTRICT
);

CREATE INDEX idx_cuenta_cliente_id ON cuenta(cliente_id);

-- 4. MOVIMIENTO
-- Mapea a: Movimiento.java IDENTITY + FK movimiento.numero_cuenta -> cuenta.numero_cuenta
CREATE TABLE movimiento (
  movimiento_id   BIGSERIAL PRIMARY KEY,
  fecha           DATE          NOT NULL,
  tipo_movimiento VARCHAR(10)   NOT NULL,   -- CREDITO / DEBITO
  valor           NUMERIC(14,2) NOT NULL,
  saldo           NUMERIC(14,2) NOT NULL,
  numero_cuenta   VARCHAR(30)   NOT NULL,
  CONSTRAINT fk_movimiento_cuenta
    FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta)
    ON DELETE CASCADE
);

-- Índice útil para reportes y recálculo cronológico
CREATE INDEX idx_mov_cuenta_fecha_id ON movimiento(numero_cuenta, fecha, movimiento_id);

COMMIT;