package com.example.sgridview.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class Util {
	
	/**
	 * ����תView����Ĺ����෽��
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
