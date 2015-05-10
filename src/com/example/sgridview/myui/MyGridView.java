package com.example.sgridview.myui;

import java.util.ArrayList;

import com.example.sgridview.R;
import com.example.sgridview.model.IButtonClickListener;
import com.example.sgridview.model.WordButton;
import com.example.sgridview.util.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MyGridView extends GridView {

	public static final int COUNT_WORDS = 24;

	private ArrayList<WordButton> mWoridList = new ArrayList<WordButton>();;
	private MyGridAdapter mAdapter;
	private Context mContext;

	// 按钮监听事件 接口对象
	IButtonClickListener mButtonClickListener;

	// 动画对象
	private Animation mAnimation;

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context; // 把加载GridView的Activity传给成员变量，供其他方法用；

		mAdapter = new MyGridAdapter();
		this.setAdapter(mAdapter);
	}

	public void updataData(ArrayList<WordButton> list) {
		mWoridList = list;

		this.setAdapter(mAdapter);
	}

	public void sregistOnWordButtonClick(IButtonClickListener listener) {
		this.mButtonClickListener = listener;
	}

	class MyGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mWoridList.size(); // 返回数量
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mWoridList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final WordButton wbtn;// 每个item的对象声明；

			mAnimation = AnimationUtils.loadAnimation(mContext,
					R.anim.button_in);
			mAnimation.setStartOffset(arg0 * 100);

			if (arg1 == null) {
				arg1 = Util.getView(mContext, R.layout.grid_item_button); // 得到布局的View对象

				wbtn = mWoridList.get(arg0); // 从集合中得到对象的实例
				wbtn.setmIndex(arg0); // 设置实例对象的序列

				if (wbtn.getmViewButton() == null) {
					wbtn.setmViewButton((Button) arg1
							.findViewById(R.id.item_button)); // 设置实例对象的按钮
																// 用布局的View来找到
				}
				arg1.setTag(wbtn); // 设置标签保存数据 供复用
			} else {
				wbtn = (WordButton) arg1.getTag();
			}

			wbtn.getmViewButton().setText(wbtn.getmViewString());

			arg1.startAnimation(mAnimation);

			wbtn.getmViewButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mButtonClickListener.onWordBUttonCLickListener(wbtn);
				}
			});

			return arg1;
		}
	}

}
