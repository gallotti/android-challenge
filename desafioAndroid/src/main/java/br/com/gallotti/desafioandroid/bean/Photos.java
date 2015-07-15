package br.com.gallotti.desafioandroid.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Photos implements Serializable{

    private int page;
    private int pages;
    private int perpage;
    private int total;
    private List<Foto> listFoto;

    public Photos(){

    }

    public Photos(int page, int pages, int perpage, int total, List<Foto> listFoto) {
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;
        this.listFoto = listFoto;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Foto> getListFoto() {
        return listFoto;
    }

    public void setListFoto(List<Foto> listFoto) {
        this.listFoto = listFoto;
    }
}
