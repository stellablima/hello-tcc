package com.flowerroutine.v1tcc.controller;

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.DAO.RelatorioDAO;
import com.flowerroutine.v1tcc.models.Relatorio;

public class RelatorioController {

    private final RelatorioDAO relatorioDAO;

    public RelatorioController(SQLiteConnection sqlConnection){
        relatorioDAO = new RelatorioDAO(sqlConnection);
    }

    public long createRelatorioController(Relatorio relatorio){
        return this.relatorioDAO.createRelatorio(relatorio);
    }
}
