package utils.persistencia;

import entities.locadora.Aluguel;
import entities.locadora.Locadora;
import entities.agencia.Agencia;
import entities.usuario.Usuario;
import entities.veiculo.Veiculo;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.Optional;

public abstract class LocadoraUtils {


    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void salvarDadosLocadora() {
        executor.submit(() -> {

            try {
                salvarDadosEmArquivo(Locadora.getUsuarios(), "usuarios.txt");
                salvarDadosEmArquivo(Locadora.getAgencias(), "agencias.txt");
                salvarDadosEmArquivo(Locadora.getVeiculos(), "veiculos.txt");
                salvarDadosEmArquivo(Locadora.getAlugueis(), "alugueis.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void carregarDadosLocadora() throws IOException {
        executor.submit(() -> {
            try {
                Locadora.setUsuarios(carregarListaDeArquivo("usuarios.txt", Usuario.class));
                Locadora.setAgencias(carregarListaDeArquivo("agencias.txt", Agencia.class));
                Locadora.setVeiculos(carregarListaDeArquivo("veiculos.txt", Veiculo.class));
                Locadora.setAlugueis(carregarListaDeArquivo("alugueis.txt", Aluguel.class));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static <T> void salvarDadosEmArquivo(List<T> lista, String arquivo) throws IOException {
        verificaBancoDeDados(arquivo);

        List<String> linhas = lista.stream()
                .map(Object::toString)
                .toList();
        Path path = Paths.get(arquivo);
        Files.write(path, linhas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

    }

    private static <T> List<T> carregarListaDeArquivo(String arquivo, Class<T> classe) throws IOException {
        verificaBancoDeDados(arquivo);

        Path path = Paths.get(arquivo);
        List<String> linhas = Files.readAllLines(path);

        return linhas.stream()
                .map(linha -> converterLinhaParaObjeto(linha, classe))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static <T> Optional<T> converterLinhaParaObjeto(String linha, Class<T> classe) {
        try {
            if (classe.equals(Usuario.class)) {
                return Optional.of(classe.cast(Usuario.fromString(linha)));
            } else if (classe.equals(Agencia.class)) {
                return Optional.of(classe.cast(Agencia.fromString(linha)));
            } else if (classe.equals(Veiculo.class)) {
                return Optional.of(classe.cast(Veiculo.fromString(linha)));
            } else if (classe.equals(Aluguel.class)) {
                return Optional.of(classe.cast(Aluguel.fromString(linha)));
            } else {
                throw new IllegalArgumentException("Tipo desconhecido para conversão: " + classe.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static void verificaBancoDeDados(String arquivo) throws IOException {
        Path path = Paths.get(arquivo);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }
}
