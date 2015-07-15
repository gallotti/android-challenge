package br.com.gallotti.desafioandroid.bean;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial") 
public class Photo implements Serializable{

    private String id;
    private String owner;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private boolean ispublic;
    private boolean isfriend;
    private boolean isfamily;
    private String description;
    private Autor autor;
    private List<Comentario> listComentarios;
    private String url;
    private int views;
    
    
    public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public List<Comentario> getListComentarios() {
		return listComentarios;
	}

	public void setListComentarios(List<Comentario> listComentarios) {
		this.listComentarios = listComentarios;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIspublic() {
        return ispublic;
    }

    public void setIspublic(boolean ispublic) {
        this.ispublic = ispublic;
    }

    public boolean getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(boolean isfriend) {
        this.isfriend = isfriend;
    }

    public boolean getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(boolean isfamily) {
        this.isfamily = isfamily;
    }

    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}
    
    

}
