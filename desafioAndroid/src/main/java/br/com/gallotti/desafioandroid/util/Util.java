package br.com.gallotti.desafioandroid.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.util.Log;

import br.com.gallotti.desafioandroid.R;
import br.com.gallotti.desafioandroid.bean.Foto;

public class Util {

	/**
	 *
	 * Retorna a url formatada da imagem de acordo a API
	 *
	 * @param foto
	 * @param size
	 * @return
	 */
	public static String formatarURLImagem(Foto foto, char size){
		return "https://farm"+foto.getFarm()+".staticflickr.com/"+foto.getServer()+"/"+foto.getId()+"_"+foto.getSecret()+"_"+size+".jpg";
	}


	/**
	 *
	 * Exibe o AlertDialog com o botão de Ok.
	 *
	 * @param context
	 * @param msg
	 */
	public static  void dialog(Context context, String msg){
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
		dlgAlert.setMessage(msg);
		dlgAlert.setTitle(context.getText(R.string.title_att));
		dlgAlert.setPositiveButton(context.getText(R.string.ok), null);
		dlgAlert.create().show();
	}

	/**
	 * Dialog com o que exibe msg de erro na internet e quando usuario clica no botão
	 * redireciona para a tela de configurações do Wi-fi
	 *
	 * @param context
	 */
	public static void dialogErrorNet(final Context context){

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(context.getText(R.string.title_att));
		alertDialog.setMessage(context.getText(R.string.errorServer));
		alertDialog.setButton(context.getText(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		});
		alertDialog.show();

	}

	/**
	 *
	 * Retornar a URI de determinada imagem
	 *
	 * @param inImage
	 * @param context
	 * @return Uri
	 */
	public static Uri getImageUri(Bitmap inImage,Context context) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		String path = Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public static void copiarImagem(InputStream is, OutputStream os)
	{
		final int buffer_size=1024;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;

				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}


	/**
	 *
	 * Metodo que arredonda a imagem do icon do usuario
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getCircularBitmap(Bitmap bitmap) {
		Bitmap output;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888);
		} else {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		float r = 0;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			r = bitmap.getHeight() / 2;
		} else {
			r = bitmap.getWidth() / 2;
		}

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(r, r, r, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}


	/**
	 *
	 * Metodo responsavel por compartilhar a imagem com as redes sociais
	 *
	 * @param bitMap
	 * @param context
	 */
	public static void compartilhar(Bitmap bitMap,Context context) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		Uri phototUri = Util.getImageUri(bitMap,context);
		shareIntent.setData(phototUri);
		shareIntent.setType("image/png");
		shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
		context.startActivity(Intent.createChooser(shareIntent, ""));
	}
}
