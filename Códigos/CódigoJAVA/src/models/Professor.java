package models;

import java.sql.*;
import java.util.Scanner;

public class Professor extends Usuario {
    public String area;

    public Professor(String nome, String email, String cpf, String area) {
        super(nome, email, cpf);
        this.area = area;
    }
    @Override
    public void acessarSistema(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o CPF do professor:");
            String cpf = sc.nextLine();

            String sql = "SELECT * FROM professores WHERE cpf = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Bem-vindo, " + rs.getString("nome") + "!");
                menuProfessor(sc, rs.getInt("id"));
            } else {
                System.out.println("Professor não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao acessar professor: " + e.getMessage());
        }
    }

    @Override
    public void registrarSistema(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o nome do professor:");
            String nome = sc.nextLine();

            System.out.println("Digite o email do professor:");
            String email = sc.nextLine();

            System.out.println("Digite o CPF do professor:");
            String cpf = sc.nextLine();

            System.out.println("Digite a área de atuação:");
            String area = sc.nextLine();

            String sql = "INSERT INTO professores (nome, email, cpf, area) VALUES (?, ?, ?, ?)";
            String sqlDisciplina  = "INSERT INTO disciplina (descricao) VALUES (?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, cpf);
            stmt.setString(4, area);

            PreparedStatement stmtDisciplina = conn.prepareStatement(sqlDisciplina);
            stmtDisciplina.setString(1, area);

            int rowsInserted = stmt.executeUpdate();
            int rowsInsertedDisc = stmtDisciplina.executeUpdate();

            if (rowsInserted > 0 & rowsInsertedDisc > 0) {
                System.out.println("Professor registrado com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao registrar professor: " + e.getMessage());
        }
    }

    public static void menuProfessor(Scanner sc, int professorId) throws SQLException {
        while (true) {
            System.out.println("\nÁrea do Professor:");
            System.out.println("1. Consultar Horários");
            System.out.println("2. Registrar Notas no Boletim");
            System.out.println("3. Consultar Boletim de Turma");
            System.out.println("4. Criar turma");
            System.out.println("5. Criar Horário");
            System.out.println("6. Excluir Horário");
            System.out.println("7. Resetar Letivo");
            System.out.println("8. Voltar ao Menu Principal");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    seeHorarios(sc, professorId);
                    break;
                case 2:
                    addNotasBoletim(sc);
                    break;
                case 3:
                    consultarBoletimTurma(sc);
                    break;
                case 4:
                    createTurma(sc);
                    break;
                case 5:
                    createHour(sc, professorId);
                    break;
                case 6:
                    deleteHour(sc);
                    break;
                case 7:
                    deleteLetivo(sc);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Opção Inválida");
            }
        }
    }

    public static void seeHorarios(Scanner sc, int professorId) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            String sql = "SELECT horario FROM horarios WHERE professor_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, professorId);

            ResultSet rs = stmt.executeQuery();
            System.out.println("Horários:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("horario"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar horários: " + e.getMessage());
        }
    }

    public static void addNotasBoletim(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o nome do aluno:");
            String alunoNome = sc.nextLine();

            System.out.println("Digite o ID da disciplina:");
            int disciplinaId = sc.nextInt();

            System.out.println("Digite a nota:");
            double nota = sc.nextDouble();

            System.out.println("Digite o semestre:");
            String semestre = sc.nextLine();

            System.out.println("Digite o ano:");
            int ano = sc.nextInt();

            System.out.println("Digite o curso:");
            String curso = sc.nextLine();

            System.out.println("Digite o ID da turma:");
            int turmaId = sc.nextInt();

            System.out.println("Digite as faltas:");
            int faltas = sc.nextInt();
            sc.nextLine();

            try {
                // Inserir dados no boletim
                String sqlBoletim = "INSERT INTO boletim (nome_aluno, disciplina, nota, semestre, ano, curso, turma, faltas) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmtBoletim = conn.prepareStatement(sqlBoletim);
                stmtBoletim.setString(1, alunoNome);
                stmtBoletim.setInt(2, disciplinaId);
                stmtBoletim.setDouble(3, nota);
                stmtBoletim.setString(4, semestre);
                stmtBoletim.setInt(5, ano);
                stmtBoletim.setString(6, curso);
                stmtBoletim.setInt(7, turmaId);
                stmtBoletim.setInt(8, faltas);

                int rowsInserted = stmtBoletim.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Notas inseridas com sucesso!");
                } else {
                    System.out.println("Erro ao inserir as notas.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao inserir dados no boletim: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public static void consultarBoletimTurma(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o ID da turma:");
            int turmaId = sc.nextInt();
            sc.nextLine();

            String sql = "SELECT nome_aluno, disciplina, nota, faltas FROM boletim WHERE turma = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, turmaId);
            ResultSet rs = stmt.executeQuery();

            String sqlDisciplina = "SELECT descricao FROM disciplina WHERE id = ?";
            PreparedStatement stmtDisciplina = conn.prepareStatement(sqlDisciplina);

            System.out.println("Boletim da Turma:");
            while (rs.next()) {
                String nomeAluno = rs.getString("nome_aluno");
                int disciplinaId = rs.getInt("disciplina");
                double nota = rs.getDouble("nota");
                int faltas = rs.getInt("faltas");

                stmtDisciplina.setInt(1, disciplinaId);
                ResultSet ps = stmtDisciplina.executeQuery();

                String nomeDisciplina = "Desconhecida";
                if (ps.next()) {
                    nomeDisciplina = ps.getString("descricao");
                }
                ps.close();

                System.out.printf("Aluno: %s, Disciplina: %s, Nota: %.2f, Faltas: %d\n",
                        nomeAluno, nomeDisciplina, nota, faltas);
            }
            rs.close();
            stmt.close();
            stmtDisciplina.close();
        }
    }
    public static void deleteLetivo(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Tem certeza que deseja finalizar o ano letivo? Essa ação apagará todas as notas, turmas e horários! (Sim/Não)");
            String confirmacao = sc.nextLine().trim().toLowerCase();

            if (!confirmacao.equals("sim")) {
                System.out.println("Ação cancelada.");
                return;
            }

            String deleteBoletim = "DELETE FROM boletim";
            String deleteHorarios = "DELETE FROM horarios";
            String deleteMatriculas = "DELETE FROM matricula";
            String deleteAlunos = "DELETE FROM alunos";
            String deleteFaltas = "DELETE FROM faltas";
            String deleteTurmas = "DELETE FROM turma";

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteFaltas);
                stmt.executeUpdate(deleteBoletim);
                stmt.executeUpdate(deleteHorarios);
                stmt.executeUpdate(deleteMatriculas);
                stmt.executeUpdate(deleteAlunos);
                stmt.executeUpdate(deleteTurmas);
                System.out.println("Ano letivo finalizado com sucesso! Todos os dados foram apagados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao finalizar o ano letivo: " + e.getMessage());
        }
    }

    public static void createTurma(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o curso da turma:");
            String curso = sc.nextLine();

            System.out.println("Digite o ano da turma:");
            String ano = sc.nextLine();

            System.out.println("Digite o semestre da turma:");
            String semestre = sc.nextLine();

            String sql = "INSERT INTO turma (curso, ano, semestre) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, curso);
            stmt.setString(2, ano);
            stmt.setString(3, semestre);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Turma criada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao criar turma: " + e.getMessage());
        }
    }

    public static void createHour(Scanner sc, int professorId) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o horário:");
            String horario = sc.nextLine();

            String sql = "INSERT INTO horarios (professor_id, horario) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, professorId);
            stmt.setString(2, horario);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Horário criado com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao criar horário: " + e.getMessage());
        }
    }

    public static void deleteHour(Scanner sc) throws SQLException {
        try (Connection conn = database.Database.getConnection()) {
            System.out.println("Digite o ID do horário a ser excluído:");
            int horarioId = sc.nextInt();
            sc.nextLine();

            String sql = "DELETE FROM horarios WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, horarioId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Horário excluído com sucesso!");
            } else {
                System.out.println("Horário não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir horário: " + e.getMessage());
        }
    }
}
