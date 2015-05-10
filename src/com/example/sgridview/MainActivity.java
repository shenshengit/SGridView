package com.example.sgridview;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sgridview.data.Const;
import com.example.sgridview.model.IButtonClickListener;
import com.example.sgridview.model.Photo;
import com.example.sgridview.model.WordButton;
import com.example.sgridview.myui.MyGridView;
import com.example.sgridview.util.Util;

public class MainActivity extends Activity implements IButtonClickListener {

	/** ��״̬ -- ��ȷ */
	public final static int STATUS_ANSWER_RIGHT = 1;

	/** ��״̬ -- ���� */
	public final static int STATUS_ANSWER_WRONG = 2;

	/** ��״̬ -- ������ */
	public final static int STATUS_ANSWER_LACK = 3;

	// ͼƬչʾ
	ImageView iv;

	// ����˸�Ĵ���
	public final static int SPASH_TIMES = 6;
	private ArrayList<WordButton> mAllWords;
	private MyGridView mMyGridView;

	private ArrayList<WordButton> mSelectWords;
	private LinearLayout mSelectWordsContatner;

	// ͼƬ���󣬹���ȡ���������Ա����ʹ��
	private Photo mCurrentPhoto;
	private int mCurrentPhotoIndex = -1;

	// ����ȷ�Ĳ��ֽ���
	private LinearLayout mAnswer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ����View�ĳ�ʼ������
		initUiView();

		// �������ݺͲ��ְ󶨵ĳ�ʼ������
		initCurrentStageData();

