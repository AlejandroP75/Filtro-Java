CREATE DATABASE db_filtro_java;
USE db_filtro_java;
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    direccion VARCHAR(255),
    celular VARCHAR(20),
    documento VARCHAR(20) NOT NULL
);
CREATE TABLE producto (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precioVenta DECIMAL(10, 2) NOT NULL,
    precioCompra DECIMAL(10, 2) NOT NULL
);
CREATE TABLE descuento (
    id_descuento INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(15),
    condicion VARCHAR(255),
    monto_porcentaje VARCHAR(10),
    productos VARCHAR(10),
    estado VARCHAR(10)
);
CREATE TABLE factura (
    numeroFactura INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    cliente_id INT,
    descuento_id INT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (descuento_id) REFERENCES descuento(id_descuento),
    INDEX idx_factura_cliente (cliente_id)
);
CREATE TABLE item_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_numeroFactura INT,
    producto_codigo INT,
    cantidad INT NOT NULL,
    importe DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (factura_numeroFactura) REFERENCES factura(numeroFactura),
    FOREIGN KEY (producto_codigo) REFERENCES producto(codigo),
    INDEX idx_item_factura_factura (factura_numeroFactura),
    INDEX idx_item_factura_producto (producto_codigo)
);
CREATE TABLE impuestos (
    id_cliente INT,
    factura_numeroFactura INT,
    valor_impuesto_factura DOUBLE,
    FOREIGN KEY (factura_numeroFactura) REFERENCES factura(numeroFactura),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);


