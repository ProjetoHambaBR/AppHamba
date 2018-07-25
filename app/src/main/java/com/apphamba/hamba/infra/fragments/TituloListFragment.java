package com.apphamba.hamba.infra.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apphamba.hamba.R;
import com.apphamba.hamba.infra.adapter.TituloAdapter;
import com.apphamba.hamba.titulo.dominio.Titulo;
import com.apphamba.hamba.titulo.gui.TituloActivity;
import com.apphamba.hamba.titulo.servicos.ServicoTitulo;

import java.util.ArrayList;
import java.util.List;

public class TituloListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Titulo> titulos;
    private ActionMode actionMode;
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
        titulos = servicoTitulo.getTitulos();
        recyclerView.setAdapter(new TituloAdapter(getContext(), titulos, onClickTitulo()));

        return view;
    }

    private TituloAdapter.TituloOnClickListener onClickTitulo() {
        return new TituloAdapter.TituloOnClickListener() {
            @Override
            public void onClickTitulo(TituloAdapter.TitulosViewHolder holder, int indexTitulo) {
                Titulo titulo = titulos.get(indexTitulo);
                Toast.makeText(getContext(), titulo.getNome(), Toast.LENGTH_SHORT).show();
                //abaixo modif Anderson + mandando o objeto titulo
                //Intent intent = new Intent(getContext(), DetalhesActivity.class);
                //intent.putExtra("Titulo", titulo);
                //startActivity(intent);

                Intent intent = new Intent(getContext(), TituloActivity.class);
                //AJEITAR AQUI EMBAIXO INTEIRO ---------- CHAMAR A FUNÇÃO
                intent.putExtra("imgTitulo", titulo.getImagem());

                startActivity(intent);

                // (1) Start activity com animação
//                String key = getString(R.string.transition_key);
//                ImageView img = holder.img;
//                ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), img, key);
//                ActivityCompat.startActivity(getActivity(), intent, opts.toBundle());
            }

            @Override
            public void onLongClickTitulo(TituloAdapter.TitulosViewHolder holder, int indexTitulo) {
                if (actionMode != null) {
                    return;
                }
                // Liga a action bar de contexto (CAB)
                actionMode = getAppCompatActivity().
                        startSupportActionMode(getActionModeCallback());
                Titulo titulo = titulos.get(indexTitulo);
                titulo.selected = true; // Seleciona o titulo
                // Solicita ao Android para desenhar a lista novamente
                recyclerView.getAdapter().notifyDataSetChanged();
                // Atualiza o título para mostrar a quantidade de titulos selecionados
                updateActionModeTitle();


            }
        };
    }
    // Atualiza o título da action bar (CAB)
    private void updateActionModeTitle() {
        if (actionMode != null) {
            actionMode.setTitle("Selecione os titulos.");
            actionMode.setSubtitle(null);
            List<Titulo> selectedTitulos = getSelectedTitulos();
            if (selectedTitulos.size() == 1) {
                actionMode.setSubtitle("1 titulo selecionado");
            } else if (selectedTitulos.size() > 1) {
                actionMode.setSubtitle(selectedTitulos.size() + " titulos selecionados");
            }
            //função abaixo que chama o metódo de compartilhamento, usando a lista de selecionados, ainda n colocado
           // updateShareIntent(selectedTitulos);
        }
    }
    // Retorna a lista de carros selecionados
    private List<Titulo> getSelectedTitulos() {
        List<Titulo> list = new ArrayList<Titulo>();
        for (Titulo titulo : titulos) {
            if (titulo.selected) {
                list.add(titulo);
            }
        }
        return list;
    }
    private ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Infla o menu específico da action bar de contexto (CAB)
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_frag_titulos_cab, menu);
                MenuItem shareItem = menu.findItem(R.id.action_share);
//                ShareActionProvider share = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//                shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                shareIntent.setType("text/plain");
//                share.setShareIntent(shareIntent);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                List<Titulo> selectedTitulos = getSelectedTitulos();
                if (item.getItemId() == R.id.action_remove) {
                    //CarroDB db = new TituloDB(getContext());
                    try {
                        for (Titulo titulo : selectedTitulos) {
                           // db.delete(titulo); // Deleta o titulo do banco
                            //titulos.remove(titulo); // Remove da lista
                        }
                    } finally {
                       // db.close();
                    }
                    snack(recyclerView, "Títulos excluídos com sucesso.");

                } else if (item.getItemId() == R.id.action_share) {
                    // Dispara a tarefa para fazer download das fotos
                    //startTask("compartilhar", new CompartilharTask(selectedCarros));

                }
                // Encerra o action mode
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Limpa o estado
                actionMode = null;
                // Configura todos os titulos para não selecionados
                for (Titulo titulo : titulos) {
                    titulo.selected = false;
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }
    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }
    protected void snack(View view, String msg) {
        snack(view, msg, null);
    }
    protected void snack(View view, String msg, final Runnable runnable) {
        Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                })
                .show();
    }

}
