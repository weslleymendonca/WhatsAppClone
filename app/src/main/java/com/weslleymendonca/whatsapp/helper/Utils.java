package com.weslleymendonca.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.weslleymendonca.whatsapp.activity.CadastroUsuarioActivity;
import com.weslleymendonca.whatsapp.activity.LoginActivity;
import com.weslleymendonca.whatsapp.activity.MainActivity;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;

/**
 * Created by usuario on 24/08/17.
 */

public class Utils extends Activity{




    public Utils() {
    }

    //Metodo para validar os campos dos formularios
    public boolean verificaCamposLogin(String email, String senha){

        if((!email.isEmpty()) || (!senha.isEmpty())){

            return true;

        }else{

            return false;

        }

    }


}
