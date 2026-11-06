package com.universidad.refactoringu11.reportes;

import org.springframework.stereotype.Service;

import com.universidad.refactoringu11.inventario.InventarioService;

@Service
public class ReporteBajoStockService {
    private final InventarioService inventarioService;

    public ReporteBajoStockService(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    public String generarReporte() {
        return "Reporte de bajo stock pendiente de implementar";
    }
}