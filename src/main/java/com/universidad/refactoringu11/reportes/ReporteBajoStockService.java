package com.universidad.refactoringu11.reportes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.NotificadorBajoStock;
import com.universidad.refactoringu11.inventario.Producto;

@Service
public class ReporteBajoStockService implements NotificadorBajoStock {
    private final InventarioService inventarioService;
    private final List<String> alertasRecibidas = new ArrayList<>();

    // @Lazy evita el ciclo entre beans de Spring: InventarioService recibe este bean por setter
    // y este bean recibe InventarioService por constructor.
    public ReporteBajoStockService(@Lazy InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Override
    public void notificar(Producto producto, int cantidadReorden) {
        alertasRecibidas.add(producto.getNombre() + " -> reordenar " + cantidadReorden + " unidades");
    }

    public String generarReporte() {
        String productosBajoStock = inventarioService.obtenerProductosBajoStock().stream()
                .map(p -> "- " + p.getNombre() + " (stock " + p.getStock() + " / minimo " + p.getStockMinimo() + ")")
                .collect(Collectors.joining("\n"));
        String alertas = alertasRecibidas.stream()
                .map(alerta -> "- " + alerta)
                .collect(Collectors.joining("\n"));
        return "REPORTE DE BAJO STOCK\n"
                + "Productos bajo el stock minimo:\n" + productosBajoStock + "\n"
                + "Alertas recibidas (" + alertasRecibidas.size() + "):\n" + alertas;
    }
}
