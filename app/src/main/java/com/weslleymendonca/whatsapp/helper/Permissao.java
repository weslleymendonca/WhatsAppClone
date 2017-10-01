package com.weslleymendonca.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weslley on 21/08/17.
 * Classe responsavel por dar permissoes ao usuario para usar recursos do telefone
 */

public class Permissao {

    public static boolean validaPermissoes(Activity activity, String[] permissoes, int requestCode){

        if(Build.VERSION.SDK_INT >=23){

            List<String> listaPermissoes = new ArrayList<String>();

            //percorrer o array de permissoes
            for(String permissao : permissoes){

                //se ContextCompact.checkSelfPermission() for igual a PackageManager.PERMISSION_GRANTED,
                // validaPermissao sera igual a verdadeiro
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if(!validaPermissao)listaPermissoes.add(permissao);
            }

            //caso a lista esteja vazia retorna true
            if(listaPermissoes.isEmpty()) return true;

            //convertendo a listaPermissoes em array de strings
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //Solicitar permissao
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);


        }

        return true;
    }
}
