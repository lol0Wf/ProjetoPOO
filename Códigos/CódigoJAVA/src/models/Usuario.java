package models;

import java.sql.*;
import java.util.Scanner;

abstract class Usuario {
    protected String nome;
    protected String email;
    protected String cpf;

    public Usuario(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }
    public abstract void acessarSistema(Scanner sc) throws SQLException;

    public abstract void registrarSistema(Scanner sc) throws SQLException;
}
