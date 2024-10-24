package entities.agencia;

import java.util.Objects;
import java.util.Optional;

public class Agencia implements Comparable<Agencia> {

    private final Integer codigo;
    private String nome;
    private Endereco endereco; // ID

    public Agencia(Integer codigo, String nome, Endereco endereco) {
        this.codigo = codigo;
        this.nome = nome;
        this.endereco = endereco;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public Optional<String> getNome() {
        return Optional.ofNullable(nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Optional<Endereco> getEndereco() {
        return Optional.ofNullable(endereco);
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agencia agencia = (Agencia) o;
        return Objects.equals(codigo, agencia.codigo) && Objects.equals(nome, agencia.nome) && Objects.equals(endereco, agencia.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nome, endereco);
    }

    public String mostrarAgencia() {
        return "Nome da agência: " + getNome() + "\n" +
                endereco.mostrarEndereco();
    }

    @Override
    public String toString() {
        return codigo + ";" +
                nome + ";" + endereco.toString();
    }

    public static Agencia fromString(String linha) {
        String[] partes = linha.split(";");
        Integer codigo = Integer.parseInt(partes[0]);
        String nome = partes[1];
        String logradouro = partes[2];
        Integer numero = Integer.parseInt(partes[3]);
        String complemento = partes[4];
        String cidade = partes[5];
        String estado = partes[6];
        String cep = partes[7];
        Endereco endereco = new Endereco(logradouro, numero, complemento, cidade, estado, cep);

        return new Agencia(codigo, nome, endereco);
    }

    @Override
    public int compareTo(Agencia o) {
        return this.nome.compareTo(o.nome);
    }
}
