package br.com.gallotti.desafioandroid;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.gallotti.desafioandroid.bean.Foto;
import br.com.gallotti.desafioandroid.util.ParseJson;
import br.com.gallotti.desafioandroid.util.Util;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;

@EActivity(R.layout.activity_foto_detalhes)
public class FotoDetalhesActivity extends ActionBarActivity{

	@ViewById
	TextView txtAutor, txtDesc,txtTitulo,txtQtdComentarios,txtQtdVisualizacoes;
	
	@ViewById
	ImageView imgUsuario,img;
	
	@ViewById
	ListView listView;

	@ViewById
	ProgressBar progressBar;

	RequestQueue  mRequestQueue;
	JsonObjectRequest req;
	Bitmap bitMap;
	AQuery aq;
	Foto foto;

	String[] listaComentarios;
	String urlAutor;
	
	@AfterViews
	public void inicializar(){

		getSupportActionBar().setElevation(0);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mRequestQueue = Volley.newRequestQueue(this);

		Intent i = getIntent();
		foto = (Foto)i.getSerializableExtra(getText(R.string.key_photo).toString());

		urlAutor = getText(R.string.url)+"?method="+getText(R.string.method_info_user)+"&api_key="+getText(R.string.api_key)+"&user_id="+foto.getAutor().getId()+"&format=json&nojsoncallback=1";
		txtAutor.setText(foto.getAutor().getNome());

		//Tratando html para inserir no TextView da Descrição e Titulo
		txtDesc.setText(Html.fromHtml(foto.getDescription()));
		txtTitulo.setText(Html.fromHtml(foto.getTitle()));

		//Link clicavel e direcionando para a pagina do link
		txtDesc.setMovementMethod(LinkMovementMethod.getInstance());

		
		txtQtdVisualizacoes.setText(getText(R.string.views).toString() + foto.getViews());
		txtQtdComentarios.setText(getText(R.string.comments).toString() + 0);

		aq = new AQuery(this);


	}

	@Override
	protected void onResume() {
		super.onResume();
		verificaCacheAutor();
		carregarComentarios();
		aq.id(img).image(Util.formatarURLImagem(foto, 'z'), true, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()) {
            
    	case R.id.action_share:
    			Util.compartilhar(((BitmapDrawable)img.getDrawable()).getBitmap(),this);
    			return super.onOptionsItemSelected(item);
    	
        default:
        	onBackPressed();
            return super.onOptionsItemSelected(item);
        }
	}

	/**
	 * Retorna imagem do icone do Usuario e chamr metodo que corta no formato circular 
	 */
	public  void getIconUsuario(){

		String url = "http://farm"+foto.getAutor().getIconFarm()+".staticflickr.com/"+foto.getAutor().getIconServer()+"/buddyicons/"+foto.getAutor().getNsid()+".jpg";
		 		
		aq.id(imgUsuario).image(url, true, true, 0,R.drawable.base_mascara_usuario, new BitmapAjaxCallback() {

			@Override
			public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {

				imgUsuario.setImageBitmap(Util.getCircularBitmap(bm));

			}
		});

	}


	/**
	 * Metodo responsavel por carregar as informações do Autor da imagem e chamar metodo para carregar icone do usuario
	 */
	public  void carregarAutor(){
		req = new JsonObjectRequest(Request.Method.GET,urlAutor,null,
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				autor(response);

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
	public  void carregarComentarios(){
		String url = getText(R.string.url)+"?method="+getText(R.string.method_list_comments)+"&api_key="+getText(R.string.api_key)+"&photo_id="+foto.getId()+"&format=json&nojsoncallback=1";
		req = new JsonObjectRequest(Request.Method.GET,url,null,
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
			
					ParseJson parse = new ParseJson();
					listaComentarios = parse.criarListComentarios(response);
					if (listaComentarios != null) {
						
						txtQtdComentarios.setText(getText(R.string.comments).toString() + listaComentarios.length);
						listView = (ListView)findViewById(R.id.listViewComentarios);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(FotoDetalhesActivity.this,
								android.R.layout.simple_list_item_1, android.R.id.text1,listaComentarios);
						
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
	private void autor(JSONObject response){
		try {
			foto.getAutor().setNsid(response.getJSONObject("person").getString("nsid"));
			foto.getAutor().setIconServer(response.getJSONObject("person").getString("iconserver"));
			foto.getAutor().setIconFarm(response.getJSONObject("person").getString("iconfarm"));
			foto.getAutor().setNome(response.getJSONObject("person").getString("realname"));
			getIconUsuario();
		} catch (JSONException e) {
				e.printStackTrace();
			}
	}

	@Background
	private void verificaCacheAutor(){
		Cache cache = mRequestQueue.getCache();
		Cache.Entry entry = cache.get(urlAutor);

		if(entry != null){
			try {
				autor(new JSONObject(new String(entry.data, "UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else{
			carregarAutor();
		}
	}
	
}
