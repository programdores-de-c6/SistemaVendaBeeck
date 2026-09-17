package com.ideias_inovadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.dto.DashboardDTO;
import com.ideias_inovadora.service.DashboardService;

/**
 * ====================================================
 * DASHBOARD CONTROLLER
 * ====================================================
 *
 * A autorização/contexto é resolvida no backend:
 *
 * ADMIN:
 *   sem X-Store-ID -> GLOBAL
 *   com X-Store-ID -> loja seleccionada
 *
 * GERENTE:
 *   sempre a loja associada à conta
 *
 * UTILIZADOR:
 *   sempre a loja associada à conta
 */
@RestController
@RequestMapping("api/sales-system/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public DashboardDTO dashboard(

            @RequestHeader(
                    value = "X-Store-ID",
                    required = false
            )
            Long storeHeader

    ) {

        return dashboardService.dashboard(
                storeHeader
        );
    }
}
