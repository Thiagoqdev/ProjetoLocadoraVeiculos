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

public class UsuarioRepository implements Repositorio<Usuario, String> {
    @Override
    public void adicionar(Usuario usuario) {
        Locadora.getUsuarios().add(usuario);
        LocadoraUtils.salvarDadosLocadora();
    }

    @Override
    public void editar(Usuario usuario, String email) {
        Usuario antigo = buscar(email);
        int index = Locadora.getUsuarios().indexOf(antigo);
        if(index != -1) {
            Usuario editado = Locadora.getUsuarios().get(index);
            editado.setNome(usuario.getNome());
            editado.setEmail(usuario.getEmail());
            if (editado instanceof Administrador adminUser) {
                adminUser.setNumeroRegistro(((Administrador) usuario).getNumeroRegistro());
            } else if (editado instanceof PessoaFisica pessoaFisica) {
                pessoaFisica.setCpf(((PessoaFisica) usuario).getCpf());
            } else if (editado instanceof PessoaJuridica pessoaJuridica) {
                pessoaJuridica.setCnpj(((PessoaJuridica) usuario).getCnpj());
            }
            LocadoraUtils.salvarDadosLocadora();
        }
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

    @Override
    public Usuario buscar(String email) {
        for(Usuario u : Locadora.getUsuarios()){
            if(u.getEmail().equals(email)){
                return u;
            }
        }
        return null;
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
