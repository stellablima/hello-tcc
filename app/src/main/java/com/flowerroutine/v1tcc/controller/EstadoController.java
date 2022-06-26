package com.flowerroutine.v1tcc.controller;

import com.flowerroutine.v1tcc.BDHelper.SQLiteConnection;
import com.flowerroutine.v1tcc.DAO.EstadoDAO;
import com.flowerroutine.v1tcc.models.Estado;

public class EstadoController {
    private final EstadoDAO estadoDAO;

    public EstadoController(SQLiteConnection sqlConnection){
        estadoDAO = new EstadoDAO(sqlConnection);
    }

    public long createEstadoController(Estado estado){
        return this.estadoDAO.createEstado(estado);
    }
}
