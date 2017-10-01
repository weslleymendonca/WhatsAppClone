package com.weslleymendonca.whatsapp.model;

/**
 * Created by usuario on 03/09/17.
 */

public class Contato {

    private String idContato;
    private String nomeContato;
    private String emailContato;


    public Contato() {
    }

    public String getIdContato() {
        return idContato;
    }

    public void setIdContato(String idContato) {
        this.idContato = idContato;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
}
