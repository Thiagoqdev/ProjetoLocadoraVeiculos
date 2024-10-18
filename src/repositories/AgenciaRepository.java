package repositories;

import entities.agencia.Agencia;
import entities.locadora.Locadora;
import utils.persistencia.LocadoraUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AgenciaRepository implements Repositorio<Agencia, Integer> {

    @Override
    public void adicionar(Agencia agencia) {
        Locadora.getAgencias().add(agencia);
        salvarDados();
    }


    @Override
    public void editar(Agencia agencia, Integer codAgencia) {
        Optional<Agencia> antigaOpt = buscar(codAgencia);
        antigaOpt.ifPresent(antiga -> {
            antiga.setNome(agencia.getNome().orElse(null));
            antiga.setEndereco(agencia.getEndereco().orElse(null));
            salvarDados();
        });
    }

    @Override
    public Agencia remover(Agencia agencia) {
        Optional<Agencia> agenciaOpt = buscar(agencia.getCodigo());
        if (agenciaOpt.isPresent()) {
            Locadora.getAgencias().remove(agenciaOpt.get());
            salvarDados();
            return agenciaOpt.get();
        }
        return null;
    }

    @Override
    public Optional<Agencia> buscar(Integer id) {
        return Locadora.getAgencias().stream()
                .filter(agencia -> agencia.getCodigo().equals(id))
                .findFirst();
    }

    @Override
    public List<Agencia> listar() {
        return Locadora.getAgencias().stream()
                .sorted()
                .collect(Collectors.toList());
    }


    public static List<Agencia> buscarPorParteDoNome(String nome) {
        return Locadora.getAgencias().stream()
                .filter(agencia -> agencia.getNome().map(n -> n.toLowerCase().contains(nome.toLowerCase())).orElse(false) ||
                        agencia.getEndereco().map(endereco -> endereco.getLogradouro().map(log -> log.toLowerCase().contains(nome.toLowerCase())).orElse(false)).orElse(false))
                .collect(Collectors.toList());
    }

    public boolean existeAgenciaComMesmosDados(Agencia agencia) {
        return Locadora.getAgencias().stream()
                .anyMatch(ag -> ag.equals(agencia));
    }

    private void salvarDados() {
        LocadoraUtils.salvarDadosLocadora();
    }
}
