package com.apphamba.hamba.titulo.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apphamba.hamba.infra.DataBase;
import com.apphamba.hamba.infra.EnumTitulos;
import com.apphamba.hamba.titulo.dominio.Titulo;

import java.util.ArrayList;

public class TituloDao {
    private DataBase bancoDados;

    public TituloDao(){
        bancoDados = new DataBase();
    }

    private Titulo criarTitulo(Cursor cursor){
        int indexId = cursor.getColumnIndex(String.valueOf(EnumTitulos.ID));
        int id = cursor.getInt(indexId);

        int indexNome = cursor.getColumnIndex(String.valueOf(EnumTitulos.NOME));
        String nome = cursor.getString(indexNome);

        int indexSinopse = cursor.getColumnIndex(String.valueOf(EnumTitulos.SINOPSE));
        String sinopse = cursor.getString(indexSinopse);

        int indexAvaliacao = cursor.getColumnIndex(String.valueOf(EnumTitulos.AVALIACAO));
        int avaliacao = cursor.getInt(indexAvaliacao);

        int indexGeneros = cursor.getColumnIndex(String.valueOf(EnumTitulos.GENEROS));
        String generos = cursor.getString(indexGeneros);

        int indexCriadores = cursor.getColumnIndex(String.valueOf(EnumTitulos.CRIADORES));
        String criadores = cursor.getString(indexCriadores);

        int indexImagem = cursor.getColumnIndex(String.valueOf(EnumTitulos.IMAGEM));
        byte[] imagem = cursor.getBlob(indexImagem);

        Titulo titulo = new Titulo();
        titulo.setId(id);
        titulo.setNome(nome);
        titulo.setSinopse(sinopse);
        titulo.setAvaliacao(avaliacao);
        titulo.setCriadores(criadores);
        titulo.setGeneros(generos);
        titulo.setImagem(imagem);

        return titulo;
    }

    public ArrayList<Titulo> loadTitulos() {
        String query = "SELECT * FROM titulo";
        return this.loadTitulos(query, null);
    }

    public ArrayList<Titulo> loadTitulos(String query, String[] args) {
        ArrayList<Titulo> titulos = new ArrayList<Titulo>();
        SQLiteDatabase leitorBanco = bancoDados.getWritableDatabase();
        Cursor cursor = leitorBanco.rawQuery(query, args);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                titulos.add(this.criarTitulo(cursor));
            } while (cursor.moveToNext());
        }
        return titulos;
    }

    public void inserir(Titulo titulo) {
        SQLiteDatabase escritorBanco = bancoDados.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(String.valueOf(EnumTitulos.NOME), titulo.getNome());
        valores.put(String.valueOf(EnumTitulos.SINOPSE), titulo.getSinopse());
        valores.put(String.valueOf(EnumTitulos.AVALIACAO), titulo.getAvaliacao());
        valores.put(String.valueOf(EnumTitulos.GENEROS), titulo.getGeneros());
        valores.put(String.valueOf(EnumTitulos.CRIADORES), titulo.getCriadores());
        valores.put(String.valueOf(EnumTitulos.IMAGEM), titulo.getImagem());
        escritorBanco.insert(String.valueOf(EnumTitulos.TABELA_TITULOS), null, valores);
        escritorBanco.close();
    }

    public Titulo getByNome(String nome){
        String query =  "SELECT * FROM titulo " +
                        "WHERE nome = ?";
        String[] args = {nome};
        return this.load(query, args);
    }

    private Titulo load(String query, String[] args) {
        SQLiteDatabase leitorBanco = bancoDados.getReadableDatabase();
        Cursor cursor = leitorBanco.rawQuery(query, args);
        Titulo titulo = null;

        if (cursor.moveToNext()) {
            titulo = criarTitulo(cursor);
        }

        cursor.close();
        leitorBanco.close();
        return titulo;
    }

}
