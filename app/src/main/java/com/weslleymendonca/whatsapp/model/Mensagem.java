package com.weslleymendonca.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;

/**
 * Created by usuario on 07/09/17.
 */

public class Mensagem {
    private String idUsuario;
    private String mensagem;


    public Mensagem() {
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }


    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


    public void salvarRemetente(String idContato){

        String contatoId = idContato;

        DatabaseReference referenciaFirebase;
        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("mensagens")
                .child(getIdUsuario())
                .child(contatoId)
                .push() //cria um identificador unico no banco do firebase
                .setValue(Mensagem.this);

    }

    public void salvarDestinatario(String idContato){

        String contatoId = idContato;

        DatabaseReference referenciaFirebase;
        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("mensagens")
                .child(contatoId)
                .child(getIdUsuario())
                .push() //cria um identificador unico no banco do firebase
                .setValue(Mensagem.this);

    }
}
