package com.apphamba.hamba.infra.adaptersFragmentos.TituloLista.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.apphamba.hamba.infra.servicos.FiltroTitulo;
import com.apphamba.hamba.infra.adaptersFragmentos.TituloLista.adapter.TituloAdapter;
import com.apphamba.hamba.titulo.dominio.Titulo;
import com.apphamba.hamba.titulo.gui.DetalhesActivity;
import com.apphamba.hamba.infra.adaptersFragmentos.TituloLista.TituloView;
import com.apphamba.hamba.titulo.servicos.ServicoTitulo;
import java.util.ArrayList;
import java.util.List;

public class TituloListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<TituloView> titulosView;
    private ActionMode actionMode;
    private ServicoTitulo servicoTitulo = new ServicoTitulo();
    private SwipeRefreshLayout swipeLayout;

    public static TituloListFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        TituloListFragment fragment = new TituloListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_titulos_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        titulosView = this.tituloToTituloView(FiltroTitulo.instance.getTitulosList());
        recyclerView.setAdapter(new TituloAdapter(getContext(), titulosView, onClickTitulo()));
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        return view;
    }

    private TituloAdapter.TituloOnClickListener onClickTitulo() {
        return new TituloAdapter.TituloOnClickListener() {
            @Override
            public void onClickTitulo(TituloAdapter.TitulosViewHolder holder, int indexTitulo) {
                Titulo titulo = titulosView.get(indexTitulo).getTitulo();
                if (actionMode == null) {
                    detalharTitulo(titulo);
                } else {
                    selecionarItem(indexTitulo);
                }
            }
            @Override
            public void onLongClickTitulo(TituloAdapter.TitulosViewHolder holder, int indexTitulo) {
                if (actionMode != null) {
                    return;
                }
                iniciarActionMode(indexTitulo);
            }
        };
    }

    private void selecionarItem(int indexTitulo) {
        titulosView.get(indexTitulo).setSelecionado(true);
        updateActionModeTitle();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void iniciarActionMode(int indexTitulo) {
        actionMode = getAppCompatActivity().
                startSupportActionMode(getActionModeCallback());
        TituloView titulo = titulosView.get(indexTitulo);
        titulo.setSelecionado(true);
        recyclerView.getAdapter().notifyDataSetChanged();
        updateActionModeTitle();
    }

    private void detalharTitulo(Titulo titulo) {
        Toast.makeText(getContext(), titulo.getNome(), Toast.LENGTH_SHORT).show();
        FiltroTitulo.instance.setTituloSelecionado(titulo);
        Intent intent = new Intent(getContext(), DetalhesActivity.class);
        startActivity(intent);
    }

    private void updateActionModeTitle() {
        if (actionMode != null) {
            actionMode.setTitle("Selecione os titulos.");
            actionMode.setSubtitle(null);
            List<TituloView> titulosSelecionados = getTitulosSelecionados();
            if (titulosSelecionados.size() == 1) {
                actionMode.setSubtitle("1 titulo selecionado");
            } else if (titulosSelecionados.size() > 1) {
                actionMode.setSubtitle(titulosSelecionados.size() + " titulos selecionados");
            }
        }
    }

    private List<TituloView> getTitulosSelecionados() {
        List<TituloView> list = new ArrayList<TituloView>();
        for (TituloView titulo : this.titulosView) {
            if (titulo.getSelecionado()) {
                list.add(titulo);
            }
        }
        return list;
    }

    private ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_frag_titulos_cab, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                selecionarAcao(item);
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                for (TituloView titulo : titulosView) {
                    titulo.setSelecionado(false);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }

    private void selecionarAcao(MenuItem item) {
        List<TituloView> selectedTitulos = getTitulosSelecionados();
        if (item.getItemId() == R.id.action_adicionar_meu_hamba) {
            adicionarMeuHamba(selectedTitulos);
            snack(recyclerView, "Títulos adicionados com sucesso.");
        } else if (item.getItemId() == R.id.action_adicionar_meus_fav) {
            adicionarFavorito(selectedTitulos);
            snack(recyclerView, "Títulos adicionados com sucesso.");
        } else if (item.getItemId() == R.id.action_remove_fav) {
            removerFavorito(selectedTitulos);
            snack(recyclerView, "Títulos excluídos com sucesso.");
        } else if (item.getItemId() == R.id.action_remove_meu_hamba) {
            removerMeuHamba(selectedTitulos);
            snack(recyclerView, "Títulos excluídos com sucesso.");
        }
    }

    private void adicionarMeuHamba(List<TituloView> selectedTitulos){
        for (TituloView tituloView : selectedTitulos) {
            if (!servicoTitulo.isMeuHamba(tituloView.getTitulo())) {
                servicoTitulo.adicionarMeuHamba(tituloView.getTitulo());
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    private void removerMeuHamba(List<TituloView> selectedTitulos){
        for (TituloView tituloView : selectedTitulos) {
            if (servicoTitulo.isMeuHamba(tituloView.getTitulo())) {
                servicoTitulo.removerMeuHamba(tituloView.getTitulo());
            }
        }
    }

    private void adicionarFavorito(List<TituloView> selectedTitulos){
        for (TituloView tituloView : selectedTitulos) {
            if (!servicoTitulo.isFavorito(tituloView.getTitulo())){
                servicoTitulo.adicionarFavorito(tituloView.getTitulo());
            }
        }
    }
    private void removerFavorito(List<TituloView> selectedTitulos){
        for (TituloView tituloView : selectedTitulos){
            if (servicoTitulo.isFavorito(tituloView.getTitulo())) {
                servicoTitulo.removerFavorito(tituloView.getTitulo());
            }
        }
    }

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    protected void snack(View view, String msg) {
        Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG)
                .show();
    }

    private ArrayList<TituloView> tituloToTituloView(ArrayList<Titulo> titulos){
        ArrayList<TituloView> tituloViews = new ArrayList<>();
        for (Titulo titulo:titulos) {
            TituloView tituloView = new TituloView();
            tituloView.setTitulo(titulo);
            tituloViews.add(tituloView);
        }
        return tituloViews;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }

}