package com.changhong.gdappstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseRelativeLayout;

public class TitleView extends BaseRelativeLayout implements
		OnFocusChangeListener, OnClickListener {

	private Button btn_jingpin, btn_yule, btn_yingyong, btn_youxi;

	private OnClickListener onClickListener;

	private OnFocusChangeListener onFocusChangeListener;

	private View lastSelectedView;

	public TitleView(Context context) {
		super(context);
		initView();
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	protected void initView() {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.view_title, null);
		addView(rootView);
		btn_jingpin = findView(R.id.bt_title_jingpin);
		btn_yingyong = findView(R.id.bt_title_yingyong);
		btn_youxi = findView(R.id.bt_title_youxi);
		btn_yule = findView(R.id.bt_title_yule);
		btn_jingpin.setOnClickListener(this);
		btn_yingyong.setOnClickListener(this);
		btn_youxi.setOnClickListener(this);
		btn_yule.setOnClickListener(this);
		btn_jingpin.setOnFocusChangeListener(this);
		btn_yingyong.setOnFocusChangeListener(this);
		btn_youxi.setOnFocusChangeListener(this);
		btn_yule.setOnFocusChangeListener(this);
	}

	public void setSelectedItemById(int btnid) {
		if (lastSelectedView != null) {
			lastSelectedView.setSelected(false);
		}
		
		if (btnid == R.id.bt_title_jingpin) {
			btn_jingpin.setSelected(true);
			lastSelectedView = btn_jingpin;
		} else if (btnid == R.id.bt_title_yingyong) {
			btn_yingyong.setSelected(true);
			lastSelectedView = btn_yingyong;
		} else if (btnid == R.id.bt_title_youxi) {
			btn_youxi.setSelected(true);
			lastSelectedView = btn_youxi;
		} else if (btnid == R.id.bt_title_yule) {
			btn_yule.setSelected(true);
			lastSelectedView = btn_yule;
		}
	}

	@Override
	public void onClick(View v) {
		if (onClickListener != null) {
			onClickListener.onClick(v);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && !hasChildFocesed()) {
			v.setSelected(true);// 处理焦点在海报时候，当前标签处于选中状态
			lastSelectedView = v;
		} else {
			v.setSelected(false);
			if (lastSelectedView != null) {
				lastSelectedView.setSelected(false);
			}
		}

		if (onFocusChangeListener != null) {
			onFocusChangeListener.onFocusChange(v, hasFocus);
		}
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void setOnFocusChangeListener(
			OnFocusChangeListener onFocusChangeListener) {
		this.onFocusChangeListener = onFocusChangeListener;
	}

	/**
	 * 是否有标签处于选中状态
	 * 
	 * @return
	 */
	public boolean hasChildFocesed() {
		return btn_jingpin.isFocused() || btn_yingyong.isFocused()
				|| btn_youxi.isFocused() || btn_yule.isFocused();
	}

	public Button getBtn_jingpin() {
		return btn_jingpin;
	}

	public Button getBtn_yule() {
		return btn_yule;
	}

	public Button getBtn_yingyong() {
		return btn_yingyong;
	}

	public Button getBtn_youxi() {
		return btn_youxi;
	}

	public View getLastSelectedView() {
		return lastSelectedView;
	}

}
