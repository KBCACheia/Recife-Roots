package com.diegobonfim.razesdorecife;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.app.ToolbarActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class TreeInfo extends AppCompatActivity {

    Map<String, String> arvoresDetalhes = new HashMap<>();
    WebView web;
    ShareActionProvider mShareActionProvider;
    String tree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(153, 204, 0)));

        web = (WebView) findViewById(R.id.webView);
        String jSon = readFile(this, "arvores.json");
        tree = getIntent().getStringExtra("arvore");
        setTitle(tree);
        try {
            JSONArray jsonArray = new JSONArray(jSon);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                arvoresDetalhes.put(jsonObject.getString("title"), jsonObject.getString("description"));
            }
        } catch (Exception e) {

        }
        ;

        web.loadDataWithBaseURL("file:///android_asset/", arvoresDetalhes.get(tree), null, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tree_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.menu_share){
            //Toast.makeText(this, "lol", Toast.LENGTH_LONG).show();
            compartilha();
            return true;
        }
       /* if (id == R.id.action_camera){


        }*/

        if (id == R.id.action_quiz){
            Intent intent = new Intent(TreeInfo.this, Quiz.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

    public void compartilha(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Estou visitando a árvore " + tree + " com ajuda do Raízes do Recife" + " #link#");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
