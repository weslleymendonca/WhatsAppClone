package com.weslleymendonca.whatsapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.helper.Preferencias;
import com.weslleymendonca.whatsapp.model.Mensagem;

import java.util.ArrayList;

/**
 * Created by usuario on 07/09/17.
 */

public class ConversaAdapter extends ArrayAdapter<Mensagem>{

    private ArrayList<Mensagem> mensagens;
    private Context context;

    public ConversaAdapter(Context c, ArrayList<Mensagem> m) {
        super(c, 0, m);
        this.mensagens = m;
        this.context = c;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        //verifica se a lista est[a vazia
        if( mensagens != null){

            //recuperar id do usuário
            Preferencias preferencias = new Preferencias(getContext());
            String idUsuario = preferencias.getIdUsuario();
            //Log.i("idUser", "idUser: " +idUsuario);

            /*
                Inicializar objeto para exibição da view
                o metódo getSysytemService() serve para recuperar um serviços globais
                dentro da aplicacao e foi utilizado para recuperar o objeto do tipo
                LayoutInflater

            */
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recuperando Mensagens
            Mensagem mensagem = mensagens.get(position);

            if(idUsuario.equals(mensagem.getIdUsuario())){
                //Montar a view apartir do xml
                view = inflater.inflate(R.layout.conversa_direita, parent, false);
                TextView mensagemUsuario  =  view.findViewById(R.id.tv_usuario);
                mensagemUsuario.setText(mensagem.getMensagem());
            }else{
                //Montar a view apartir do xml
                view = inflater.inflate(R.layout.conversa_esquerda, parent, false);
                TextView mensagemUsuario  =  view.findViewById(R.id.tv_contato);
                mensagemUsuario.setText(mensagem.getMensagem());
            }

            //Montar a view apartir do xml
            //view = inflater.inflate(R.layout.conversa_direita, parent, false);

            //recuperando elemento para exibicao
            //TextView mensagemContato  = (TextView) view.findViewById(R.id.tv_contato);

            //TextView mensagemUsuario  =  view.findViewById(R.id.tv_usuario);


        }

        return view;
    }

}
