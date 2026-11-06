package com.universidad.refactoringu11.reportes;

import org.springframework.stereotype.Service;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.NotificadorBajoStock;
import com.universidad.refactoringu11.inventario.Producto;

@Service
public class ReporteBajoStockService implements NotificadorBajoStock {
    private final InventarioService inventarioService;

    public ReporteBajoStockService(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Override
    public void notificar(Producto producto, int cantidadReorden) {
        System.out.println("ALERTA EN REPORTES: " + producto.getNombre() 
                + " requiere reorden de " + cantidadReorden + " unidades.");
    }

    public String generarReporte() {
        return "Reporte de bajo stock procesado";
    }
}