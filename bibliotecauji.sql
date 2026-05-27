CREATE TABLE autores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    biografia TEXT,
    pais VARCHAR(100)
);

CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(500)
);

CREATE TABLE libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    google_books_id VARCHAR(255) UNIQUE,
    titulo VARCHAR(255) NOT NULL,
    subtitulo VARCHAR(255),
    descripcion TEXT,
    editorial VARCHAR(255),
    fecha_publicacion VARCHAR(20),
    numero_paginas INT,
    isbn_10 VARCHAR(20),
    isbn_13 VARCHAR(20),
    imagen_portada VARCHAR(512),
    valoracion_media DOUBLE,
    numero_valoraciones INT,
    idioma VARCHAR(10)
);

CREATE TABLE libro_autor (
    libro_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    PRIMARY KEY (libro_id, autor_id),
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE,
    FOREIGN KEY (autor_id) REFERENCES autores(id) ON DELETE CASCADE
);

CREATE TABLE libro_categoria (
    libro_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (libro_id, categoria_id),
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE
);

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- RELACIÓN: usuarios - libros (colecciones personales)
-- Un usuario puede tener muchos libros en su colección
-- ============================================

CREATE TABLE colecciones (
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_lectura ENUM('PENDIENTE', 'LEYENDO', 'LEIDO') DEFAULT 'PENDIENTE',
    comentario_personal VARCHAR(500),
    PRIMARY KEY (usuario_id, libro_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE
);

-- ============================================
-- RELACIÓN: usuarios - libros (lista de deseados)
-- ============================================

CREATE TABLE deseados (
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, libro_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE
);

-- ============================================
-- RELACIÓN: usuarios - libros (reseñas)
-- ============================================

CREATE TABLE reseñas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    puntuacion INT CHECK (puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE
);

-- ============================================
-- RELACIÓN: usuarios - usuarios (seguimientos)
-- Un usuario puede seguir a otros usuarios
-- ============================================

CREATE TABLE seguimientos_usuarios (
    seguidor_id BIGINT NOT NULL,
    seguido_id BIGINT NOT NULL,
    PRIMARY KEY (seguidor_id, seguido_id),
    FOREIGN KEY (seguidor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (seguido_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- ============================================
-- RELACIÓN: usuarios - autores (seguimientos de autores)
-- ============================================

CREATE TABLE seguimientos_autores (
    usuario_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, autor_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (autor_id) REFERENCES autores(id) ON DELETE CASCADE
);