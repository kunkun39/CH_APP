package com.changhong.gdappstore.post;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.view.ScoreView;
import com.post.view.base.BasePostItem;

public class PostItem extends BasePostItem {
	private PostSetting postSetting;
	private ImageView iv_appicon;
	private TextView tv_appname, tv_apptext;
	private ScoreView scoreView;

	public PostItem(Context context) {
		super(context);
		this.context = context;
	}

	public PostItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public PostItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void init(PostSetting postSetting) {
		this.postSetting = postSetting;
		switch (postSetting.getPosttype()) {
		case PostSetting.TYPE_NORMAL:
		case PostSetting.TYPE_NATIVEAPP:
			initNormalView();
			break;
		default:
			initNormalView();
			break;
		}
		setFocusable(true);
	}

	private void initNormalView() {
		View view = LayoutInflater.from(context).inflate(R.layout.item_apppost, null);
		addView(view);
		iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
		tv_appname = (TextView) view.findViewById(R.id.tv_appname);
		scoreView = (ScoreView) view.findViewById(R.id.scoreview);
	}

	/**
	 * 设置数据
	 * 
	 * @param object
	 */
	public void setData(Object object, int dataPosition, int selectedPosition) {
		this.dataPosition = dataPosition;
		this.selectedPos = selectedPosition;
		switch (postSetting.getPosttype()) {
		case PostSetting.TYPE_NORMAL:
			doNormalData(object);
			break;
		case PostSetting.TYPE_NATIVEAPP:
			doNativeAppData(object);
			break;
		default:
			break;
		}

	}

	private void doNormalData(Object object) {
		App app = (App) object;
		if (app==null) {
			return;
		}
		ImageLoadUtil.displayImgByonlyDiscCache(app.getPosterFilePath(), iv_appicon);
		tv_appname.setText(app.getAppname());
		scoreView.setVisibility(GONE);
	}
	private void doNativeAppData(Object object) {
		NativeApp app = (NativeApp) object;
		if (app==null) {
			return;
		}
		iv_appicon.setImageDrawable(app.getAppIcon());
		tv_appname.setText(app.getAppname());
		scoreView.setVisibility(GONE);
	}

	/**
	 * 焦点处理 如果焦点只需要在这里处理那么需要在PosterWallView的init()
	 * 方法里面把背景设置去掉或者不在PostSetting里面配置itemFocusDrawableId
	 * 
	 * @param hasfocues
	 */
	public void setOnFocuesChange(boolean hasfocues) {

		switch (postSetting.getPosttype()) {
		case PostSetting.TYPE_NORMAL:
		case PostSetting.TYPE_NATIVEAPP:
			doScalse(hasfocues);
			break;
		default:
			 doScalse(hasfocues);
			break;
		}
	}

	/**
	 * 清楚选中状态
	 */
	public void clearItemSelected() {
		setFocuesType(TYPE_NORMAL);
	}

	public void setFocuesType(int type) {
		switch (postSetting.getPosttype()) {
		case PostSetting.TYPE_NORMAL:
		case PostSetting.TYPE_NATIVEAPP:
			if (type == TYPE_FOCUES) {
				setSelected(false);
			} else if (type == TYPE_SELECTED) {
				setSelected(true);
			} else if (type == TYPE_SELECTED_FOCUES) {
				setSelected(true);
			} else {
				setSelected(false);
			}
			break;
		default:
			break;
		}
		invalidate();
	}

	private void doScalse(boolean isScaleIn) {
		if (isScaleIn) {
			startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_big));
		} else {
			startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_small));
		}
	}

}
