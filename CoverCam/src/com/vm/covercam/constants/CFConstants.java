package com.vm.covercam.constants;

import com.vm.covercam.R;

/**
 * @author Kucheryavaya Lyudmila
 */

public class CFConstants {
	private static int COLOR_FILTER = 0;
	private static int WIDTH_CANVAS = 350;
	private static int HEIGHT_CANVAS = 570;
	
	private static Integer[] pics = {
		R.drawable.preview_cosmopolitan,
		R.drawable.preview_glamour,
		R.drawable.preview_gq,
		R.drawable.preview_forbes,
		R.drawable.preview_marie_claire,
		R.drawable.preview_mens_health,
		R.drawable.preview_people,
		R.drawable.preview_playboy,
		R.drawable.preview_rolling_stone,
		R.drawable.preview_vogue
	};
	
	public CFConstants() {}

	public static void setColorFilter(int color_filter) {
		COLOR_FILTER = color_filter;
	}
	
	public static int getColorFilter() {
		return COLOR_FILTER;
	}
	
	public static int getSizeCanvas(String flag) {
		if (flag=="height") {
			return HEIGHT_CANVAS;
		}
		else
			return WIDTH_CANVAS;
	}
	
	public static Integer[] getPics() {
		return pics;
	}
	
}
