package br.com.gallotti.desafioandroid.bean;

import java.io.Serializable;

@SuppressWarnings("serial") 
public class Comentario implements Serializable{

	String nomeAutor;
	String msg;
	
	public String getNomeAutor() {
		return nomeAutor;
	}
	public void setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
		
}
