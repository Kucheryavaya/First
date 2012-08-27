package com.vm.covercam.gallery;

import com.vm.covercam.R;
import com.vm.covercam.constants.CFConstants;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
/**
 * @author Kucheryavaya Lyudmila
 */
public class ImageAdapter extends BaseAdapter {
	private Context context;
	private Integer[] pics = null;
	
	public ImageAdapter(Context c) {
		context = c;
		pics = CFConstants.getPics();
	}

	@Override
	public int getCount() {
		return pics.length;
	}

	@Override
	public Object getItem(int arg0) {

		return arg0;
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ImageView iv = new ImageView(context);
		iv.setImageResource(pics[arg0]);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		iv.setLayoutParams(new Gallery.LayoutParams(120,120));
		iv.setBackgroundResource(R.drawable.bg);
		iv.setPadding(7, 7, 7, 7);
		return iv;
	}
}
