package com.apphamba.hamba.titulo.servicos;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apphamba.hamba.infra.DataBase;
import com.apphamba.hamba.infra.Sessao;
import com.apphamba.hamba.titulo.dominio.Titulo;
import com.apphamba.hamba.titulo.persistencia.FavoritoDao;
import com.apphamba.hamba.titulo.persistencia.TituloDao;
import com.apphamba.hamba.usuario.dominio.Usuario;

import java.util.ArrayList;

public class ServicoTitulo {

    public ArrayList<Titulo> getTitulos(){
        TituloDao tituloDao = new TituloDao();
        ArrayList<Titulo> titulos = tituloDao.loadTitulos();
        return titulos;
    }

    public Titulo buscarTituloPorNome(String nome) {
        TituloDao tituloDao = new TituloDao();
        return tituloDao.getByNome(nome);
    }

    public ArrayList<Titulo> getFavoritos() {
        Usuario usuario = Sessao.instance.getPessoa().getUsuario();
        FavoritoDao favoritoDao = new FavoritoDao();
        return favoritoDao.loadFavoritos(usuario);
    }

}