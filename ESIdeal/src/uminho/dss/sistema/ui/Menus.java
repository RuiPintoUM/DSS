package uminho.dss.sistema.ui;

import uminho.dss.sistema.business.SistemaFacade;

import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

public class Menus {

    private SistemaFacade sistemaFacade;

    public Menus(SistemaFacade sistemaFacade) {
        this.sistemaFacade = sistemaFacade;
    }

    public void menuPrincipal() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Menu Principal ---\n");

        System.out.println(" 1. UI Funcionario");
        System.out.println(" 2. UI Mecanico");
        System.out.println(" 0. Sair");

        System.out.println("\nSelecione uma opção: ");
        String opcao = scanner.next();

        switch (opcao) {
            case "1":
                funcionarioUI();
                break;
            case "2":
                mecanicoUI();
                break;
            case "0":
                break;
            default:
                System.out.println("Essa opção não está diponível");
                menuPrincipal();
        }
    }

    // ------------------------------------------
    // --- UI MECANICO ---
    // ------------------------------------------

    public void mecanicoUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- UI Mecanico ---\n");

        System.out.println(" 1. Registar Entrada");
        System.out.println(" 2. Registar Saída");
        System.out.println(" 3. Iniciar Serviço");
        System.out.println(" 4. Concluir Serviço");
        System.out.println(" 5. Consultar Serviços");
        System.out.println(" 0. Voltar");

        System.out.println("\nSelecione uma opção: ");
        String opcao = scanner.next();

        switch (opcao) {
            case "1":
                menuRegistarEntrada() ;
                break;
            case "2":
                menuRegistarSaida();
                break;
            case "3":
                menuIniciarServico();
                break;
            case "4":
                menuConcluirServico();
                break;
            case "5":
                menuConsultaServicos();
                break;
            case "0":
                menuPrincipal();
                break;
            default:
                System.out.println("Essa opção não está diponível");
                mecanicoUI();
        }
    }

    public void menuRegistarEntrada() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Registar Entrada ---\n");

        System.out.println("Digite o ID de mecanico: ");
        String idMecanico = scanner.nextLine();

        String output = this.sistemaFacade.registarEntrada(idMecanico);

        if (output.equals("null")) {
            System.out.println("Entrada não registada.");
            System.out.println("O ID não se encontra registado no sistema");
        } else System.out.println("Entrada registada.");

        mecanicoUI();
    }

    public void menuRegistarSaida() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Registar Saída ---\n");

        System.out.println("Digite o ID de mecanico: ");
        String idMecanico = scanner.nextLine();

        String output = this.sistemaFacade.registarSaida(idMecanico);

        if (output.equals("null")) {
            System.out.println("Saída não registada.");
            System.out.println("O ID não se encontra registado no sistema ou não se encontra em atividade");
        } else System.out.println("Saída registada.");

        mecanicoUI();
    }

    public void menuIniciarServico() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Iniciar Serviço ---\n");

        System.out.println("Digite o ID de Cliente: ");
        String idCliente = scanner.nextLine();
        System.out.println("Digite o ID do Serviço: ");
        String idServico = scanner.nextLine();
        System.out.println("Digite a matrícula o Carro: ");
        String matricula = scanner.nextLine();

        String output = this.sistemaFacade.iniciarServico(idCliente, idServico, matricula);

        if (output.equals("null")) {
            System.out.println("Serviço não iniciado.");
            System.out.println("Informações erradas introduzidas.");
        } else System.out.println("Serviço iniciado");

        mecanicoUI();
    }

    public void menuConcluirServico() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Concluir Serviço ---\n");

        System.out.println("Digite o ID de Cliente: ");
        String idCliente = scanner.nextLine();
        System.out.println("Digite o ID do Serviço: ");
        String idServico = scanner.nextLine();
        System.out.println("Digite a matrícula o Carro: ");
        String matricula = scanner.nextLine();

        String output = this.sistemaFacade.concluirServico(idCliente, idServico, matricula);

        if (output.equals("null")) {
            System.out.println("Serviço não concluído.");
            System.out.println("O Serviço não se encontra registado no sistema.");
        }
        if (output.equals("checkup")){
            System.out.println("Indique quais os servicos vai adicionar(servico1 servico2: ");
            String respostaServicos = scanner.nextLine();

            String[] arrayServicos = respostaServicos.split(" ");

            List<String> listaServicos = Arrays.asList(arrayServicos);

            if(listaServicos.isEmpty()){
                System.out.println("Nenhum serviço atribuído.");
            }
            else {
                for (String servico : listaServicos) {
                    sistemaFacade.atribuirServico(idCliente, idServico, matricula);
                }
            }
        }
        else System.out.println("Cliente Notificado que serviço foi entregue");

        mecanicoUI();
    }

    public void menuConsultaServicos() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Serviços Disponíveis ---\n");

        System.out.println("Digite o ID de mecanico: ");
        String idMecanico = scanner.nextLine();

        System.out.println("\n");

        System.out.println(this.sistemaFacade.consultaServicos(idMecanico));

        mecanicoUI();
    }

    // ------------------------------------------
    // --- UI FUNCIONARIO ---
    // ------------------------------------------

    public void funcionarioUI() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- UI Funcionário ---\n");

        System.out.println(" 1. Remover Serviço");
        System.out.println(" 2. Atribuir Serviço");
        System.out.println(" 3. Agendar Serviço");
        System.out.println(" 0. Voltar");

        System.out.println("\nSelecione uma opção: ");
        String opcao = scanner.next();

        switch (opcao) {
            case "1":
                menuRemoverServico() ;
                break;
            case "2":
                menuAtribuirServico();
                break;
            case "3":
                menuAgendarServico();
                break;
            case "0":
                menuPrincipal();
                break;
            default:
                System.out.println("Essa opção não está diponível.");
                funcionarioUI();
        }
    }

    public void menuRemoverServico() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Remoção de Serviços ---\n");

        System.out.println("Digite a matrícula o Carro: ");
        String matricula = scanner.nextLine();
        System.out.println("Digite o ID do Cliente: ");
        String idCliente = scanner.nextLine();
        System.out.println("Digite o nome do serviço que pretende remover: ");
        String nomeServico = scanner.nextLine();

        String output = this.sistemaFacade.removerServico(matricula, idCliente, nomeServico);

        if (output.equals("null")) {
            System.out.println("Serviço não removido.");
            System.out.println("Esse serviço não se encontra atribuído ao veículo em questão.");
        } else System.out.println("Serviço removido.");

        funcionarioUI();
    }

    public void menuAtribuirServico() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Atribuição de Serviços ---\n");

        System.out.println("Digite a matrícula o Carro: ");
        String matricula = scanner.nextLine();
        System.out.println("Digite o ID do Cliente: ");
        String idCliente = scanner.nextLine();
        System.out.println("Digite o nome do Serviço: ");
        String nomeServico = scanner.nextLine();

        String output = this.sistemaFacade.atribuirServico(matricula, idCliente, nomeServico);

        if (output.equals("null")) {
            System.out.println("Serviço não atribuído.");
            System.out.println("Esse serviço não pode ser atribuído ao carro em questão.");
        } else System.out.println("Serviço atribuído.");

        funcionarioUI();
    }



    public void menuAgendarServico() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Agendamento de Serviços ---\n");

        System.out.println("Digite a matrícula o Carro: ");
        String matricula = scanner.nextLine();
        System.out.println("Digite o ID do Cliente: ");
        String idCliente = scanner.nextLine();
        System.out.println("Digite o nome do Serviço: ");
        String nomeServico = scanner.nextLine();

        int output = this.sistemaFacade.agendarServico(matricula, idCliente, nomeServico);

        if (output == -1) {
            System.out.println("Serviço não agendado.");
            System.out.println("Não existe disponibilidade no horário escolhido.");
        } else System.out.println("Serviço agendado.\n Estimativa de tempo: " + output + "horas \n");

        funcionarioUI();
    }





}