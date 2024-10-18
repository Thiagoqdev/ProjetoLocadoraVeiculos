package entities.agencia;

import java.util.Objects;
import java.util.Optional;

public class Endereco {

    private String logradouro;
    private Integer numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;

    public Endereco(String logradouro, Integer numero, String complemento, String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Optional<String> getLogradouro() {
        return Optional.ofNullable(logradouro);
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Optional<Integer> getNumero() {
        return Optional.ofNullable(numero);
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Optional<String> getComplemento() {
        return Optional.ofNullable(complemento);
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Optional<String> getCidade() {
        return Optional.ofNullable(cidade);
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Optional<String> getEstado() {
        return Optional.ofNullable(estado);
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Optional<String> getCep() {
        return Optional.ofNullable(cep);
    }

    public void setCep(String cep) {
        this.cep = cep;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(logradouro, endereco.logradouro)
                && Objects.equals(numero, endereco.numero)
                && Objects.equals(complemento, endereco.complemento)
                && Objects.equals(cidade, endereco.cidade)
                && Objects.equals(estado, endereco.estado)
                && Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logradouro, numero, complemento, cidade, estado, cep);
    }

    public String mostrarEndereco() {
        return "Logradouro: " + getLogradouro().orElse("Desconhecido") +
                "\nNumero: " + getNumero().map(String::valueOf).orElse("Desconhecido") +
                "\nComplemento: " + getComplemento().orElse("Desconhecido") +
                "\nCidade: " + getCidade().orElse("Desconhecido") +
                "\nEstado: " + getEstado().orElse("Desconhecido") +
                "\nCEP: " + getCep().orElse("Desconhecido");
    }

    @Override
    public String toString() {
        return logradouro + ";" +
               numero + ";" +
               complemento + ";" +
               cidade + ";" +
               estado + ";" +
               cep;
    }
}
