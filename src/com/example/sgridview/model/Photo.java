package com.example.sgridview.model;

public class Photo {

	// ͼƬ����
	private String mPhotoName;
	// ͼƬ�ļ���
	private String mPhotoFileNme;
	// ͼƬ���Ƴ���
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
