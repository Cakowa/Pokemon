package com.carloskowalevicz.pokemon;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;

import static com.carloskowalevicz.pokemon.ServerManager.*;

public class MainActivity extends AppCompatActivity {

    EditText user, pass, ip;
    public CheckBox pa,doc;
    AlertDialog alertDialog;
    public Intent voy;
    //alertDialog = new AlertDialog.Builder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pa =(CheckBox)findViewById(R.id.checkpa);
        doc =(CheckBox)findViewById(R.id.checkdoc);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        ip = (EditText) findViewById(R.id.ip);



    }


   public void OnLog(View view) {

        String username = user.getText().toString();
        String password = pass.getText().toString();
        String ips = ip.getText().toString();
        String tipo = " ";

       if (pa.isChecked() == true) {
           tipo = "login";
       }
       if (doc.isChecked()== true){
          tipo = "logindoc";
       }
        ServerManager serverManager = new ServerManager(this);

       Log.i(CLASS_TAG, "no lo logro");
       serverManager.execute(tipo, username, password, ips);

        while (serverManager.getConectionStatus() == null){

          //  alertDialog.setMessage();

           // alertDialog.show();
        }
        String er = serverManager.getConectionStatus();
       ServerManager serverManager1 = new ServerManager(this);
       tipo = "userId";
        serverManager1.execute(tipo, username, password);
        tipo = "";
       Log.i(CLASS_TAG,"antes de if "+ serverManager.er + " esto es lo que tiene el er");
       Log.i(CLASS_TAG,"antes ("+ er + " )esto es lo que tiene el er");
        if (er.equals("conecto") ){
            Log.i(CLASS_TAG, "sip lo logro");
            voy=new Intent(MainActivity.this,LoginPaciente.class);
            startActivity(voy);
            er="";

       }
       if (er.equals("conecto doc") ){
           Log.i(CLASS_TAG, "sip lo logro Doc");
           voy=new Intent(MainActivity.this,LoginDoctor.class);
           startActivity(voy);
           er="";

       }
    }


    public void irreg(View view){

        if (pa.isChecked() == true) {
            startActivity(new Intent(this, registro.class));
        }
        if (doc.isChecked()== true){
            startActivity(new Intent(this, RegistroDoctor.class));
        }


    }
}