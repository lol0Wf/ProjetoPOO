import java.sql.*;
import java.util.Scanner;

public class Menu {
    public static void menu(Scanner sc) throws SQLException {
        while (true) {
            System.out.println("\nPara acesso:");
            System.out.println("1. Acesso Professor");
            System.out.println("2. Acesso Aluno");
            System.out.println("\nPara Registrar:");
            System.out.println("3. Registrar Professor");
            System.out.println("4. Registrar Aluno");
            System.out.println("\n-------------------");
            System.out.println("5. Sair");
            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: {
                    Professor professor = new Professor("", "", "", "");
                    professor.acessarSistema(sc);
                    break;
                }
                case 2: {
                    Aluno aluno = new Aluno("", "", "");
                    aluno.acessarSistema(sc);
                    break;
                }
                case 3: {
                    Professor professor = new Professor("", "", "", "");
                    professor.registrarSistema(sc);
                    break;
                }
                case 4: {
                    Aluno aluno = new Aluno("", "", "");
                    aluno.registrarSistema(sc);
                    break;
                }
                case 5: {
                    System.out.println("\nEncerrando Sistema:");
                    return;
                }
                default:
                    System.out.println("Opção Inválida");
            }
        }
    }
}

