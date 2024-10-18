package repositories;

import entities.locadora.Locadora;
import entities.veiculo.Veiculo;
import utils.persistencia.LocadoraUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class VeiculoRepository implements Repositorio<Veiculo, String> {

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Override
    public void adicionar(Veiculo veiculo) {
        Locadora.getVeiculos().add(veiculo);
        executor.submit(LocadoraUtils::salvarDadosLocadora);

    }

    @Override
    public void editar(Veiculo veiculo, String placaAntiga) {
        buscar(placaAntiga).ifPresent(antigo -> {
            antigo.setPlaca(veiculo.getPlaca());
            antigo.setCor(veiculo.getCor());
            executor.submit(LocadoraUtils::salvarDadosLocadora);
        });
    }

    @Override
    public Optional<Veiculo> buscar(String placa) {
        return Locadora.getVeiculos().stream()
                .filter(v -> v.getPlaca().equals(placa))
                .findFirst();
    }


    public Veiculo remover(Veiculo veiculo) {
        int index = Locadora.getVeiculos().indexOf(veiculo);
        if (index != -1) {
            Veiculo removido = Locadora.getVeiculos().remove(index);
            executor.submit(() -> {
                LocadoraUtils.salvarDadosLocadora();
            });
            return removido;
        }
        return null;
    }

    public List<Veiculo> buscarPorModelo(String modelo) {
        return Locadora.getVeiculos().stream()
                .filter(v -> v.getModelo().contains(modelo))
                .collect(Collectors.toList());
    }

    public List<Veiculo> listar() {
        return Locadora.getVeiculos().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Veiculo buscarPorId(Integer codigo) {
        return Locadora.getVeiculos().stream()
                .filter(v -> v.getId().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public List<Veiculo> bucarVeiculosDisponiveis() {
        return Locadora.getVeiculos().stream()
                .filter(Veiculo::isDisponivel)
                .sorted()
                .collect(Collectors.toList());
    }
}