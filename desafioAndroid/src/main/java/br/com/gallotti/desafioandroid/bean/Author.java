package br.com.gallotti.desafioandroid.bean;

import java.io.Serializable;

@SuppressWarnings("serial") 
public class Autor implements Serializable{
	
	private String id;
	private String nome;
	private String iconServer;
	private String iconFarm;
	private String nsid;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIconServer() {
		return iconServer;
	}
	public void setIconServer(String iconServer) {
		this.iconServer = iconServer;
	}
	public String getIconFarm() {
		return iconFarm;
	}
	public void setIconFarm(String iconFarm) {
		this.iconFarm = iconFarm;
	}
	public String getNsid() {
		return nsid;
	}
	public void setNsid(String nsid) {
		this.nsid = nsid;
	}
	
}
