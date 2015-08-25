package com.changhong.gdappstore.post;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.view.ScoreView;
import com.post.view.base.BasePostItem;

public class PostItem extends BasePostItem {
	private PostSetting postSetting;
	private ImageView iv_appicon, iv_update;
	private TextView tv_appname,tv_apksize;
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
			initNormalView();
			break;
		case PostSetting.TYPE_NATIVEAPP:
			initNativeAppView();
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
		tv_apksize=(TextView)view.findViewById(R.id.tv_apksize);
		scoreView=(ScoreView)view.findViewById(R.id.scoreview);
	}

	private void initNativeAppView() {
		View view = LayoutInflater.from(context).inflate(R.layout.item_nativeapppost, null);
		addView(view);
		iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
		tv_appname = (TextView) view.findViewById(R.id.tv_appname);
		iv_update = (ImageView) view.findViewById(R.id.iv_update);
		iv_update.setVisibility(INVISIBLE);
	}

	/**
	 * 设置数据
	 * 
	 * @param object
	 */
	public void setData(Object object, int dataPosition, int selectedPosition) {
		this.dataPosition = dataPosition;
		this.selectedPos = selectedPosition;
		setTag(object);
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
		if (app == null) {
			return;
		}
		ImageLoadUtil.displayImgByonlyDiscCache(app.getPosterFilePath(), iv_appicon);
		tv_appname.setText(app.getAppname());
		tv_apksize.setText(app.getApkSize());
		scoreView.setScoreBy5Total(5);
	}

	private void doNativeAppData(Object object) {
		NativeApp app = (NativeApp) object;
		if (app == null) {
			return;
		}
		iv_appicon.setImageDrawable(app.getAppIcon());
		tv_appname.setText(app.getAppname());
		try {
			if (Float.parseFloat(app.ServerVersion) > Float.parseFloat(app.nativeVersion)) {
				iv_update.setVisibility(VISIBLE);
			} else {
				iv_update.setVisibility(INVISIBLE);
			}
		} catch (Exception e) {
			iv_update.setVisibility(INVISIBLE);
			e.printStackTrace();
		}
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
			if (tv_appname!=null) {
				tv_appname.setSelected(hasfocues);
			}
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
			startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_big_post));
		} else {
			startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_small_post));
		}
	}

}
