package com.weslleymendonca.whatsapp.model;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.weslleymendonca.whatsapp.activity.CadastroUsuarioActivity;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;

/**
 * Created by usuario on 24/08/17.
 */

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;


    //construtor
    public Usuario() {

    }


    public void salvar(){

        DatabaseReference referenciaFirebase;
        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(Usuario.this);

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
