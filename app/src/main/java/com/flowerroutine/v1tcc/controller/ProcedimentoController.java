package com.flowerroutine.v1tcc.controller;

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.DAO.ProcedimentoDAO;
import com.flowerroutine.v1tcc.models.Procedimento;

import java.util.List;

public class ProcedimentoController {

    private final ProcedimentoDAO procedimentoDAO;

    public ProcedimentoController(SQLiteConnection sqlConnection){
        procedimentoDAO = new ProcedimentoDAO(sqlConnection);
    }

    public long createProcedimentoController(Procedimento procedimento){
        return this.procedimentoDAO.createProcedimento(procedimento);
    }

    public List<Procedimento> listProcedimentoController(){
        return  this.procedimentoDAO.listProcedimento();
    }
}
