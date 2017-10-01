package com.weslleymendonca.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.model.Contato;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usuario on 07/09/17.
 */

public class ContatoAdapter extends ArrayAdapter<Contato>{

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatoAdapter(@NonNull Context c, @NonNull ArrayList<Contato> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //verifica se a lista est[a vazia
        if( contatos != null){
            /*
                Inicializar objeto para exibição da view
                o metódo getSysytemService() serve para recuperar um serviços globais
                dentro da aplicacao e foi utilizado para recuperar o objeto do tipo
                LayoutInflater

            */
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar a view apartir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            //recuperando elemento para exibicao
            TextView nomeContato  = (TextView) view.findViewById(R.id.tv_nome);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_email);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNomeContato());
            emailContato.setText(contato.getEmailContato());
        }

        return view;
    }
}
