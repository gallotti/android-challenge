package br.com.gallotti.desafioandroid.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gallotti.desafioandroid.bean.Autor;
import br.com.gallotti.desafioandroid.bean.Foto;
import br.com.gallotti.desafioandroid.bean.Fotos;

public class ParseJson {

	/**
	 * 
	 * @param response
	 * @return Fotos
	 */
	public Fotos parseFotos(JSONObject response){
		Fotos fotos = new Fotos();

		try {
			JSONObject responseFotos = response.getJSONObject("photos");
			fotos.setListFoto(criarListaFoto(responseFotos.getJSONArray("photo")));
			fotos.setPage(responseFotos.getInt("page")); 
			fotos.setPages(responseFotos.getInt("pages"));
			fotos.setPerpage(responseFotos.getInt("perpage"));
			fotos.setTotal(responseFotos.getInt("total"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fotos;
	}

	/**
	 * Metodo responsavel por criar lista de fotos
	 * @param jsonArray
	 * @return List<Foto>
	 * @throws JSONException 
	 */
	private List<Foto> criarListaFoto(JSONArray jsonArray) throws JSONException {
		ArrayList<Foto> listFotos = new ArrayList<Foto>();

		for (int i = 0; i < jsonArray.length(); i++) {
			listFotos.add(criarFoto(jsonArray.getJSONObject(i)));
		}
			
		return listFotos;
	}

	/**
	 * Metodo responsavel por criar foto
	 * @param jObject
	 * @return Foto
	 * @throws JSONException 
	 */
	private Foto criarFoto(JSONObject jObject) throws JSONException{
		Foto foto = new Foto();
		foto.setFarm(jObject.getInt("farm"));
		foto.setId(jObject.getString("id"));
		foto.setSecret(jObject.getString("secret"));
		foto.setServer(jObject.getString("server"));
		foto.setTitle(jObject.getString("title"));
		foto.setDescription(jObject.getJSONObject("description").getString("_content"));
		foto.setAutor(createAutor(jObject));
		foto.setViews(jObject.getInt("views"));
		
		return foto;
	}

	private Autor createAutor(JSONObject jObject) throws JSONException {
		Autor autor = new Autor();
		autor.setNome(jObject.getString("ownername"));
		autor.setId(jObject.getString("owner"));
		return autor;
	}
	
	public String[] criarListComentarios(JSONObject jObject){
		String[] listaComentarios = null;
		try {
		JSONArray jsonArray = jObject.getJSONArray("comment");
		listaComentarios = new String[jsonArray.length()];
		
		for (int i = 0; i < jsonArray.length(); i++) {
			String c;
				c = i + " " + jsonArray.getJSONObject(i).getString("authorname");
			listaComentarios[i] = c;
		}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return listaComentarios;
	}
	


}
