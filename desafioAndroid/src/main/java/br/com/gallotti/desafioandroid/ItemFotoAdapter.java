package br.com.gallotti.desafioandroid;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.gallotti.desafioandroid.bean.Foto;
import br.com.gallotti.desafioandroid.util.Util;
import com.androidquery.AQuery;

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
    	
    		LayoutInflater inflater = (LayoutInflater) context
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.item_foto, parent, false);
			ProgressBar progress = (ProgressBar)rowView.findViewById(R.id.progress);
    	    //Lib Android - Query
    	    AQuery aq = new AQuery(rowView);    
    	    aq.id(R.id.imgItem).progress(progress).image(Util.formatarURLImagem(this.listFoto.get(position), 'm'), true, true);
    	    
    	    TextView txtAutor = (TextView) rowView.findViewById(R.id.txtAutor);
    	    txtAutor.setText(this.listFoto.get(position).getAutor().getNome());
    	    
    	    rowView.setTag(listFoto.get(position));
    	    
    	return rowView;
    }

 }