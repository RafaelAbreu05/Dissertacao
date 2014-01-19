package atividadePrincipal.planta;

import java.util.TreeMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import classes.PontoAcesso;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.lomza.R;

/**
 * This is the Second tab list fragment.
 */
public class PlantaFragment extends SherlockFragment {

	private static View _fragmentView;
	private static Canvas canvas;
	public static Context baseContext;
	private static TouchImageView imageView;

	private static TreeMap<Integer, PontoAcesso> pontosAcesso = new TreeMap<Integer, PontoAcesso>();
	private static String nomeFicheiro = null;

	public static void setNomeFicheiro(String nomeF) {
		nomeFicheiro = nomeF;
		refreshList();
	}

	@SuppressWarnings("unchecked")
	public static void setPontosAcesso(TreeMap<Integer, PontoAcesso> pa) {
		pontosAcesso = (TreeMap<Integer, PontoAcesso>) pa.clone();
		refreshList();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (nomeFicheiro != null) {
			uploadImagem();
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (nomeFicheiro != null) {
			uploadImagem();
		}
		baseContext = getActivity();
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_fragmentView = inflater.inflate(R.layout.planta_layout, container,
				false);

		if (_fragmentView == null)
			return null;

		imageView = (TouchImageView) _fragmentView.findViewById(R.id.imageView);
		imageView.setMaxZoom(4f);

		if (nomeFicheiro != null) {
			uploadImagem();
		}

		return _fragmentView;
	}

	public static void uploadImagem() {
		String path = "/storage/sdcard1/Imagens/" + nomeFicheiro;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		if (imageWidth > imageHeight) {
			options.inSampleSize = calculateInSampleSize(options, 512, 256);
		} else {
			options.inSampleSize = calculateInSampleSize(options, 256, 512);
		}
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);

		Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.RGB_565);

		imageView.setBackgroundColor(Color.parseColor("#80000000"));

		canvas = new Canvas(tempBitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);

		if (!pontosAcesso.isEmpty()) {
			for (Integer i : pontosAcesso.keySet()) {
				PontoAcesso p = pontosAcesso.get(i);
				if (p.getCoordenadas() != null) {

					Bitmap marker = BitmapFactory.decodeResource(
							_fragmentView.getResources(), icon(i));
					int markerLarg = marker.getWidth();
					int markerAlt = marker.getHeight();

					float auxX = (float) (p.getCoordenadas().getPosX() - (markerLarg / 2));
					float auxY = (float) (p.getCoordenadas().getPosY() - (markerAlt / 2));

					canvas.drawBitmap(marker, auxX, auxY, null);
					i++;
				}
			}
		}
		imageView.setImageDrawable(new BitmapDrawable(_fragmentView
				.getResources(), tempBitmap));
	}

	/** Retorna o icon com número */
	public static int icon(int num) {
		int icon;
		switch (num) {
		case 1:
			icon = R.drawable.w1;
			break;
		case 2:
			icon = R.drawable.w2;
			break;
		case 3:
			icon = R.drawable.w3;
			break;
		case 4:
			icon = R.drawable.w4;
			break;
		case 5:
			icon = R.drawable.w5;
			break;
		case 6:
			icon = R.drawable.w6;
			break;
		case 7:
			icon = R.drawable.w7;
			break;
		case 8:
			icon = R.drawable.w8;
			break;
		case 9:
			icon = R.drawable.w9;
			break;
		default:
			icon = R.drawable.unknown;
			break;
		}
		return icon;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.action_refresh))) {
			refreshList();
			return true;
		}

		return false;
	}

	/**
	 * Refreshes the list.
	 */
	private static void refreshList() {
		if (nomeFicheiro != null) {
			uploadImagem();
		}
	}
}
