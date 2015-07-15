package br.com.gallotti.desafioandroid;

import java.util.Collection;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.gallotti.desafioandroid.bean.Foto;
import br.com.gallotti.desafioandroid.util.SimpleLog;
import br.com.gallotti.desafioandroid.util.Util;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

public class ItemFotoAdapter extends ArrayAdapter<Foto> {

    private List<Foto> listFoto;
    public Context context;

    public ItemFotoAdapter(Context context, int textViewResourceId,
			List<Foto> objects) {

		super(context,  textViewResourceId, objects);
		 this.context = context;
		 this.listFoto = objects;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolderItem viewHolder;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_foto, parent, false);
			viewHolder = new ViewHolderItem();

			viewHolder.textViewAutor = (TextView) convertView.findViewById(R.id.txtAutorItem);


			viewHolder.textViewTitulo = (TextView) convertView.findViewById(R.id.txtTituloItem);


			viewHolder.progressView = (ProgressBar)convertView.findViewById(R.id.progress);
			viewHolder.imgView = (ImageView)convertView.findViewById(R.id.imgItem);

			//Lib Android - Query


			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}

		viewHolder.textViewAutor.setText(this.listFoto.get(position).getAutor().getNome());
		viewHolder.textViewTitulo.setText(this.listFoto.get(position).getTitle());
		AQuery aq = new AQuery(convertView);
		aq.id(viewHolder.imgView).progress(viewHolder.progressView).image(Util.formatarURLImagem(this.listFoto.get(position), 'm'), true, true, 0, R.drawable.base_mascara_usuario, new BitmapAjaxCallback() {

			@Override
			public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
				viewHolder.progressView.setVisibility(View.GONE);
				viewHolder.imgView.setImageBitmap(Util.getCircularBitmap(bm));

			}
		});

		viewHolder.foto = listFoto.get(position);
		return convertView;
	}



	static class ViewHolderItem{
		TextView textViewAutor;
		TextView textViewTitulo;
		ImageView imgView;
		ProgressBar progressView;
		Foto foto;

	}

 }