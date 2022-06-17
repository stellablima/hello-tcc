package com.example.v1tcc.controller;

import com.example.v1tcc.BDHelper.SQLiteConnection;
import com.example.v1tcc.DAO.EstadoDAO;
import com.example.v1tcc.DAO.RelatorioDAO;
import com.example.v1tcc.models.Estado;
import com.example.v1tcc.models.Relatorio;

public class EstadoController {
    private final EstadoDAO estadoDAO;

    public EstadoController(SQLiteConnection sqlConnection){
        estadoDAO = new EstadoDAO(sqlConnection);
    }

    public long createEstadoController(Estado estado){
        return this.estadoDAO.createEstado(estado);
    }
}
