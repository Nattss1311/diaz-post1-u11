package com.universidad.refactoringu11.inventario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {
    private final List<Producto> productos = new ArrayList<>();
    private final List<String> movimientos = new ArrayList<>();
    private final CalculadoraReorden calculadoraReorden = new CalculadoraReorden();
    private NotificadorBajoStock notificador;

    @Autowired(required = false)
    public void setNotificador(NotificadorBajoStock notificador) {
        this.notificador = notificador;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public String actualizarInventario(Long productoId, int cantidad, String tipoMovimiento) {
        if (!esMovimientoValido(productoId, cantidad, tipoMovimiento)) return "ERROR_DATOS_INVALIDOS";
        Producto producto = buscarProducto(productoId);
        if (producto == null) return "ERROR_PRODUCTO_NO_EXISTE";

        int nuevoStock = calcularNuevoStock(producto.getStock(), cantidad, tipoMovimiento);
        if (nuevoStock < 0) return "ERROR_STOCK_INSUFICIENTE";

        producto.setStock(nuevoStock);
        registrarAuditoria(tipoMovimiento, cantidad, producto.getNombre(), nuevoStock);
        return procesarNotificacionReorden(producto, nuevoStock);
    }

    private boolean esMovimientoValido(Long productoId, int cantidad, String tipoMovimiento) {
        return productoId != null && cantidad > 0 
                && ("ENTRADA".equals(tipoMovimiento) || "SALIDA".equals(tipoMovimiento));
    }

    private Producto buscarProducto(Long productoId) {
        return productos.stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .orElse(null);
    }

    private int calcularNuevoStock(int stockActual, int cantidad, String tipoMovimiento) {
        return "ENTRADA".equals(tipoMovimiento) ? stockActual + cantidad : stockActual - cantidad;
    }

    private void registrarAuditoria(String tipoMovimiento, int cantidad, String nombreProducto, int stockFinal) {
        String registro = tipoMovimiento + " de " + cantidad + " unidades de " 
                + nombreProducto + " -> stock final: " + stockFinal;
        movimientos.add(registro);
        System.out.println(registro);
    }

    private String procesarNotificacionReorden(Producto producto, int nuevoStock) {
        if (calculadoraReorden.necesitaReorden(nuevoStock, producto.getStockMinimo())) {
            int cantidadReorden = calculadoraReorden.calcularCantidad(nuevoStock, producto.getStockMinimo());
            if (notificador != null) {
                notificador.notificar(producto, cantidadReorden);
            }
            System.out.println("ALERTA: " + producto.getNombre()
                    + " bajo stock minimo. Reordenar " + cantidadReorden + " unidades.");
            return "OK_BAJO_STOCK_REORDEN_" + cantidadReorden;
        }
        return "OK_" + nuevoStock;
    }
}