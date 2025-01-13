package models;

import java.sql.*;
import java.util.Scanner;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Aluno extends Usuario {
    public Aluno(String nome, String email, String cpf) {
        super(nome, email, cpf);
    }

    @Override
    public void acessarSistema(Scanner sc) throws SQLException {

        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o ID do aluno:");
            String aluno_id = sc.nextLine();

            String sql = "SELECT * FROM alunos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Bem-vindo, " + rs.getString("nome") + "!");
                menuAluno(sc, aluno_id);
            } else {
                System.out.println("Aluno não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao acessar aluno: " + e.getMessage());
        }
    }

    @Override
    public void registrarSistema(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o nome do aluno:");
            String nome = sc.nextLine();

            System.out.println("Digite o email do aluno:");
            String email = sc.nextLine();

            System.out.println("Digite o CPF do aluno:");
            String cpf = sc.nextLine();

            System.out.println("Digite o ID da turma:");
            int turma_id = sc.nextInt();
            sc.nextLine();
            Timestamp data = Timestamp.valueOf(LocalDateTime.now());

            String sql = "INSERT INTO alunos (nome, turma_id, cpf) VALUES (?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, turma_id);
            stmt.setString(3, cpf);

            int validationAluno = stmt.executeUpdate();
            if (validationAluno > 0) {
                String status_matricula = "Ativo";

                String Matricula = "INSERT INTO matricula (id_turma, data, status_matricula) VALUES (?,?,?)";
                PreparedStatement stmtMatricula = conn.prepareStatement(Matricula);
                stmtMatricula.setInt(1, turma_id);
                stmtMatricula.setString(2, String.valueOf(data));
                stmtMatricula.setString(3, status_matricula);

                int validationMatricula = stmtMatricula.executeUpdate();

                if (validationMatricula > 0) {
                    System.out.println("Aluno e matricula registrados com sucesso!");
                } else {
                    System.out.println("Erro ao registrar matricula.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao registrar aluno: " + e.getMessage());
        }
    }

    private static void menuAluno(Scanner sc, String aluno_id) throws SQLException {
        while (true) {
            System.out.println("\nÁrea do Aluno:");
            System.out.println("1. Ver Boletim");
            System.out.println("2. Ver Turma");
            System.out.println("3. Voltar ao Menu Principal");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    seeBoletim(sc, aluno_id);
                    break;
                case 2:
                    seeTurma(sc, aluno_id);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opção Inválida");
            }
        }
    }

    private static void seeBoletim(Scanner sc, String aluno_id) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            String sql = "SELECT * FROM boletim WHERE nome_aluno = (SELECT nome FROM alunos WHERE id = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno_id);

            ResultSet rs = stmt.executeQuery();
            System.out.println("Boletim:");
            while (rs.next()) {
                System.out.printf("Disciplina: %s, Nota: %.2f, Faltas: %d\n",
                        rs.getString("disciplina"),
                        rs.getDouble("nota"),
                        rs.getInt("faltas"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar boletim: " + e.getMessage());
        }
    }


    private static void seeTurma(Scanner sc, String aluno_id) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            String sql = "SELECT turma.curso, turma.ano, turma.semestre FROM turma JOIN alunos ON turma.id = alunos.turma_id WHERE alunos.id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("Curso: %s, Ano: %s, Semestre: %s\n",
                        rs.getString("curso"),
                        rs.getString("ano"),
                        rs.getString("semestre"));
            } else {
                System.out.println("Turma não encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar turma: " + e.getMessage());
        }
    }
}
