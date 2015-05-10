package com.example.sgridview.model;

public class Photo {

	// 图片名称
	private String mPhotoName;
	// 图片文件名
	private String mPhotoFileNme;
	// 图片名称长度
	private int mPhotoNmaeLength;

	public char[] getNameCharacters() {
		return mPhotoName.toCharArray();
	}

	public String getPhotoName() {
		return mPhotoName;
	}

	public void setPhotoName(String mPhotoName) {
		this.mPhotoName = mPhotoName;
		this.mPhotoNmaeLength = mPhotoName.length();
	}

	public String getPhotoFileNme() {
		return mPhotoFileNme;
	}

	public void setPhotoFileNme(String mPhotoFileNme) {
		this.mPhotoFileNme = mPhotoFileNme;
	}

	public int getPhotoNmaeLength() {
		return mPhotoNmaeLength;
	}
}
