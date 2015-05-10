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

	/** 答案状态 -- 正确 */
	public final static int STATUS_ANSWER_RIGHT = 1;

	/** 答案状态 -- 错误 */
	public final static int STATUS_ANSWER_WRONG = 2;

	/** 答案状态 -- 不完整 */
	public final static int STATUS_ANSWER_LACK = 3;

	// 图片展示
	ImageView iv;

	// 答案闪烁的次数
	public final static int SPASH_TIMES = 6;
	private ArrayList<WordButton> mAllWords;
	private MyGridView mMyGridView;

	private ArrayList<WordButton> mSelectWords;
	private LinearLayout mSelectWordsContatner;

	// 图片对象，供读取索引等类成员变量使用
	private Photo mCurrentPhoto;
	private int mCurrentPhotoIndex = -1;

	// 答案正确的布局界面
	private LinearLayout mAnswer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 调用View的初始化方法
		initUiView();

		// 调用数据和布局绑定的初始化方法
		initCurrentStageData();

		// 注册键盘按钮点击接口事件
		mMyGridView.sregistOnWordButtonClick(this);
	}

	/**
	 * 初始化UI的
	 */
	private void initUiView() {

		// 图片
		iv = (ImageView) findViewById(R.id.imageviewmain);

		// 键盘的布局
		mMyGridView = (MyGridView) findViewById(R.id.sg);

		// 输入框的布局
		mSelectWordsContatner = (LinearLayout) findViewById(R.id.word_select_container);
	}

	/**
	 * 初始化数据和控件的绑定
	 */
	private void initCurrentStageData() {
		
		
		// 读取图片的信息
		mCurrentPhoto = loadStagePhotoInfo(++mCurrentPhotoIndex);
		Log.i("当前关", mCurrentPhotoIndex + "");
		Log.i("sgridview的名字长度", mCurrentPhoto.getPhotoNmaeLength() + "");

		//加载图片
		Log.i("当前关文件名称", mCurrentPhoto.getPhotoFileNme() + "");
		iv.setImageBitmap(loadImage(mCurrentPhoto.getPhotoFileNme()));
		
		// 清空原来的答案,把容器中上一关的文字清除掉
		mSelectWordsContatner.removeAllViews();

		// 显示当前关的索引

		// 绑定键盘数据和布局
		mAllWords = initAllWord();
		mMyGridView.updataData(mAllWords);

		// 绑定输入框的数据和布局
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
	 * 初始化数据 --- 输入框的
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
							// 调用输入框点击逻辑
							clearTheAnswer(button);
						}
					});

			data.add(button);

		}
		return data;
	}

	/**
	 * 初始化数据 --- 键盘的
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
	 * 按钮按下的监听事件
	 */
	@Override
	public void onWordBUttonCLickListener(WordButton wordButton) {
		// Toast.makeText(this, wordButton.getmIndex() + "", Toast.LENGTH_SHORT)
		// .show();
		setSelectWord(wordButton);

		switch (checkTheAnswer()) {
		case STATUS_ANSWER_RIGHT:
			// 答案正确
			correctAnswer();
			break;
		case STATUS_ANSWER_WRONG:
			// 答案错误
			sparkTheWrods();
			Toast.makeText(MainActivity.this, "答案错误", Toast.LENGTH_SHORT)
					.show();
			break;
		case STATUS_ANSWER_LACK:
			// 答案不完整
			// 白色文字
			for (int i = 0; i < mSelectWords.size(); i++) {
				mSelectWords.get(i).getmViewButton().setTextColor(Color.WHITE);
			}
			break;
		}
		;

	}

	/**
	 * 文字的输入和 键盘文字隐藏逻辑
	 * 
	 * @param wordButton
	 */
	private void setSelectWord(WordButton wordButton) {
		for (int i = 0; i < mSelectWords.size(); i++) {

			if (mSelectWords.get(i).getmViewString().length() == 0) {
				// 设置输入框
				mSelectWords.get(i).getmViewButton()
						.setText(wordButton.getmViewString());
				mSelectWords.get(i).setmIsVisiable(true);
				mSelectWords.get(i).setmViewString(wordButton.getmViewString());
				// 记录索引
				mSelectWords.get(i).setmIndex(wordButton.getmIndex());

				// 设置键盘按钮的隐藏
				wordButton.getmViewButton().setVisibility(View.INVISIBLE);
				wordButton.setmIsVisiable(false);

				break;// 设置好了一个文字就要退出判断循环
			}
		}
	}

	/**
	 * 输入框的点击逻辑
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
	 * 读取当前关的信息
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
	 * 读取24个要用的字符
	 * 
	 * @param stageIndex
	 * @return
	 */
	public char[] loadAllWord(int stageIndex) {
		char[] char1 = new char[] {};
		String str = "";
		// if(char1.length <= MyGridView.COUNT_WORDS){

		// 从当前关索引开始从数组中取字符，全部取完退出训还字符还不够24个的话，完成循环执行下个一循环
		for (int i = stageIndex; i < Const.PHOTO_INFD.length; i++) {
			String[] stage = Const.PHOTO_INFD[i];
			str = str + stage[Const.INDEX_PHONT_NAME];
			if (str.length() >= MyGridView.COUNT_WORDS) {
				// char1 = str.toCharArray();
				// Log.v("去除的字如果是24个，就在这个循环退出方法", str);
				// // 打印出一共读取了多少个字出来
				// Log.i("==================", "loadAllWord方法读取了" + char1.length
				// +
				// "个字出来");
				// return char1;
				return returnChar(str);
			}

		}
		for (int i = 0; i < Const.PHOTO_INFD.length; i++) {
			String[] stage = Const.PHOTO_INFD[i];
			str = str + stage[Const.INDEX_PHONT_NAME];
			if (str.length() >= MyGridView.COUNT_WORDS) {
				// char1 = str.toCharArray();
				// Log.v("上个循环没有取到24个字，就接着执行这个循环，直到取到24个字", str);
				// // 打印出一共读取了多少个字出来
				// Log.i("==================", "loadAllWord方法读取了" + char1.length
				// + "个字出来");
				Log.i("取字第二个循环", "执行了没");
				return returnChar(str);
			}

		}

		return char1;
	}

	/**
	 * 在循环中判断取出来的字是不是够24了 够的画执行打乱文字循序并返回出去
	 * 
	 * @return
	 */
	private char[] returnChar(String s) {
		char[] a = new char[] {};
		a = s.toCharArray();
		Log.v("去除的字如果是24个，就在这个循环退出方法", s);
		// 打印出一共读取了多少个字出来
		Log.i("==================", "loadAllWord方法读取了" + a.length + "个字出来");

		// 打乱文字顺序
		// 首先从所有元素中随机选取一个与第一个元素进行交换，
		// 然后再第二个之后选择一个元素与第二个交换，直到最后一个元素。
		// 这样能够确保每个元素在每个位置的概率都是1/n。
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
	 * 检查答案
	 * 
	 * @return 常量状态
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
	 * 答案错误 文字闪烁
	 */
	private void sparkTheWrods() {
		// 定时器
		TimerTask task = new TimerTask() {
			boolean mChange = false;
			int mSpardTimes = 0;

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// 显示闪烁次数
						if (++mSpardTimes > SPASH_TIMES) {
							return;
						}
						// 执行闪烁逻辑，交替显示红色和白色文字
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
	 * 答案正确，显示正确界面
	 */
	private void correctAnswer() {
		mAnswer = (LinearLayout) findViewById(R.id.answer_layout);
		mAnswer.setVisibility(View.VISIBLE);

		Button btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mAnswer.setVisibility(View.GONE);
				// 要判断是是不是最后一关
				if (mCurrentPhotoIndex == Const.PHOTO_INFD.length - 1) {
					Toast.makeText(MainActivity.this, "最后一关",
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
