package com.apphamba.hamba.usuario.servicos;

import android.content.Context;
import android.util.Patterns;

import com.apphamba.hamba.usuario.dominio.Pessoa;
import com.apphamba.hamba.usuario.dominio.Usuario;
import com.apphamba.hamba.usuario.persistencia.PessoaDAO;
import com.apphamba.hamba.usuario.persistencia.UsuarioDAO;

public class ServicoUsuario {

    public boolean verificarCampoVazio(String campo) {
        if (campo.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean verificarEmailExistente(String email, Context context){
        UsuarioDAO usuarioDAO = new UsuarioDAO(context);
        Usuario usuario = usuarioDAO.getByEmail(email);
        if (usuario != null) {
            return true;
        }
        return  false;
    }

    public Usuario criaUsuario(String email, String senha){
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuario;
    }

    public void salvarUsuarioBanco(Usuario usuario, Context context){
        UsuarioDAO usuarioDAO = new UsuarioDAO(context);
        usuarioDAO.inserir(usuario);
    }

    public boolean validarCampoEmail(String email){
        if(verificarCampoVazio(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean login(String email, String senha, Context context){
        UsuarioDAO usuarioDAO = new UsuarioDAO(context);
        Usuario usuario = usuarioDAO.getByEmail(email);
        String senhaUsuario = usuario.getSenha();
        if (senha.equals(senhaUsuario)) {
            return true;
        }
        return false;
    }

}