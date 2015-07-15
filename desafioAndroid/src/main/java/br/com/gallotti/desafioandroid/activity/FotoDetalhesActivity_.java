//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package br.com.gallotti.desafioandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.gallotti.desafioandroid.R.id;
import br.com.gallotti.desafioandroid.R.layout;
import com.googlecode.androidannotations.api.SdkVersionHelper;

public final class FotoDetalhesActivity_
    extends FotoDetalhesActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_foto_detalhes);
    }

    private void init_(Bundle savedInstanceState) {
    }

    private void afterSetContentView_() {
        txtDesc = ((TextView) findViewById(id.txtDesc));
        txtTitulo = ((TextView) findViewById(id.txtTitulo));
        txtAutor = ((TextView) findViewById(id.txtAutor));
        listView = ((ListView) findViewById(id.listView));
        imgUsuario = ((ImageView) findViewById(id.imgUsuario));
        img = ((ImageView) findViewById(id.img));
        txtQtdComentarios = ((TextView) findViewById(id.txtQtdComentarios));
        txtQtdVisualizacoes = ((TextView) findViewById(id.txtQtdVisualizacoes));
        inicializar();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static FotoDetalhesActivity_.IntentBuilder_ intent(Context context) {
        return new FotoDetalhesActivity_.IntentBuilder_(context);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, FotoDetalhesActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public FotoDetalhesActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (context_ instanceof Activity) {
                ((Activity) context_).startActivityForResult(intent_, requestCode);
            } else {
                context_.startActivity(intent_);
            }
        }

    }

}