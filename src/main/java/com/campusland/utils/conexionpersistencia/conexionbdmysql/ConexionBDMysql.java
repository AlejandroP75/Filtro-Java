package com.campusland.utils.conexionpersistencia.conexionbdmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.campusland.utils.Configuracion;

public class ConexionBDMysql {

    private static String url = Configuracion.obtenerValor("db.url");
    private static String username = Configuracion.obtenerValor("db.username");
    private static String password = Configuracion.obtenerValor("db.password");
    private static Connection connection;

    public static Connection getInstance() throws SQLException {

        return connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_filtro_java?serverTimezone=UTC", "root", "campus2024");

    }

}