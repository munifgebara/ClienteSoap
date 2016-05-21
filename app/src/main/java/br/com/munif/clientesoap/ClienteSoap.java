package br.com.munif.clientesoap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class ClienteSoap extends AppCompatActivity implements View.OnClickListener{

    private EditText et;
    private TextView tv;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_soap);
        et=(EditText)findViewById(R.id.editText);
        tv=(TextView)findViewById(R.id.textView);
        ((Button)findViewById(R.id.button)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente_soap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        consultaCep(et.getText().toString());
    }

    private void consultaCep(String s) {
        new ConsultaCep().execute(s);
    }

    class ConsultaCep extends AsyncTask<String,String,String>{

        private static final String METHOD_NAME = "consultaCEP";
        private static final String NAMESPACE = "http://cliente.bean.master.sigep.bsb.correios.com.br/";
        private static final String URL = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente";
        public static final String CEP = "cep";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ClienteSoap.this, getResources()
                    .getString(R.string.app_name), "Buscando cep nos correios...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            SoapObject cliente = new SoapObject(NAMESPACE, METHOD_NAME);
            cliente.addProperty(CEP, params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = cliente;
            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
            String result = "";
            try {
                httpTransportSE.call("", envelope);
                SoapObject response = (SoapObject) envelope
                        .getResponse();
                result = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = "ERROR";
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            tv.setText(s);
        }
    }

}
