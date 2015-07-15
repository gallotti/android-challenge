package br.com.gallotti.desafioandroid.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.gallotti.desafioandroid.R;
import br.com.gallotti.desafioandroid.bean.Foto;
import br.com.gallotti.desafioandroid.bean.Fotos;
import br.com.gallotti.desafioandroid.util.ParseJson;
import br.com.gallotti.desafioandroid.util.SimpleLog;
import br.com.gallotti.desafioandroid.util.Util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

	@ViewById
	protected ListView listView;

	private RequestQueue  mRequestQueue;
	private ItemFotoAdapter adapter;
	private Fotos fotos;


	@AfterViews
	public void inicializar(){
		mRequestQueue = Volley.newRequestQueue(this);
		getSupportActionBar().setElevation(0);
		Intent i = getIntent();
		fotos = (Fotos)i.getSerializableExtra(getText(R.string.key_photos).toString());


			if (fotos!=null){
				createListView();
			} else {
				Util.dialogErrorNet(MainActivity.this);
			}
	}

	/**
	 * Metodo responsavel por criar a lista com as Imagens e Nome do Autor da Foto.
	 */

	private void createListView(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Foto foto = ((ItemFotoAdapter.ViewHolderItem)view.getTag()).foto;
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
	private void getListFotosRecentes(){

		String url = getText(R.string.url)+"?method="+getText(R.string.method_list_recent)+"&api_key="+getText(R.string.api_key)+"&extras=description%2Cowner_name%2Cviews&per_page=10&page=1&format=json&nojsoncallback=1";

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
				new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				ParseJson parse = new ParseJson();
				fotos.getListFoto().addAll(parse.parseFotos(response).getListFoto());
				adapter.notifyDataSetChanged();

			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				SimpleLog.write("Error: " + error.getMessage());
				Util.dialogErrorNet(MainActivity.this);

			}
		});


		mRequestQueue.add(req);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_list, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


		switch(item.getItemId()) {

			case R.id.action_atualizar:
				getListFotosRecentes();
				return super.onOptionsItemSelected(item);

			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
