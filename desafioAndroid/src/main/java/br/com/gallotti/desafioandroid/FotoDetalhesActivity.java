package br.com.gallotti.desafioandroid;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.gallotti.desafioandroid.bean.Foto;
import br.com.gallotti.desafioandroid.util.ParseJson;
import br.com.gallotti.desafioandroid.util.Util;

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
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_foto_detalhes)
public class FotoDetalhesActivity extends ActionBarActivity{

	@ViewById
	TextView txtAutor, txtDesc,txtTitulo,txtQtdComentarios,txtQtdVisualizacoes;
	
	@ViewById
	ImageView imgUsuario,img;
	
	@ViewById
	ListView listView;

	Bitmap bitMap;
	AQuery aq;
	Foto foto;
	
	String[] listaComentarios;

	
	@AfterViews
	public void inicializar(){

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent i = getIntent();
		foto = (Foto)i.getSerializableExtra("foto");

		txtAutor.setText(foto.getAutor().getNome());
		
		//Tratando html para inserir no TextView da Descrição e Titulo
		txtDesc.setText(Html.fromHtml(foto.getDescription()));
		txtTitulo.setText(Html.fromHtml(foto.getTitle()));
		
		txtQtdVisualizacoes.setText(getText(R.string.visualizacoes).toString() + foto.getViews());
		txtQtdComentarios.setText(getText(R.string.comentarios).toString() + 0);
		
		
		carregarAutor();
		carregarComentarios();
		
		 aq = new AQuery(this);    
		
		aq.id(img).image(Util.formatarURLImagem(foto, 'z'), true, true, 0, 0, new BitmapAjaxCallback(){

            @Override
            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){
            	
                bitMap = bm;
                img.setImageBitmap(bitMap);
            }

    });
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
    			compartilhar();
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
		 		
		aq.id(R.id.imgUsuario).progress(this).image(url, true, true, 0, R.drawable.base_mascara_usuario, new BitmapAjaxCallback(){

            @Override
            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){
            	
                imgUsuario.setImageBitmap(Util.getCircularBitmap(bm));

            }
		});
		
	}


	/**
	 * Metodo responsavel por carregar as informações do Autor da imagem e chamar metodo para carregar icone do usuario
	 */
	public  void carregarAutor(){
		final String url = getText(R.string.url)+"?method=flickr.people.getInfo&api_key="+getText(R.string.api_key)+"&user_id="+foto.getAutor().getId()+"&format=json&nojsoncallback=1";
		RequestQueue  mRequestQueue = Volley.newRequestQueue(this); 
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					foto.getAutor().setNsid(response.getJSONObject("person").getString("nsid"));
					foto.getAutor().setIconServer(response.getJSONObject("person").getString("iconserver"));
					foto.getAutor().setIconFarm(response.getJSONObject("person").getString("iconfarm"));
					getIconUsuario();
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Desafio", "Error: " + error.getMessage());

			}
		});


		mRequestQueue.add(req);

	}
	
	/**
	 * Metodo responsavel por carregar a lista de comentarios e setar sua quantidade.
	 */
	public  void carregarComentarios(){
		String url = getText(R.string.url)+"?method=flickr.photos.comments.getList&api_key="+getText(R.string.api_key)+"&photo_id="+foto.getId()+"&format=json&nojsoncallback=1";
		RequestQueue  mRequestQueue = Volley.newRequestQueue(this); 
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
			
					ParseJson parse = new ParseJson();
					listaComentarios = parse.criarListComentarios(response);
					if (listaComentarios != null) {
						
						txtQtdComentarios.setText(getText(R.string.comentarios).toString() + listaComentarios.length);
						listView = (ListView)findViewById(R.id.listViewComentarios);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(FotoDetalhesActivity.this,
								android.R.layout.simple_list_item_1, android.R.id.text1,listaComentarios);
						
						listView.setAdapter(adapter);
					}
					
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Desafio", "Error: " + error.getMessage());
			}
		});


		mRequestQueue.add(req);

	}
	
	
	/*
	 * Metodo responsavel por compartilhar a imagem com as redes sociais
	 */
	private void compartilhar() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		Uri phototUri = Util.getImageUri(bitMap,this);
		shareIntent.setData(phototUri);
		shareIntent.setType("image/png");
		shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
		startActivity(Intent.createChooser(shareIntent, "Use this for sharing"));
	}
	
	
	
}
