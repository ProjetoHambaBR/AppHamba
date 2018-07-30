package com.apphamba.hamba.titulo.gui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.apphamba.hamba.R;
import com.apphamba.hamba.infra.fragments.TituloFragment;


public class TituloActivity extends CollapsingToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulo);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        criarFragment(savedInstanceState);
    }

    private void criarFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            TituloFragment frag = new TituloFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragContainer, frag, null).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}
