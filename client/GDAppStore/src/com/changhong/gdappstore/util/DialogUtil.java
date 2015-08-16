package com.changhong.gdappstore.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.changhong.gdappstore.R;

public class DialogUtil {
	public static interface DialogBtnOnClickListener {

		public void onSubmit(DialogMessage dialogMessage);

		public void onCancel(DialogMessage dialogMessage);

	}

	public static class DialogMessage {
		public DialogInterface dialogInterface;// 弹出的对话框对象
		public String msg;// 文本信息

		public DialogMessage() {
		}

		public DialogMessage(DialogInterface dialogInterface) {
			this.dialogInterface = dialogInterface;
		}
	}

	public static Dialog showAlertDialog(Context context, String title, String content,
			final DialogBtnOnClickListener listener) {
		final Dialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(content)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (listener!=null) {
							listener.onSubmit(new DialogMessage(dialog));
						}
					}

				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (listener!=null) {
							listener.onCancel(new DialogMessage(dialog));
						}
					}
				}).create();
		alertDialog.show();
		return alertDialog;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
