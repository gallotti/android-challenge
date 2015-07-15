package br.com.gallotti.desafioandroid.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.gallotti.desafioandroid.R;
import br.com.gallotti.desafioandroid.bean.Photo;
import br.com.gallotti.desafioandroid.util.ParseJson;
import br.com.gallotti.desafioandroid.util.Util;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

import java.io.UnsupportedEncodingException;

@EActivity(R.layout.activity_foto_detalhes)
public class FotoDetalhesActivity extends ActionBarActivity {

    @ViewById
    TextView txtAuthor, txtDesc, txtTitle, txtQtdComments, txtQtdViews;

    @ViewById
    ImageView imgUser;

    @ViewById
    ImageView img;

    @ViewById
    ListView listView;

    @ViewById
    ProgressBar progressBar;

    RequestQueue mRequestQueue;
    JsonObjectRequest req;
    AQuery aq;
    Photo photo;

    String[] listComments;
    String urlAuthor;

    @AfterViews
    public void init() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Referenciando o objeto RequestQueue
        mRequestQueue = Volley.newRequestQueue(this);

        //Referenciando o objeto Android Query
        aq = new AQuery(this);

        //resgatando o objeto Foto enviado por parametro da activity anterior
        photo = (Photo) getIntent().getSerializableExtra(getText(R.string.key_photo).toString());

        //Formata a url que retorna os dados do Autor
        urlAuthor = getText(R.string.url) + "?method=" + getText(R.string.method_info_user) + "&api_key=" + getText(R.string.api_key) + "&user_id=" + photo.getAuthor().getId() + "&format=json&nojsoncallback=1";
        txtAuthor.setText(photo.getAuthor().getName());

        //Tratando html para inserir no TextView da Descrição e Titulo
        txtDesc.setText(Html.fromHtml(photo.getDescription()));
        txtTitle.setText(Html.fromHtml(photo.getTitle()));

        //Link clicavel e direcionando para a pagina do link
        txtDesc.setMovementMethod(LinkMovementMethod.getInstance());

        //Seta a quantidade de Visualizações
        txtQtdViews.setText(getText(R.string.views).toString() + photo.getViews());

        //Seta a quantidade de Comentarios
        txtQtdComments.setText(getText(R.string.comments).toString() + 0);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //varifica o cache do autor a exibição da imagem e suas informações
        verifyCacheAuthor();
        //chama metodo para carregar os comentarios
        loadComments();
        //utiliza a lib androidQuery para realizar load, cache e progress
        aq.id(img).progress(progressBar).image(Util.formatarURLImagem(photo, 'z'), true, true, 0, 0, new BitmapAjaxCallback() {

            @Override
            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                //após o carregamento da imagem, seta Gone para sumir com o progress e colocar a imagem na posição correta
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Cria o menu com o item de compartilhar a imagem
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_share:
                //chama o metodo de compartilhar a imagem passando a sua bitmap
                Util.share(((BitmapDrawable) img.getDrawable()).getBitmap(), this);
                return super.onOptionsItemSelected(item);

            default:
                //retorna para activity anterior
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Retorna imagem do icone do Usuario e chamar metodo que corta no formato circular
     */
    @UiThread
    public void getIconUser() {
        //cria url para realizar load do icone do usuario
        String url = "http://farm" + photo.getAuthor().getIconFarm() + ".staticflickr.com/" + photo.getAuthor().getIconServer() + "/buddyicons/" + photo.getAuthor().getNsid() + ".jpg";

        aq.id(imgUser).image(url, true, true, 0, R.drawable.base_mascara_usuario, new BitmapAjaxCallback() {

            @Override
            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                //seta imagem cortada de forma circular
                imgUser.setImageBitmap(Util.getCircleBitmap(bm));

            }
        });

    }


    /**
     * Metodo responsavel por carregar as informações do Autor da imagem e chamar metodo para carregar icone do usuario
     */
    public void loadAuthor() {
        req = new JsonObjectRequest(Request.Method.GET, urlAuthor, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        author(response);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.dialogErrorNet(FotoDetalhesActivity.this);

            }
        });

        mRequestQueue.add(req);

    }

    /**
     * Metodo responsavel por carregar a lista de comentarios e setar sua quantidade.
     */
    public void loadComments() {
        String url = getText(R.string.url) + "?method=" + getText(R.string.method_list_comments) + "&api_key=" + getText(R.string.api_key) + "&photo_id=" + photo.getId() + "&format=json&nojsoncallback=1";
        req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        ParseJson parse = new ParseJson();
                        listComments = parse.criarListComentarios(response);
                        if (listComments != null) {

                            txtQtdComments.setText(getText(R.string.comments).toString() + listComments.length);
                            listView = (ListView) findViewById(R.id.listViewComments);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FotoDetalhesActivity.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1, listComments);

                            listView.setAdapter(adapter);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        req.setShouldCache(true);

        mRequestQueue.add(req);

    }

    @Background
    protected void author(JSONObject response) {
        try {
            photo.getAuthor().setNsid(response.getJSONObject("person").getString("nsid"));
            photo.getAuthor().setIconServer(response.getJSONObject("person").getString("iconserver"));
            photo.getAuthor().setIconFarm(response.getJSONObject("person").getString("iconfarm"));
            photo.getAuthor().setName(response.getJSONObject("person").getString("realname"));
            getIconUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Background
    protected void verifyCacheAuthor() {
        Cache cache = mRequestQueue.getCache();
        Cache.Entry entry = cache.get(urlAuthor);

        if (entry != null) {
            try {
                author(new JSONObject(new String(entry.data, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            loadAuthor();
        }
    }

}