		// ע����̰�ť����ӿ��¼�
		mMyGridView.sregistOnWordButtonClick(this);
	}

	/**
	 * ��ʼ��UI��
	 */
	private void initUiView() {

		// ͼƬ
		iv = (ImageView) findViewById(R.id.imageviewmain);

		// ���̵Ĳ���
		mMyGridView = (MyGridView) findViewById(R.id.sg);

		// �����Ĳ���
		mSelectWordsContatner = (LinearLayout) findViewById(R.id.word_select_container);
	}

	/**
	 * ��ʼ�����ݺͿؼ��İ�
	 */
	private void initCurrentStageData() {
		
		
		// ��ȡͼƬ����Ϣ
		mCurrentPhoto = loadStagePhotoInfo(++mCurrentPhotoIndex);
		Log.i("��ǰ��", mCurrentPhotoIndex + "");
		Log.i("sgridview�����ֳ���", mCurrentPhoto.getPhotoNmaeLength() + "");

		//����ͼƬ
		Log.i("��ǰ���ļ�����", mCurrentPhoto.getPhotoFileNme() + "");
		iv.setImageBitmap(loadImage(mCurrentPhoto.getPhotoFileNme()));
		
		// ���ԭ���Ĵ�,����������һ�ص����������
		mSelectWordsContatner.removeAllViews();

		// ��ʾ��ǰ�ص�����

		// �󶨼������ݺͲ���
		mAllWords = initAllWord();
		mMyGridView.updataData(mAllWords);

		// �����������ݺͲ���
		mSelectWords = initSelectWord();
		// LayoutParams params = new LayoutParams(140, 140);
		// params.setMargins(left, top, right, bottom);
		for (int i = 0; i < mSelectWords.size(); i++) {
			// mSelectWordsContatner.addView(mSelectWords.get(i).getmViewButton(),params);
			mSelectWordsContatner.addView(mSelectWords.get(i).getmViewButton());
		}

	}

	private Bitmap loadImage(String fileName) {
		AssetManager assets = getAssets();
		InputStream assetFile = null;
		Bitmap bitmap = null;
		try {
			assetFile = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(assetFile);
			assetFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (bitmap);
	}

	/**
	 * ��ʼ������ --- ������
	 * 
	 * @return
	 */
	private ArrayList<WordButton> initSelectWord() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();

		for (int i = 0; i < mCurrentPhoto.getPhotoNmaeLength(); i++) {
			final WordButton button = new WordButton();

			View view = Util.getView(MainActivity.this,
					R.layout.input_item_button);
			button.setmViewButton((Button) view.findViewById(R.id.input_item));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(20, 0, 20, 0);
			lp.width = 140;
			lp.height = 140;
			button.getmViewButton().setLayoutParams(lp);
			// button.getmViewButton().setBackgroundResource(R.drawable.input_box);//
			button.getmViewButton().setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// ������������߼�
							clearTheAnswer(button);
						}
					});

			data.add(button);

		}
		return data;
	}

	/**
	 * ��ʼ������ --- ���̵�
	 * 
	 * @return
	 */
	private ArrayList<WordButton> initAllWord() {

		ArrayList<WordButton> data = new ArrayList<WordButton>();

		char[] c = loadAllWord(mCurrentPhotoIndex);

		for (int i = 0; i < MyGridView.COUNT_WORDS; i++) {
			WordButton button = new WordButton();
			button.setmViewString(c[i] + "");
			data.add(button);
		}
		return data;
	}

	/**
	 * ��ť���µļ����¼�
	 */
	@Override
	public void onWordBUttonCLickListener(WordButton wordButton) {
		// Toast.makeText(this, wordButton.getmIndex() + "", Toast.LENGTH_SHORT)
		// .show();
		setSelectWord(wordButton);

		switch (checkTheAnswer()) {
		case STATUS_ANSWER_RIGHT:
			// ����ȷ
			correctAnswer();
			break;
		case STATUS_ANSWER_WRONG:
			// �𰸴���
			sparkTheWrods();
			Toast.makeText(MainActivity.this, "�𰸴���", Toast.LENGTH_SHORT)
					.show();
			break;
		case STATUS_ANSWER_LACK:
			// �𰸲�����
			// ��ɫ����
			for (int i = 0; i < mSelectWords.size(); i++) {
				mSelectWords.get(i).getmViewButton().setTextColor(Color.WHITE);
			}
			break;
		}
		;

	}

	/**
	 * ���ֵ������ �������������߼�
	 * 
	 * @param wordButton
	 */
	private void setSelectWord(WordButton wordButton) {
		for (int i = 0; i < mSelectWords.size(); i++) {

			if (mSelectWords.get(i).getmViewString().length() == 0) {
				// ���������
				mSelectWords.get(i).getmViewButton()
						.setText(wordButton.getmViewString());
				mSelectWords.get(i).setmIsVisiable(true);
				mSelectWords.get(i).setmViewString(wordButton.getmViewString());
				// ��¼����
				mSelectWords.get(i).setmIndex(wordButton.getmIndex());

				// ���ü��̰�ť������
				wordButton.getmViewButton().setVisibility(View.INVISIBLE);
				wordButton.setmIsVisiable(false);

				break;// ���ú���һ�����־�Ҫ�˳��ж�ѭ��
			}
		}
	}

	/**
	 * �����ĵ���߼�
	 * 
	 * @param wordButton
	 */
	private void clearTheAnswer(WordButton wordButton) {
		wordButton.getmViewButton().setText("");
		wordButton.setmViewString("");
		wordButton.setmIsVisiable(false);

		mAllWords.get(wordButton.getmIndex()).getmViewButton()
				.setVisibility(View.VISIBLE);
		wordButton.setmIsVisiable(true);
		for (int i = 0; i < mSelectWords.size(); i++) {
			mSelectWords.get(i).getmViewButton().setTextColor(Color.WHITE);
		}
	}

	/**
	 * ��ȡ��ǰ�ص���Ϣ
	 * 
	 * @param stageIndex
	 * @return
	 */
	public Photo loadStagePhotoInfo(int stageIndex) {
		Photo photo = new Photo();
		String[] stage = Const.PHOTO_INFD[stageIndex];
		photo.setPhotoFileNme(stage[Const.INDEX_FELE_NAME]);
		photo.setPhotoName(stage[Const.INDEX_PHONT_NAME]);
		return photo;
	}

	/**
	 * ��ȡ24��Ҫ�õ��ַ�
	 * 
	 * @param stageIndex
	 * @return
	 */
	public char[] loadAllWord(int stageIndex) {
		char[] char1 = new char[] {};
		String str = "";
		// if(char1.length <= MyGridView.COUNT_WORDS){

		// �ӵ�ǰ��������ʼ��������ȡ�ַ���ȫ��ȡ���˳�ѵ���ַ�������24���Ļ������ѭ��ִ���¸�һѭ��
		for (int i = stageIndex; i < Const.PHOTO_INFD.length; i++) {
			String[] stage = Const.PHOTO_INFD[i];
			str = str + stage[Const.INDEX_PHONT_NAME];
			if (str.length() >= MyGridView.COUNT_WORDS) {
				// char1 = str.toCharArray();
				// Log.v("ȥ�����������24�����������ѭ���˳�����", str);
				// // ��ӡ��һ����ȡ�˶��ٸ��ֳ���
				// Log.i("==================", "loadAllWord������ȡ��" + char1.length
				// +
				// "���ֳ���");
				// return char1;
				return returnChar(str);
			}

		}
		for (int i = 0; i < Const.PHOTO_INFD.length; i++) {
			String[] stage = Const.PHOTO_INFD[i];
			str = str + stage[Const.INDEX_PHONT_NAME];
			if (str.length() >= MyGridView.COUNT_WORDS) {
				// char1 = str.toCharArray();
				// Log.v("�ϸ�ѭ��û��ȡ��24���֣��ͽ���ִ�����ѭ����ֱ��ȡ��24����", str);
				// // ��ӡ��һ����ȡ�˶��ٸ��ֳ���
				// Log.i("==================", "loadAllWord������ȡ��" + char1.length
				// + "���ֳ���");
				Log.i("ȡ�ֵڶ���ѭ��", "ִ����û");
				return returnChar(str);
			}

		}

		return char1;
	}

	/**
	 * ��ѭ�����ж�ȡ���������ǲ��ǹ�24�� ���Ļ�ִ�д�������ѭ�򲢷��س�ȥ
	 * 
	 * @return
	 */
	private char[] returnChar(String s) {
		char[] a = new char[] {};
		a = s.toCharArray();
		Log.v("ȥ�����������24�����������ѭ���˳�����", s);
		// ��ӡ��һ����ȡ�˶��ٸ��ֳ���
		Log.i("==================", "loadAllWord������ȡ��" + a.length + "���ֳ���");

		// ��������˳��
		// ���ȴ�����Ԫ�������ѡȡһ�����һ��Ԫ�ؽ��н�����
		// Ȼ���ٵڶ���֮��ѡ��һ��Ԫ����ڶ���������ֱ�����һ��Ԫ�ء�
		// �����ܹ�ȷ��ÿ��Ԫ����ÿ��λ�õĸ��ʶ���1/n��
		 Random random = new Random();
		 for (int i = MyGridView.COUNT_WORDS - 1; i > 0; i--) {
		 int index = random.nextInt(i + 1);
		 char buf = a[index];
		 a[index] = a[i];
		 a[i] = buf;
		 }

		return a;
	}

	/**
	 * ����
	 * 
	 * @return ����״̬
	 */

	private int checkTheAnswer() {
		for (int i = 0; i < mSelectWords.size(); i++) {
			if (mSelectWords.get(i).getmViewString().length() == 0) {
				return STATUS_ANSWER_LACK;
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mSelectWords.size(); i++) {

			sb.append(mSelectWords.get(i).getmViewString());

		}
		return ((sb.toString()).equals(mCurrentPhoto.getPhotoName())) ? STATUS_ANSWER_RIGHT
				: STATUS_ANSWER_WRONG;
	}

	/**
	 * �𰸴��� ������˸
	 */
	private void sparkTheWrods() {
		// ��ʱ��
		TimerTask task = new TimerTask() {
			boolean mChange = false;
			int mSpardTimes = 0;

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// ��ʾ��˸����
						if (++mSpardTimes > SPASH_TIMES) {
							return;
						}
						// ִ����˸�߼���������ʾ��ɫ�Ͱ�ɫ����
						for (int i = 0; i < mSelectWords.size(); i++) {
							mSelectWords
									.get(i)
									.getmViewButton()
									.setTextColor(
											mChange ? Color.RED : Color.WHITE);
						}
						mChange = !mChange;
					}
				});
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 1, 150);
	}

	/**
	 * ����ȷ����ʾ��ȷ����
	 */
	private void correctAnswer() {
		mAnswer = (LinearLayout) findViewById(R.id.answer_layout);
		mAnswer.setVisibility(View.VISIBLE);

		Button btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mAnswer.setVisibility(View.GONE);
				// Ҫ�ж����ǲ������һ��
				if (mCurrentPhotoIndex == Const.PHOTO_INFD.length - 1) {
					Toast.makeText(MainActivity.this, "���һ��",
							Toast.LENGTH_SHORT).show();
					mCurrentPhotoIndex = -1;
					initCurrentStageData();
				} else {
					initCurrentStageData();
				}

			}
		});
	}
}
