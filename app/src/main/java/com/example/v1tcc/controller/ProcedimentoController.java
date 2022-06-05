package com.example.v1tcc.controller;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.DAO.ProcedimentoDAO;
import com.example.v1tcc.models.Procedimento;

public class ProcedimentoController {

    private final ProcedimentoDAO procedimentoDAO;

    public ProcedimentoController(SQLiteConnection sqlConnection){
        procedimentoDAO = new ProcedimentoDAO(sqlConnection);
    }

    public long createProcedimentoController(Procedimento procedimento){
        return this.procedimentoDAO.createProcedimento(procedimento);
    }
}
