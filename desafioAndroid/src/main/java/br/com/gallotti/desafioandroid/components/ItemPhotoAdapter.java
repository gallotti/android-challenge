package br.com.gallotti.desafioandroid.components;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.gallotti.desafioandroid.R;
import br.com.gallotti.desafioandroid.bean.Photo;
import br.com.gallotti.desafioandroid.util.Util;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

public class ItemFotoAdapter extends ArrayAdapter<Photo> {

    private List<Photo> listPhoto;
    public Context context;

    public ItemFotoAdapter(Context context, int textViewResourceId,
                           List<Photo> objects) {

        super(context, textViewResourceId, objects);
        this.context = context;
        this.listPhoto = objects;
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

            viewHolder.textViewAuthor = (TextView) convertView.findViewById(R.id.txtAuthorItem);


            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.txtTitleItem);


            viewHolder.progressView = (ProgressBar) convertView.findViewById(R.id.progress);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.imgItem);

            //Lib Android - Query


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        viewHolder.textViewAuthor.setText(this.listPhoto.get(position).getAuthor().getName());
        viewHolder.textViewTitle.setText(this.listPhoto.get(position).getTitle());
        AQuery aq = new AQuery(convertView);
        aq.id(viewHolder.imgView).progress(viewHolder.progressView).image(Util.formatarURLImagem(this.listPhoto.get(position), 'm'), true, true, 0, R.drawable.base_mascara_usuario, new BitmapAjaxCallback() {

            @Override
            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                viewHolder.progressView.setVisibility(View.GONE);
                viewHolder.imgView.setImageBitmap(Util.getCircleBitmap(bm));

            }
        });

        viewHolder.photo = listPhoto.get(position);
        return convertView;
    }


    static class ViewHolderItem {
        TextView textViewAuthor;
        TextView textViewTitle;
        ImageView imgView;
        ProgressBar progressView;
        Photo photo;

    }

}