package com.apphamba.hamba.infra.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apphamba.hamba.R;
import com.apphamba.hamba.infra.adapter.TituloAdapter;
import com.apphamba.hamba.titulo.dominio.Titulo;
import com.apphamba.hamba.titulo.servicos.ServicoTitulo;

import java.util.List;

import static com.apphamba.hamba.infra.HambaApp.getContext;

public class FavoritoListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Titulo> titulos;
    private ServicoTitulo servicoTitulo = new ServicoTitulo();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_titulos_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setHasFixedSize(true);

        //Função abaixo pega os titulos pela função do dominio e get()
        //titulos = Titulo.getTitulos();
        titulos = servicoTitulo.getFavoritos();
        recyclerView.setAdapter(new TituloAdapter(getContext(), titulos, onClickTitulo()));

        return view;
    }

    private TituloAdapter.TituloOnClickListener onClickTitulo() {
        return new TituloAdapter.TituloOnClickListener() {
            @Override
            public void onClickTitulo(TituloAdapter.TitulosViewHolder holder, int indexTitulo) {
                Titulo titulo = titulos.get(indexTitulo);
                Toast.makeText(getContext(), titulo.getNome(), Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getContext(), TituloActivity.class);
                //AJEITAR AQUI EMBAIXO INTEIRO ---------- CHAMAR A FUNÇÃO
                //intent.putExtra("imgTitulo", p.img);

                //startActivity(intent);

                // (1) Start activity com animação
//                String key = getString(R.string.transition_key);
//                ImageView img = holder.img;
//                ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), img, key);
//                ActivityCompat.startActivity(getActivity(), intent, opts.toBundle());
            }
        };
    }

}

