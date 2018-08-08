package com.apphamba.hamba.titulo.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apphamba.hamba.R;
import com.apphamba.hamba.infra.Sessao;
import com.apphamba.hamba.infra.servicos.FiltroTitulo;
import com.apphamba.hamba.titulo.dominio.Titulo;
import com.apphamba.hamba.titulo.servicos.ServicoTitulo;

public class AvaliacaoActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView txtValorAvaliacao;
    private Button botaoAvaliar;
    private double nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);
        setRatingBar();
        addListenerOnButton();
    }

    public void setRatingBar() {
        ratingBar = findViewById(R.id.ratingBar);
        txtValorAvaliacao = findViewById(R.id.txtValorAvaliacao);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float avaliacao, boolean fromUser) {
                txtValorAvaliacao.setText(String.valueOf(avaliacao));
                nota = Double.valueOf(avaliacao);
            }
        });
    }

    public void addListenerOnButton() {
        ratingBar = findViewById(R.id.ratingBar);
        botaoAvaliar = findViewById(R.id.botaoAvaliar);
        botaoAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avaliar(FiltroTitulo.instance.getTituloSelecionado(), regraDeTres(nota));
                Toast.makeText(AvaliacaoActivity.this, "Avaliação feita com sucesso " + nota, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private Double regraDeTres(double nota) {
        double notaFinal = ((int) nota) / 5;
        return (notaFinal);
    }

    public void avaliar(Titulo titulo, Double nota) {
        ServicoTitulo servicoTitulo = new ServicoTitulo();
        servicoTitulo.avaliar(titulo,nota);

    }
}
