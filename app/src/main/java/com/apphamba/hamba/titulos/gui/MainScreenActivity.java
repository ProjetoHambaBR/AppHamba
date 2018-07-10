package com.apphamba.hamba.titulos.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.apphamba.hamba.R;

public class MainScreenActivity extends Activity {
    private ListView listaSeries;
    private String[] itens = {
            "hehe","haha","hoho"
    };

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.main_screen);

        listaSeries = (ListView) findViewById(R.id.list_viewID);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1,itens);

        listaSeries.setAdapter(adaptador);

       listaSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               int codigoPosicao = i;
               String valorClicado = (String) listaSeries.getItemAtPosition(codigoPosicao);
               Toast.makeText(getApplicationContext(),valorClicado,Toast.LENGTH_SHORT).show();

           }
       });
    }
}