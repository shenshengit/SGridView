package com.example.sgridview.model;

import android.widget.Button;

public class WordButton {

	private int mIndex;//��������
	
	private boolean mIsVisiable; //�Ƿ�����
	
	private String mViewString; //��ǰ����
	
	private Button mViewButton; //��ǰ��ť��
	
	public WordButton(){
		this.mIsVisiable = true;//��ǰ��ť�ɼ�
		this.mViewString = ""; //��ǰ����Ϊ��
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
