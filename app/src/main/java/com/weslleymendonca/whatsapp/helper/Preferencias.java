package com.weslleymendonca.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by weslley on 17/08/17.
 * Classe responsavel por salvar os dados do usuário em um arquivo.
 */

public class Preferencias {

    private final String NOME_ARQUIVO   = "whatsapp.preferencias";
    private final String CHAVE_EMAIL    = "email";
    private final String CHAVE_SENHA    = "senha";
    private final String CHAVE_ID       = "idUsuario";
    private final int MODE = 0;

    private Context contexto;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    //Metoo para criar arquivo MODE=0 significa que somente esse app pode manipular o arquivo
    public Preferencias(Context contextoParametro) {

        contexto    = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor      = preferences.edit();


    }

    public void salvarDados(String idUsuario){
        editor.putString(CHAVE_ID, idUsuario);
        editor.commit();
    }

    public void apagarDados(){
        editor.clear();
        editor.commit();
    }


    //Metoo para salvar dados do usuário dentro do arquivo
    public void salvarUsuarioPreferencia(String email, String senha){

        editor.putString(CHAVE_EMAIL, email);
        editor.putString(CHAVE_SENHA, senha);
        editor.commit();
    }

    //Metoo para recuperar dados do usuário gravados no arquivo
    public HashMap<String, String> getDadosUsuario(){

        HashMap<String, String> dadosUsuario = new HashMap<>();

        //preferences.getString(CHAVE, "valor a ser exibido caso nao exista valor na chave")
        dadosUsuario.put(CHAVE_EMAIL, preferences.getString(CHAVE_EMAIL, null));
        dadosUsuario.put(CHAVE_SENHA, preferences.getString(CHAVE_SENHA, null));

        return dadosUsuario;

    }

    public String getIdUsuario(){
        return preferences.getString(CHAVE_ID, null);
    }
}
