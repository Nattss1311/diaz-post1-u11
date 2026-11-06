package com.universidad.refactoringu11.inventario;

public enum TipoProducto {
    LIBRO {
        @Override
        public double calcularRecargo(double precio) {
            return 0.0;
        }

        @Override
        public String generarDescripcionEtiqueta(String nombre) {
            return "[LIBRO] " + nombre + " - Exento de IVA";
        }
    },
    ELECTRONICO {
        @Override
        public double calcularRecargo(double precio) {
            return precio * 0.08;
        }

        @Override
        public String generarDescripcionEtiqueta(String nombre) {
            return "[ELECTRO] " + nombre + " - Garantia 1 ano";
        }
    },
    ROPA {
        @Override
        public double calcularRecargo(double precio) {
            return precio * 0.03;
        }

        @Override
        public String generarDescripcionEtiqueta(String nombre) {
            return "[ROPA] " + nombre + " - Ver talla";
        }
    },
    ALIMENTO {
        @Override
        public double calcularRecargo(double precio) {
            return 2.50;
        }

        @Override
        public String generarDescripcionEtiqueta(String nombre) {
            return "[ALIMENTO] " + nombre + " - Perecedero";
        }
    };

    public abstract double calcularRecargo(double precio);
    public abstract String generarDescripcionEtiqueta(String nombre);
}