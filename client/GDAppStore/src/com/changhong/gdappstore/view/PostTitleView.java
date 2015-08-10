package com.changhong.gdappstore.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseRelativeLayout;
import com.changhong.gdappstore.model.PostTitleModel;
import com.changhong.gdappstore.util.L;

/**
 * 海报墙标签栏目view，能动态添加更新标签数据
 * 
 * @author wangxiufeng
 * 
 */
public class PostTitleView extends BaseRelativeLayout {

	/**
	 * 标签焦点监听器接口
	 * 
	 * @author wangxiufeng
	 * 
	 */
	public interface TitleItemOnFocuesChangedListener {
		public void onItemFocuesChanged(View view, boolean hasFocues, int position);
	}

	/**
	 * 标签点击监听器接口
	 * 
	 * @author wangxiufeng
	 * 
	 */
	public interface TitleItemOnClickListener {
		public void onItemClick(View view, int position);
	}

	private LinearLayout ll_content;

	private LinearLayout.LayoutParams layoutParams;
	/** 标签textview列表 */
	private List<TextView> list_textViews = new ArrayList<TextView>();
	/** 当前选中的view */
	private View currentSelectedView = null;
	/** 标签焦点监听器 */
	private TitleItemOnFocuesChangedListener itemOnFocuesChangedListener;
	/** 标签点击监听器 */
	private TitleItemOnClickListener itemOnClickListener;

	public PostTitleView(Context context) {
		super(context);
		init();
	}

	public PostTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PostTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		ll_content = new LinearLayout(context);
		ll_content.setOrientation(LinearLayout.HORIZONTAL);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = 0;
		layoutParams.rightMargin = 0;
		layoutParams.gravity = Gravity.CENTER;
		removeAllViews();
		addView(ll_content);
	}

	public void initData(List<PostTitleModel> items) {
		if (items == null || items.size() <= 0) {
			return;
		}
		ll_content.removeAllViews();
		list_textViews.clear();
		for (int i = 0; i < items.size(); i++) {
			View itemView = getItemView(items.get(i),i);
			ll_content.addView(itemView);
		}
		int count = list_textViews.size();
		// 左右焦点定死
		for (int i = 0; i < count; i++) {
			if (i == 0 && i == count - 1) {
				break;
			}
			L.d("posttitle init " + list_textViews.get(i));
			if (i == 0) {
				list_textViews.get(i).setNextFocusRightId(list_textViews.get(i + 1).getId());
				list_textViews.get(i).setNextFocusLeftId(list_textViews.get(i).getId());
			} else if (i == count - 1) {
				list_textViews.get(i).setNextFocusRightId(list_textViews.get(i).getId());
				list_textViews.get(i).setNextFocusLeftId(list_textViews.get(i - 1).getId());
			} else {
				list_textViews.get(i).setNextFocusRightId(list_textViews.get(i + 1).getId());
				list_textViews.get(i).setNextFocusLeftId(list_textViews.get(i - 1).getId());
			}
		}
		ll_content.invalidate();
	}
	
	public void setFocusItem(int position) {
		if (position < 0 || position >= list_textViews.size()) {
			list_textViews.get(position).requestFocus();
			currentSelectedView=list_textViews.get(position);
		}
	}

	/**
	 * 获取itemview
	 * 
	 * @param model
	 * @return
	 */
	private View getItemView(PostTitleModel model,final int position) {
		final View view = LayoutInflater.from(context).inflate(R.layout.item_titleview, null);
		final TextView textView = (TextView) view.findViewById(R.id.tv_title);
		textView.setText(model.getName());
		textView.setId(4522342 + position);
		list_textViews.add(textView);
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (itemOnClickListener!=null) {
					itemOnClickListener.onItemClick(textView, position);
				}
			}
		});
		textView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (itemOnFocuesChangedListener!=null) {
					itemOnFocuesChangedListener.onItemFocuesChanged(v, hasFocus, position);
				}
				if (hasFocus) {
					textView.setSelected(true);
					if (currentSelectedView != null && currentSelectedView != v) {
						currentSelectedView.setSelected(false);
					}
					currentSelectedView = v;
				} else {
					textView.setSelected(hasChildFocesed());
				}
			}
		});
		return view;
	}

	/**
	 * 是否有标签处于选中状态
	 * 
	 * @return
	 */
	public boolean hasChildFocesed() {
		for (int i = 0; i < list_textViews.size(); i++) {
			if (list_textViews.get(i).isFocusable()) {
				return true;
			}
		}
		return false;
	}

	public TitleItemOnFocuesChangedListener getItemOnFocuesChangedListener() {
		return itemOnFocuesChangedListener;
	}

	public void setItemOnFocuesChangedListener(TitleItemOnFocuesChangedListener itemOnFocuesChangedListener) {
		this.itemOnFocuesChangedListener = itemOnFocuesChangedListener;
	}

	public TitleItemOnClickListener getItemOnClickListener() {
		return itemOnClickListener;
	}

	public void setItemOnClickListener(TitleItemOnClickListener itemOnClickListener) {
		this.itemOnClickListener = itemOnClickListener;
	}

	public List<TextView> getList_textViews() {
		return list_textViews;
	}

	public View getCurrentSelectedView() {
		return currentSelectedView;
	}

	// private TextView getItemView(PostTitleModel model) {
	// final TextView textView = new TextView(context);
	// // textView.setWidth(100);
	// // textView.setHeight(50);
	// textView.setFocusable(true);
	// textView.setBackgroundResource(R.drawable.selector_btn_title);
	// if (model != null) {
	// textView.setText(model.getName());
	// }
	// // textView.setTextColor(R.drawable.selector_btn_title_textcolor);
	// textView.setTextColor(Color.WHITE);
	// textView.setTextSize(22);
	// textView.setLayoutParams(layoutParams);
	// textView.invalidate();
	// textView.setGravity(Gravity.CENTER);
	// textView.setOnFocusChangeListener(new OnFocusChangeListener() {
	//
	// @Override
	// public void onFocusChange(View v, boolean hasFocus) {
	// if (hasFocus) {
	// textView.setSelected(true);
	// }
	// }
	// });
	// return textView;
	// }

}