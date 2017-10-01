package com.weslleymendonca.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.adapter.ConversaAdapter;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;
import com.weslleymendonca.whatsapp.helper.Base64Custom;
import com.weslleymendonca.whatsapp.helper.Preferencias;
import com.weslleymendonca.whatsapp.model.Mensagem;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btEnviar;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagens;


    //Dados Contato
    private String nomeContato;
    private String idContato;

    //Dados remetente
    private String idUsuario; //usuario que está enviando a mensagem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btEnviar     = (ImageButton) findViewById(R.id.bt_enviar);
        listView     = (ListView) findViewById(R.id.lv_conversas);


        //Dados do usuario logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuario = preferencias.getIdUsuario();



        Bundle extra = getIntent().getExtras();

        if(extra != null){
            nomeContato = extra.getString("nome");
            idContato   = Base64Custom.codificaBase64(extra.getString("email"));

        }

        //configurar a toolbar
        toolbar = (Toolbar) findViewById(R.id.tb_conversa);

        toolbar.setTitle(nomeContato);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //configurar a listView
        mensagens = new ArrayList<>();

        //recuperando mensagens
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child(idUsuario)
                .child(idContato);

        //Criar listener para mensagens
        valueEventListenerMensagens = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        /*adapter   = new ArrayAdapter(ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                mensagens);*/
        adapter = new ConversaAdapter(ConversaActivity.this, mensagens);

        listView.setAdapter(adapter);

        firebase.addValueEventListener(valueEventListenerMensagens);



        //enviar mensagem
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoMensagem = editMensagem.getText().toString();


                if(textoMensagem.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();
                }else {

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuario);
                    mensagem.setMensagem(textoMensagem);
                    //mensagem.salvarRemetente(idContato);
                    //mensagem.salvarDestinatario(idContato);
                    salvarMensagem(idUsuario, idContato, mensagem);
                    salvarMensagem(idContato, idUsuario, mensagem);
                    editMensagem.setText("");
                }




            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagens);
    }

    private boolean salvarMensagem(String idUsuario, String idContato, Mensagem mensagem){
        try{

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idUsuario)
                    .child(idContato)
                    .push() //cria um identificador unico no banco do firebase
                    .setValue(mensagem);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
