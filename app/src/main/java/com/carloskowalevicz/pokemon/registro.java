package com.carloskowalevicz.pokemon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class registro extends AppCompatActivity {

    EditText Nombre, Elemento, Tipo, username, password, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ip = (EditText) findViewById(R.id.ip);
        Nombre = (EditText)findViewById(R.id.pnombre);
        Elemento = (EditText)findViewById(R.id.pelemento);
        Tipo = (EditText)findViewById(R.id.ptipo);
        username = (EditText)findViewById(R.id.pusername);
        password = (EditText)findViewById(R.id.ppassword);

            }
    public void onreg(View view){
        String sNombre = Nombre.getText().toString();
        String sElemento = Elemento.getText().toString();
        String sTipo = Tipo.getText().toString();
        String susername = username.getText().toString();
        String spassword = password.getText().toString();
        String tipo = "registro";
        String ips = ip.getText().toString();
        
        ServerManager serverManager = new ServerManager(this);
        serverManager.execute(tipo, sNombre, sElemento, sTipo, susername, spassword,ips);
    }
    }


