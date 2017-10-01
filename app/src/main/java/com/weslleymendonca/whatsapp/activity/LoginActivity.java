package com.weslleymendonca.whatsapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;
import com.weslleymendonca.whatsapp.helper.Base64Custom;
import com.weslleymendonca.whatsapp.helper.Permissao;
import com.weslleymendonca.whatsapp.helper.Preferencias;
import com.weslleymendonca.whatsapp.helper.Utils;
import com.weslleymendonca.whatsapp.model.Usuario;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private Button   btLogin;
    private EditText senha;
    private EditText email;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth;
    private Utils utils;

    //private DatabaseReference referenciaFirebase;


    //String com a lista de permissoes necessarias para o funcionamento do App
    private String[] permissoaoNecessaria = new String[]{
            android.Manifest.permission.SEND_SMS

            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //verificar se o usuario ja esta logado
        verificarUsuarioLogado();


        //Solicitar permissao ao usuario
        Permissao.validaPermissoes(LoginActivity.this, permissoaoNecessaria, 1);


        btLogin  = (Button) findViewById(R.id.btLogar_id);
        senha    = (EditText) findViewById(R.id.senhaLogin_id);
        email    = (EditText) findViewById(R.id.emailLogin_id);
        utils    = new Utils();


        //Botao cadastrar
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //capturando valores digitados
            usuario = new Usuario();
            usuario.setEmail(email.getText().toString());
            usuario.setSenha(senha.getText().toString());


            //validando campos Email e Senha
            if(utils.verificaCamposLogin(usuario.getEmail().toString(), usuario.getSenha().toString())){

                validaLogin();

            }else{

                Toast.makeText(getApplicationContext(), "Você precisa preencher todos os campos.", Toast.LENGTH_LONG).show();

            }

            }
        });
    }


    //verificar usuario logado
    public void verificarUsuarioLogado(){

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        if(firebaseAuth.getCurrentUser() != null){
            abrirMainActivity();
        }

    }


    //metodo para realizar a validacao do login do usuario
    public void validaLogin(){

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    String idUsuarioLogado = Base64Custom.codificaBase64(usuario.getEmail());
                    preferencias.salvarDados(idUsuarioLogado);

                    Toast.makeText(getApplicationContext(), "Login realizado com sucesso.", Toast.LENGTH_LONG).show();

                    abrirMainActivity();


                }else{
                    String erroExcessao = "";

                    try{

                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e){

                        erroExcessao = "O E-mail digitado não corresponde a um usuário cadastrado";

                    }catch (FirebaseAuthInvalidCredentialsException e){

                        erroExcessao = "A senha digitada não está correta.";

                    }catch (Exception e){

                        e.printStackTrace();

                    }

                    Toast.makeText(getApplicationContext(), "Ops... "+erroExcessao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //metodo para abrir a activity cadastro de usuario
    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }

    //abrir MainActivity
    public void abrirMainActivity(){

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    //metodo para tratar quando o usuario clica no botao Deny na hora da soliitacao das permissoes
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult){

        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        for(int resultado: grantResult){

            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }


    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Permissões Negadas.");
        builder.setMessage("Para utilizar esse App, é necessário aceitar as permissões.");
        builder.setCancelable(false);


        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}


