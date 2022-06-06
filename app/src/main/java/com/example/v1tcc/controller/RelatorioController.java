package com.example.v1tcc.controller;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.DAO.ProcedimentoDAO;
import com.example.v1tcc.DAO.RelatorioDAO;
import com.example.v1tcc.models.Procedimento;
import com.example.v1tcc.models.Relatorio;

public class RelatorioController {

    private final RelatorioDAO relatorioDAO;

    public RelatorioController(SQLiteConnection sqlConnection){
        relatorioDAO = new RelatorioDAO(sqlConnection);
    }

    public long createRelatorioController(Relatorio relatorio){
        return this.relatorioDAO.createRelatorio(relatorio);
    }
}
