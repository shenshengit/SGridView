package com.example.sgridview.model;

import android.widget.Button;

public class WordButton {

	private int mIndex;//文字索引
	
	private boolean mIsVisiable; //是否隐藏
	
	private String mViewString; //当前文字
	
	private Button mViewButton; //当前按钮？
	
	public WordButton(){
		this.mIsVisiable = true;//当前按钮可见
		this.mViewString = ""; //当前文字为空
	}

	public int getmIndex() {
		return mIndex;
	}

	public void setmIndex(int mIndex) {
		this.mIndex = mIndex;
	}

	public boolean ismIsVisiable() {
		return mIsVisiable;
	}

	public void setmIsVisiable(boolean mIsVisiable) {
		this.mIsVisiable = mIsVisiable;
	}

	public String getmViewString() {
		return mViewString;
	}

	public void setmViewString(String mViewString) {
		this.mViewString = mViewString;
	}

	public Button getmViewButton() {
		return mViewButton;
	}

	public void setmViewButton(Button mViewButton) {
		this.mViewButton = mViewButton;
	}
	
	
	
}
