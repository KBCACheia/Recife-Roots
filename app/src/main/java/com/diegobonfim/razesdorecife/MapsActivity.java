package com.diegobonfim.razesdorecife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<ArvoresTombadas> listaArvores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        converteCVS(readFile(this, "arvores-tombadas.csv"));
        SupportMapFragment frag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        frag.getMapAsync(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(153, 204, 0)));


    }


    public static String readFile(Context context, String fileName) {
        BufferedReader in = null;
        try {

            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(fileName);
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String str;
            boolean isFirst = true;
            while ( (str = in.readLine()) != null ) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append("\n");
                buf.append(str);
            }
            return buf.toString();

        } catch (IOException e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    public ArrayList<ArvoresTombadas> converteCVS(String arquivo){
        listaArvores = new ArrayList<ArvoresTombadas>();
        for(String line : arquivo.split("\n")){
            String[] columns = line.split(";");
            ArvoresTombadas arvores = new ArvoresTombadas();
            arvores.nome_popularNome = columns[0];
            arvores.numero = Integer.parseInt(columns[1]);
            arvores.familia = columns[2];
            arvores.nome_cientifico = columns[3];
            arvores.este = columns[4];
            arvores.endereco = columns[5];
            arvores.norte = columns[6];
            arvores.decreto = columns[7];
            arvores.microregiao = columns[8];
            arvores.rpa = Integer.parseInt(columns[9]);
            arvores.identifica = Integer.parseInt(columns[10]);
            arvores.observacao = columns[11];
            arvores.latitude = Double.parseDouble(columns[12].replace(",","."));
            arvores.longitude = Double.parseDouble(columns[13].replace(",","."));
            listaArvores.add(arvores);

        }
        return listaArvores;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for(ArvoresTombadas tree : listaArvores) {
            LatLng latLng = new LatLng(tree.latitude, tree.longitude);
            latLngBoundsBuilder.include(latLng);
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(MapsActivity.this, TreeInfo.class);
                    intent.putExtra("arvore", marker.getTitle());
                    startActivity(intent);
                }
            });
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(tree.nome_popularNome)
                    .snippet("Clique para ler mais sobre esta árvore...")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).alpha(0.6f));
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngBoundsBuilder.build().getCenter(), 12));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle("Sobre");
            dialogo.setMessage("Diego Bonfim\n" +
                    "\n" +
                    "Técnico em Técnologia da Informação\n" +
                    "\n" +
                    "diego@diegobonfim.com\n" +
                    "\n" +
                    "facebook.com/Diego.JBonfim\n" +
                    "\n" +
                    "twitter.com/@Diego_88\n");
            dialogo.create();
            dialogo.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
