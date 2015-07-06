package br.com.gallotti.desafioandroid;

import org.json.JSONObject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import br.com.gallotti.desafioandroid.bean.Foto;
import br.com.gallotti.desafioandroid.bean.Fotos;
import br.com.gallotti.desafioandroid.util.ParseJson;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

	@ViewById
	protected ListView listView;
	@ViewById
	protected ScrollView scroll;
	private RequestQueue  mRequestQueue;
	private ItemFotoAdapter adapter;
	private Fotos fotos;
	int count;

	@AfterViews
	public void inicializar(){
		mRequestQueue = Volley.newRequestQueue(this);
		getListFotosRecentes();

	}

	/**
	 * Metodo responsavel por criar a lista com as Imagens e Nome do Autor da Foto.
	 */

	private void createListView(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Foto foto = (Foto)view.getTag();
				Intent i = new Intent(MainActivity.this, FotoDetalhesActivity_	.class);
				i.putExtra("foto", foto);
				startActivity(i);
			}
		});
		adapter = new ItemFotoAdapter(MainActivity.this, android.R.layout.simple_list_item_1, fotos.getListFoto());
		
		listView.setAdapter(adapter);
	}

	/**
	 * Metodo responsavel por consumir o Web Service da lista recentes de fotos
	 * Primeiro chama 10 fotos, depois x*10 
	 */
	@Click({R.id.btnMais})
	public  void getListFotosRecentes(){
		count++;
		String url = getText(R.string.url)+"?method=flickr.photos.getRecent&api_key="+getText(R.string.api_key)+"&extras=description%2Cowner_name%2Cviews&per_page="+count*10+"&page=1&format=json&nojsoncallback=1";

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				ParseJson parse = new ParseJson();
				fotos = parse.parseFotos(response);

				createListView();
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Desafio", "Error: " + error.getMessage());

			}
		});


		mRequestQueue.add(req);

	}

}
