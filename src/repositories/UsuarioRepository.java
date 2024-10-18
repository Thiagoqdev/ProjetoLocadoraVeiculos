package repositories;

import entities.locadora.Locadora;
import entities.usuario.Administrador;
import entities.usuario.PessoaFisica;
import entities.usuario.PessoaJuridica;
import entities.usuario.Usuario;
import utils.persistencia.LocadoraUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository implements Repositorio<Usuario, String> {
    @Override
    public void adicionar(Usuario usuario) {
        Locadora.getUsuarios().add(usuario);
        LocadoraUtils.salvarDadosLocadora();
    }

    @Override
    public void editar(Usuario usuario, String email) {
        buscar(email).ifPresent(antigo -> {
            int index = Locadora.getUsuarios().indexOf(antigo);
            if (index != -1) {
                Usuario editado = Locadora.getUsuarios().get(index);
                editado.setNome(usuario.getNome());
                editado.setEmail(usuario.getEmail());

                if (editado instanceof Administrador adminUser && usuario instanceof Administrador) {
                    adminUser.setNumeroRegistro(((Administrador) usuario).getNumeroRegistro());
                } else if (editado instanceof PessoaFisica pessoaFisica && usuario instanceof PessoaFisica) {
                    pessoaFisica.setCpf(((PessoaFisica) usuario).getCpf());
                } else if (editado instanceof PessoaJuridica pessoaJuridica && usuario instanceof PessoaJuridica) {
                    pessoaJuridica.setCnpj(((PessoaJuridica) usuario).getCnpj());
                }

                LocadoraUtils.salvarDadosLocadora();
            }
        });
    }

    @Override
    public Usuario remover(Usuario usuario) {
        int index = Locadora.getUsuarios().indexOf(usuario);
        if(index != -1) {
            Usuario removido = Locadora.getUsuarios().remove(index);
            LocadoraUtils.salvarDadosLocadora();
            return removido;
        }
        return null;
    }

    public Optional<Usuario> buscar(String email) {
        return Locadora.getUsuarios().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }


    public static List<Usuario> buscarPorParteDoNome(String parteDoNome) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        for (Usuario usuario : Locadora.getUsuarios()) {
            if (usuario.getNome().toLowerCase().contains(parteDoNome.toLowerCase())) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }




    @Override
    public List<Usuario> listar() {
        List<Usuario> usuarios = Locadora.getUsuarios();
        Collections.sort(usuarios);
        return usuarios;
    }
}
