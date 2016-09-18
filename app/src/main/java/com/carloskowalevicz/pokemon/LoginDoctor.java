package com.carloskowalevicz.pokemon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class LoginDoctor extends Activity {

    private static final String TAG = "stickynotes";
    private boolean mResumed = false;
    NfcAdapter mNfcAdapter;
    EditText tupass,nombreDoc,passDoc,idUsuario;
    TextView text1,text2;
    String tipo,id,sNombreDoc,sPassDoc;

    PendingIntent mNfcPendingIntent;
    IntentFilter[] mNdefExchangeFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        setContentView(R.layout.activity_login_doctor);


        nombreDoc = ((EditText) findViewById(R.id.userDoc));
        passDoc = ((EditText) findViewById(R.id.passDoc));
        idUsuario = ((EditText) findViewById(R.id.idUsuario));
        text1 = ((TextView) findViewById(R.id.textView9));
        text2 = ((TextView) findViewById(R.id.textView11));


        tupass = ((EditText) findViewById(R.id.pass));
        tupass.addTextChangedListener(mTextWatcher);
        nombreDoc.setText(ServerManager.userDoctor);
        passDoc.setText(ServerManager.passDoctor);

        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (MalformedMimeTypeException e) { }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };




    }
    public void onDataFill(View View) {
        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);
        tipo = "codigDoc";
        id = idUsuario.getText().toString();
        tupass.setText(null);

        sNombreDoc = nombreDoc.getText().toString();
        sPassDoc = passDoc.getText().toString();

        ServerManager serverManager3 = new ServerManager(this);

        serverManager3.execute(tipo,id,sNombreDoc,sPassDoc);
        while(serverManager3.docIdResult == null){

        }


        tupass.setText(serverManager3.docIdResult.replace("null", ""));

        if (tupass.getText().toString() != null){
            String codigo = tupass.getText().toString();
            if (codigo != null)
            {
           // int value1 = Integer.parseInt(codigo.replace("null", ""));


                while (tupass == null)
                {

                }
                String [] arrayCodigo = codigo.replace("null", "").split("");



                if (arrayCodigo[1].equals("1")){ //si esto se cumple quiere decir que el valor es 0 por tanto no se le otorga visibilidad
              text1.setVisibility(View.VISIBLE);
             }
             if (arrayCodigo[3] == "0") {
                 text2.setVisibility(View.VISIBLE);
             }

                 tipo = "getData";
                 String ids = idUsuario.getText().toString();



                 ServerManager serverManager = new ServerManager(this);

                 serverManager.execute(tipo, ids);

                 while (serverManager.var == null) {


                 }

                 String variables = serverManager.var;
                 variables = variables.replace("[", "");
                 variables = variables.replace("]", "");
                 variables = variables.replace("\"", "");
                 variables = variables.trim();
                 String[] variablesArray = variables.split(",");

                 text1.setText(variablesArray[0]);
                 text2.setText(variablesArray[1]);

             }
             }

 }


    public void onStore (View View) {



        ServerManager serverManager = new ServerManager(this);

        tipo = "guardarCodigo";

        String id = idUsuario.getText().toString();
        String clave= tupass.getText().toString();

        serverManager.execute(tipo, sNombreDoc,sPassDoc,id,clave);



    }
    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
            setIntent(new Intent()); // Consume this intent.
        }
        enableNdefExchangeMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
        mNfcAdapter.disableForegroundNdefPush(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
        }
    }
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            if (mResumed) {
                mNfcAdapter.enableForegroundNdefPush(LoginDoctor.this, getPassAsNdef());
            }
        }
    };

    private void promptForContent(final NdefMessage msg) {
        new AlertDialog.Builder(this).setTitle("Replace current content?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String body = new String(msg.getRecords()[0].getPayload());
                        setNoteBody(body);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).show();
    }


    private void setNoteBody(String body) {
        Editable text = tupass.getText();
        text.clear();
        text.append(body);
    }



    private NdefMessage getPassAsNdef() {
        byte[] textBytes =tupass.getText().toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }


    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        } else {
            Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundNdefPush(LoginDoctor.this, getPassAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
    }








}