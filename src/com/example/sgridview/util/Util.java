package com.example.sgridview.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Util {
	
	/**
	 * 布局转View对象的公共类方法
	 * 
	 * @param context
	 * @param layoutID
	 * @return
	 */
	public static View getView(Context context, int layoutID){
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(layoutID, null);
		return layout;
		
	}
	
}
