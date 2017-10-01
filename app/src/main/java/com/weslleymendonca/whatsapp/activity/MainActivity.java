package com.weslleymendonca.whatsapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.adapter.TabAdapter;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;
import com.weslleymendonca.whatsapp.helper.Base64Custom;
import com.weslleymendonca.whatsapp.helper.Preferencias;
import com.weslleymendonca.whatsapp.helper.SlidingTabLayout;
import com.weslleymendonca.whatsapp.model.Contato;
import com.weslleymendonca.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebase;
    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private String idContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        //Configurando a ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);


        //Criando SlidingTab
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tab);
        viewPager        = (ViewPager) findViewById(R.id.vp_pagina);

        //consfigurando o SlidingTab
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

        //configurando Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        //setando as abas
        slidingTabLayout.setViewPager(viewPager);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }


    //metodo para capturar a acao do clique nos itens do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_configuracao:
                return true;
            case R.id.item_adicionar:
                cadastrarContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void cadastrarContato(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        final EditText editText = new EditText(MainActivity.this);

        dialog.setCancelable(false);
        dialog.setTitle("Novo Contato");
        dialog.setMessage("Digite o Email para adicioanar o usuário");
        //Adicionar EditText dentro do Alert Dialog
        dialog.setView(editText);

        dialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(MainActivity.this, "CANCELAR", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setPositiveButton("CADASTRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String emailContato = editText.getText().toString();

                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Digite um Email.", Toast.LENGTH_SHORT).show();
                }else{
                    /*verificar se o usuário está cadastrado no FireBase*/
                    idContato = Base64Custom.codificaBase64(emailContato);

                    // Recuperar referencia do firebase
                    firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(idContato);


                    //verifica se a referencia acima existe valor
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                //recuoperando dados do contato
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String usuarioLogado = preferencias.getIdUsuario();

                                Contato contato = new Contato();
                                contato.setIdContato(idContato);
                                contato.setEmailContato(usuarioContato.getEmail());
                                contato.setNomeContato(usuarioContato.getNome());

                                //Gravando os dados do contato no banco Firebase
                                firebase = ConfiguracaoFirebase.getFirebase();
                                firebase.child("contatos")
                                        .child(usuarioLogado)
                                        .child(idContato).setValue(contato);


                            }else {
                                Toast.makeText(MainActivity.this, "Usuário não cadastrado.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }
        });
        dialog.create();
        dialog.show();


    }

    public void deslogarUsuario(){
        firebaseAuth.signOut();
        Preferencias preferencias = new Preferencias(getApplicationContext());
        preferencias.apagarDados();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirTelaLogin(){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
