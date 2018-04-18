package com.example.rse_vzla_07.practica_memoria_externa;

import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by RSE_VZLA_07 on 17/4/2018.
 */

public class ExternalActivity extends AppCompatActivity {

    private boolean DisponibleSD;
    private boolean AccesoEscrituraSD;

    private Button btnLeer;
    private Button btnEscribir;

    private EditText editText;

    private FileOutputStream flujo = null;
    private OutputStreamWriter escritor = null;

    private InputStreamReader flujoLeer = null;
    private BufferedReader lector = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.external_activity);

        btnLeer = findViewById(R.id.btnLeer);
        btnEscribir = findViewById(R.id.btnEscribir);
        editText = findViewById(R.id.textV);

        checkSD();
        checkBtn();
    }

    private void checkBtn() {

        btnEscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DisponibleSD && AccesoEscrituraSD) {
                    escribirSD();
                }
            }
        });

        btnLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DisponibleSD) {
                    leerSD();
                }
            }
        });
    }

    private void escribirSD() {

        try {
            File ruta = Environment.getExternalStorageDirectory();
            File fichero = new File(ruta.getAbsolutePath(), "ficheroPrueba.txt");

            flujo = new FileOutputStream(fichero);
            escritor = new OutputStreamWriter(flujo);
            escritor.write(editText.getText().toString());
        } catch (Exception e) {
            Log.e("tag", "Error al escribir");
        } finally {

            try {
                if (escritor != null) {
                    escritor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void leerSD() {

        try {
            File ruta = Environment.getExternalStorageDirectory();
            File fichero = new File(ruta.getAbsolutePath(), "ficheroPrueba.txt");
            flujoLeer = new InputStreamReader(new FileInputStream(fichero));
            lector = new BufferedReader(flujoLeer);
            String texto = lector.readLine();
            while (texto != null) {
                editText.setText(texto);
                texto = lector.readLine();
            }
        } catch (Exception ex) {
            Log.e("tag", "Error al leer fichero desde tarjeta SD");
        } finally {
            try {
                if (lector != null)
                    lector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkSD() {

        String estado = Environment.getExternalStorageState();

        switch (estado) {
            case Environment.MEDIA_MOUNTED:
                DisponibleSD = true;
                AccesoEscrituraSD = true;
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                DisponibleSD = true;
                AccesoEscrituraSD = false;
                break;
            default:
                DisponibleSD = false;
                AccesoEscrituraSD = false;
                break;
        }
    }
}
