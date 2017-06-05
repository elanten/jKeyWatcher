SET SCHEMA PUBLIC;

CREATE TABLE IF NOT EXISTS digital_key_type(
  id  BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR2(100)
);

CREATE TABLE IF NOT EXISTS digital_key (
  id          BIGINT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR2(255) NOT NULL,
  serial      VARCHAR2(255) NOT NULL,
  description TEXT,
  expire      DATE,
  type_id BIGINT NOT NULL,
  removed BOOLEAN DEFAULT FALSE,
);

CREATE TABLE IF NOT EXISTS contragent_type (
  id          BIGINT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR2(255) NOT NULL,
  description TEXT
);

CREATE TABLE IF NOT EXISTS contragent
(
  id          BIGINT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR2(100) NOT NULL,
  full_name   VARCHAR2(255),
  type_id        BIGINT,
  description TEXT,
  removed BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (type_id) REFERENCES contragent_type (id)
);

CREATE TABLE IF NOT EXISTS contragent_link (
  contragent_id BIGINT NOT NULL,
  link       BIGINT NOT NULL,
  FOREIGN KEY (contragent_id) REFERENCES contragent (id) ON DELETE CASCADE,
  FOREIGN KEY (link) REFERENCES contragent (id) ON DELETE CASCADE,
  PRIMARY KEY (contragent_id, link),
);

CREATE TABLE IF NOT EXISTS contragent_detail_type (
  id   INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR2(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS contragent_detail (
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  contragent_id BIGINT NOT NULL,
  type_id       BIGINT NOT NULL,
  value      VARCHAR2(255),
  FOREIGN KEY (contragent_id) REFERENCES contragent (id) ON DELETE CASCADE,
  FOREIGN KEY (type_id) REFERENCES contragent_detail_type (id)
);

CREATE TABLE IF NOT EXISTS digital_key_contacts (
  digital_key_id   BIGINT NOT NULL,
  contragent_id BIGINT NOT NULL,
  type       VARCHAR2(20),
  FOREIGN KEY (digital_key_id) REFERENCES digital_key (id) ON DELETE CASCADE,
  FOREIGN KEY (contragent_id) REFERENCES contragent (id) ON DELETE CASCADE,
  PRIMARY KEY (digital_key_id, contragent_id, type)
);