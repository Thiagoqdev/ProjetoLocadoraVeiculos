package services;

import entities.locadora.Locadora;
import entities.usuario.*;
import repositories.UsuarioRepository;
import utils.ConsoleColors;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UsuarioService {

    private static final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public static void adicionar(Scanner input) {
        int opcao = mostrarMenuDeAdicao(input);
        input.nextLine();

        if (opcao == 4) {
            return;
        }

        System.out.println("Digite o nome do usuario: ");
        String nome = input.nextLine();
        System.out.println("Digite o email do usuário: ");
        String email = input.nextLine();

        Integer novoIdUsuario = obterUltimoIdUsuario() + 1;
        Integer novoIdCliente = obterUltimoIdCliente() + 1;

        adicionarUsuarioPorTipo(opcao, input, novoIdUsuario, email, nome, novoIdCliente);
    }

    private static void adicionarUsuarioPorTipo(int opcao, Scanner input, Integer idUsuario, String email, String nome, Integer idCliente) {
        switch (opcao) {
            case 1 -> adicionarPessoaFisica(input, idUsuario, email, nome, idCliente);
            case 2 -> adicionarPessoaJuridica(input, idUsuario, email, nome, idCliente);
            case 3 -> adicionarAdministrador(input, idUsuario, email, nome);
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void adicionarAdministrador(Scanner input, Integer idUsuario, String email, String nome) {
        if (!emailExistente(email)) {
            System.out.println("Digite o número de registro do funcionário: ");
            Integer numeroRegistro = null;

            while (numeroRegistro == null) {
                try {
                    numeroRegistro = input.nextInt();
                    input.nextLine();
                    Administrador adm = new Administrador(idUsuario, nome, email, numeroRegistro);
                    usuarioRepository.adicionar(adm);
                    System.out.println(ConsoleColors.GREEN_BOLD + "Usuário adicionado com sucesso!" + ConsoleColors.RESET);
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
                    input.nextLine();
                }
            }
        } else {
            System.out.println(ConsoleColors.YELLOW + "\nEmail já cadastrado. Não é possível adicionar o usuário." + ConsoleColors.RESET);
        }
    }

    private static void adicionarPessoaJuridica(Scanner input, Integer idUsuario, String email, String nome, Integer idCliente) {
        if (!emailExistente(email)) {
            System.out.println("Digite o CNPJ do usuário: ");
            String cnpj = null;

            while (cnpj == null) {
                try {
                    cnpj = input.nextLine();
                    if (cnpj.trim().isEmpty()) {
                        System.out.println("CNPJ não pode ser vazio. Por favor, digite novamente: ");
                        cnpj = null;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um CNPJ válido.");
                    input.nextLine();
                }
            }

            PessoaJuridica pj = new PessoaJuridica(idUsuario, nome, email, idCliente, cnpj);
            usuarioRepository.adicionar(pj);
            System.out.println(ConsoleColors.GREEN_BOLD + "Usuário " + nome + " adicionado com sucesso!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.YELLOW + "\nEmail já cadastrado. Não é possível adicionar o usuário." + ConsoleColors.RESET);
        }
    }

    private static void adicionarPessoaFisica(Scanner input, Integer idUsuario, String email, String nome, Integer idCliente) {
        if (!emailExistente(email)) {
            String cpf = null;
            System.out.println("Digite o CPF do usuário: ");

            while (cpf == null) {
                try {
                    cpf = input.nextLine();
                    if (cpf.trim().isEmpty()) {
                        System.out.println("CPF não pode ser vazio. Por favor, digite novamente: ");
                        cpf = null;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida. Por favor, digite um CPF válido.");
                    input.nextLine();
                }
            }

            PessoaFisica pf = new PessoaFisica(idUsuario, nome, email, idCliente, cpf);
            usuarioRepository.adicionar(pf);
            System.out.println(ConsoleColors.GREEN_BOLD + "Usuário " + nome + " adicionado com sucesso!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.YELLOW + "\nEmail já cadastrado. Não é possível adicionar o usuário." + ConsoleColors.RESET);
        }
    }

    private static boolean emailExistente(String email) {
        return usuarioRepository.buscar(email).isPresent();
    }

    private static int mostrarMenuDeAdicao(Scanner input) {
        System.out.println("Digite o tipo de cliente que deseja adicionar: ");
        System.out.println("1. Pessoa Física (PF)");
        System.out.println("2. Pessoa Jurídica (PJ)");
        System.out.println("3. Operador/Administrador");
        System.out.println("4. Sair");

        return input.nextInt();

    }

    public static void editar(Scanner input) {
        System.out.println("Digite o email do usuário que deseja alterar dados: ");
        String email = input.nextLine();
        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(email);

        if (usuarioOptional.isEmpty()) {
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + "\nUsuário não encontrado.\n" + ConsoleColors.RESET);
            return;
        }

        Usuario usuario = usuarioOptional.get();

        atualizarEmail(usuario, input);
        atualizarNome(usuario, input);

        if (usuario instanceof Administrador administrador) {
            atualizarNumeroRegistro(administrador, input);
        } else if (usuario instanceof PessoaFisica pessoaFisica) {
            atualizarCpf(pessoaFisica, input);
        } else if (usuario instanceof PessoaJuridica pessoaJuridica) {
            atualizarCnpj(pessoaJuridica, input);
        }

        usuarioRepository.editar(usuario, email);
    }

    private static void atualizarEmail(Usuario usuario, Scanner input) {
        System.out.println("Digite um novo email para o usuário ou tecle <ENTER> para manter o mesmo: ");
        String novoEmail = input.nextLine();

        while (!novoEmail.isEmpty() && emailExistente(novoEmail)) {
            System.out.println("Email já cadastrado para outro usuário.");
            System.out.println("Digite um novo email para o usuário ou tecle <ENTER> para manter o mesmo: ");
            novoEmail = input.nextLine().trim();
        }


        usuario.setEmail(novoEmail.isEmpty() ? usuario.getEmail() : novoEmail);
    }

    private static void atualizarNome(Usuario usuario, Scanner input) {
        System.out.println("Digite o novo nome de usuário ou tecle <ENTER> para manter o mesmo: ");
        String novoNome = input.nextLine();
        usuario.setNome(novoNome.isEmpty() ? usuario.getNome() : novoNome);
    }

    private static void atualizarNumeroRegistro(Administrador administrador, Scanner input) {
        System.out.println("Digite o novo número de registro do funcionário ou tecle <ENTER> para manter o mesmo: ");
        String entrada = input.nextLine();
        Integer novoRegistro = entrada.isEmpty() ? administrador.getNumeroRegistro() : Integer.parseInt(entrada);
        administrador.setNumeroRegistro(novoRegistro);
    }

    private static void atualizarCpf(PessoaFisica pessoaFisica, Scanner input) {
        System.out.println("Digite o novo CPF ou tecle <ENTER> para manter o mesmo: ");
        String novoCpf = input.nextLine();
        pessoaFisica.setCpf(novoCpf.isEmpty() ? pessoaFisica.getCpf() : novoCpf);
    }

    private static void atualizarCnpj(PessoaJuridica pessoaJuridica, Scanner input) {
        System.out.println("Digite o novo CNPJ ou tecle <ENTER> para manter o mesmo: ");
        String novoCnpj = input.nextLine();
        pessoaJuridica.setCnpj(novoCnpj.isEmpty() ? pessoaJuridica.getCnpj() : novoCnpj);
    }

    public static void buscar(Scanner input) {
        System.out.println("Digite o email do usuario: ");
        String email = input.nextLine(); // TODO: validação de email?

        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(email);

        usuarioOptional.ifPresentOrElse(usuario -> {
            if (usuario instanceof Administrador administrador) {
                System.out.println("\n" + administrador.mostrarAdmin());
            } else if (usuario instanceof PessoaFisica pessoaFisica) {
                System.out.println("\n" + pessoaFisica.mostrarPF());
            } else if (usuario instanceof PessoaJuridica pessoaJuridica) {
                System.out.println("\n" + pessoaJuridica.mostrarPJ());
            }
        }, () -> {
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + "\nUsuário não encontrado.\n" + ConsoleColors.RESET);
        });
    }

    public static Optional<Cliente> buscarCliente(String email) {
        return usuarioRepository.buscar(email)
                .filter(usuario -> usuario instanceof Cliente)
                .map(usuario -> (Cliente) usuario);
    }

    public static void buscarPorParteDoNome(Scanner input) {
        System.out.println("Digite uma parte do nome do usuário: ");
        String parteNome = input.nextLine();

        List<Usuario> usuarios = UsuarioRepository.buscarPorParteDoNome(parteNome);

        for (Usuario usuario : usuarios) {
            System.out.println(usuario.mostrarUsuario());
        }
    }

    public static void remover(Scanner input) {
        System.out.println("Digite o email do usuário: ");
        String email = input.nextLine();

        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuarioRepository.remover(usuario);
            System.out.println("Usuário removido com sucesso.");
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    public static void listar(Scanner input) {
        List<Usuario> usuarios = usuarioRepository.listar();

        int tamanhoPagina = 2; // quantidade de itens por página
        int totalPaginas = (int) Math.ceil((double) usuarios.size() / tamanhoPagina);
        int paginaAtual = 1;

        while (true) {
            exibirPaginaUsuarios(usuarios, paginaAtual, tamanhoPagina);
            System.out.println("\nPágina " + paginaAtual + " de " + totalPaginas);
            System.out.println("[P] Próxima página | [A] Página anterior | [S] Sair");

            String opcao = input.nextLine().toUpperCase();
            if (opcao.equals("P") && paginaAtual < totalPaginas) {
                paginaAtual++;
            } else if (opcao.equals("A") && paginaAtual > 1) {
                paginaAtual--;
            } else if (opcao.equals("S")) {
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Comando inválido!" + ConsoleColors.RESET);
            }
        }
    }

    private static void exibirPaginaUsuarios(List<Usuario> usuarios, int pagina, int tamanhoPagina) {
        int inicio = (pagina - 1) * tamanhoPagina;
        int fim = Math.min(inicio + tamanhoPagina, usuarios.size());
        System.out.println(ConsoleColors.GREEN_BOLD + "\n||> Usuários cadastrados <||" + ConsoleColors.RESET);

        for (int i = inicio; i < fim; i++) {
            if (usuarios.get(i) instanceof Administrador administrador) {
                System.out.println(administrador.mostrarAdmin());
            } else if (usuarios.get(i) instanceof PessoaFisica pessoaFisica) {
                System.out.println(pessoaFisica.mostrarPF());
            } else if (usuarios.get(i) instanceof PessoaJuridica pessoaJuridica) {
                System.out.println(pessoaJuridica.mostrarPJ());
            } else {
                System.out.println("Usuário de tipo desconhecido");
            }
        }
    }

    private static Integer obterUltimoIdCliente() {
        return Locadora.getUsuarios().stream()
                .filter(u -> u instanceof Cliente)
                .map(u -> ((Cliente) u).getIdCliente())
                .max(Integer::compareTo)
                .orElse(0);
    }


    private static Integer obterUltimoIdUsuario() {
        return Locadora.getUsuarios().stream()
                .map(Usuario::getId)
                .max(Integer::compareTo)
                .orElse(0);
    }
}