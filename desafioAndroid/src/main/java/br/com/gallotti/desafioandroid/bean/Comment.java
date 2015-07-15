package br.com.gallotti.desafioandroid.bean;

import java.io.Serializable;

@SuppressWarnings("serial") 
public class Comments implements Serializable{

	private String nameAuthor;
	private String msg;
	
	public String getNomeAutor() {
		return nameAuthor;
	}
	public void setNomeAutor(String nameAuthor) {
		this.nameAuthor = nameAuthor;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
		
}
