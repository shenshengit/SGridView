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

	// ��ť�����¼� �ӿڶ���
	IButtonClickListener mButtonClickListener;

	// ��������
	private Animation mAnimation;

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context; // �Ѽ���GridView��Activity������Ա�����������������ã�

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
			return mWoridList.size(); // ��������
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
			final WordButton wbtn;// ÿ��item�Ķ���������

			mAnimation = AnimationUtils.loadAnimation(mContext,
					R.anim.button_in);
			mAnimation.setStartOffset(arg0 * 100);

			if (arg1 == null) {
				arg1 = Util.getView(mContext, R.layout.grid_item_button); // �õ����ֵ�View����

				wbtn = mWoridList.get(arg0); // �Ӽ����еõ������ʵ��
				wbtn.setmIndex(arg0); // ����ʵ�����������

				if (wbtn.getmViewButton() == null) {
					wbtn.setmViewButton((Button) arg1
							.findViewById(R.id.item_button)); // ����ʵ������İ�ť
																// �ò��ֵ�View���ҵ�
				}
				arg1.setTag(wbtn); // ���ñ�ǩ�������� ������
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
