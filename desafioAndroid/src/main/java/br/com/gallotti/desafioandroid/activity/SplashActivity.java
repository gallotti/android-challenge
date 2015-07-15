package br.com.gallotti.desafioandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import br.com.gallotti.desafioandroid.R;
import br.com.gallotti.desafioandroid.bean.Fotos;
import br.com.gallotti.desafioandroid.util.ParseJson;
import br.com.gallotti.desafioandroid.util.Util;

public class SplashActivity extends Activity {
    Fotos fotos;
    private RequestQueue mRequestQueue;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRequestQueue = Volley.newRequestQueue(this);

        progress = (ProgressBar)findViewById(R.id.progressBar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializar();
    }

    public void inicializar() {

        String url = getText(R.string.url) + "?method=flickr.photos.getRecent&api_key=" + getText(R.string.api_key) + "&extras=description%2Cowner_name%2Cviews&per_page=10&page=1&format=json&nojsoncallback=1";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ParseJson parse = new ParseJson();
                        fotos = parse.parseFotos(response);
                        progress.setVisibility(View.GONE);
                        Intent i = new Intent(SplashActivity.this, MainActivity_.class);
                        i.putExtra(getText(R.string.key_photos).toString(), fotos);
                        startActivity(i);
                        finish();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.dialogErrorNet(SplashActivity.this);

            }
        });


        mRequestQueue.add(req);

    }


}

