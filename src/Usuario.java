import java.sql.*;
import java.util.Scanner;

// Classe base abstrata Usuario
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

// Classe Aluno que herda de Usuario
class Aluno extends Usuario {
    public Aluno(String nome, String email, String cpf) {
        super(nome, email, cpf);
    }

    @Override
    public void acessarSistema(Scanner sc) throws SQLException {
        try (Connection conn = Database.getConnection()) {
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
        try (Connection conn = Database.getConnection()) {
            System.out.println("Digite o nome do aluno:");
            String nome = sc.nextLine();

            System.out.println("Digite o email do aluno:");
            String email = sc.nextLine();

            System.out.println("Digite o CPF do aluno:");
            String cpf = sc.nextLine();

            System.out.println("Digite o ID da turma:");
            int turma_id = sc.nextInt();
            sc.nextLine();

            String sql = "INSERT INTO alunos (nome, turma_id, cpf) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, turma_id);
            stmt.setString(3, cpf);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Aluno registrado com sucesso!");
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
        try (Connection conn = Database.getConnection()) {
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
        try (Connection conn = Database.getConnection()) {
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

class Professor extends Usuario {
    private String area;

    public Professor(String nome, String email, String cpf, String area) {
        super(nome, email, cpf);
        this.area = area;
    }

    @Override
    public void acessarSistema(Scanner sc) throws SQLException {
        try (Connection conn = Database.getConnection()) {
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
        try (Connection conn = Database.getConnection()) {
            System.out.println("Digite o nome do professor:");
            String nome = sc.nextLine();

            System.out.println("Digite o email do professor:");
            String email = sc.nextLine();

            System.out.println("Digite o CPF do professor:");
            String cpf = sc.nextLine();

            System.out.println("Digite a área de atuação:");
            String area = sc.nextLine();

            String sql = "INSERT INTO professores (nome, email, cpf, area) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, cpf);
            stmt.setString(4, area);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Professor registrado com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao registrar professor: " + e.getMessage());
        }
    }

    private static void menuProfessor(Scanner sc, int professorId) throws SQLException {
        while (true) {
            System.out.println("\nÁrea do Professor:");
            System.out.println("1. Consultar Horários");
            System.out.println("2. Registrar Notas no Boletim");
            System.out.println("3. Consultar Boletim de Turma");
            System.out.println("4. Voltar ao Menu Principal");
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
                    return;
                default:
                    System.out.println("Opção Inválida");
            }
        }
    }

    private static void seeHorarios(Scanner sc, int professorId) throws SQLException {
        try (Connection conn = Database.getConnection()) {
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

    private static void addNotasBoletim(Scanner sc) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            System.out.println("Digite o nome do aluno:");
            String nomeAluno = sc.nextLine();

            System.out.println("Digite a disciplina:");
            String disciplina = sc.nextLine();

            System.out.println("Digite a nota:");
            double nota = sc.nextDouble();

            System.out.println("Digite as faltas:");
            int faltas = sc.nextInt();
            sc.nextLine();

            String sql = "UPDATE boletim SET nota = ?, faltas = ? WHERE nome_aluno = ? AND disciplina = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, nota);
            stmt.setInt(2, faltas);
            stmt.setString(3, nomeAluno);
            stmt.setString(4, disciplina);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Notas atualizadas com sucesso!");
            } else {
                System.out.println("Aluno ou disciplina não encontrados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar notas: " + e.getMessage());
        }
    }

    private static void consultarBoletimTurma(Scanner sc) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            System.out.println("Digite o ID da turma:");
            int turmaId = sc.nextInt();
            sc.nextLine();

            String sql = "SELECT nome_aluno, disciplina, nota, faltas FROM boletim WHERE turma = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, turmaId);

            ResultSet rs = stmt.executeQuery();
            System.out.println("Boletim da Turma:");
            while (rs.next()) {
                System.out.printf("Aluno: %s, Disciplina: %s, Nota: %.2f, Faltas: %d\n",
                        rs.getString("nome_aluno"),
                        rs.getString("disciplina"),
                        rs.getDouble("nota"),
                        rs.getInt("faltas"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar boletim da turma: " + e.getMessage());
        }
    }
}

