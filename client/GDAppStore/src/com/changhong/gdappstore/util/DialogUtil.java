package com.changhong.gdappstore.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

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
	/**
	 * 显示短toast
	 * @param context
	 * @param text
	 */
	public static void showShortToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 显示长toast
	 * @param context
	 * @param text
	 */
	public static void showLongToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static Dialog showAlertDialog(Context context, String title, String content,
			final DialogBtnOnClickListener listener) {
		final Dialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(content)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (listener != null) {
							listener.onSubmit(new DialogMessage(dialog));
						}
					}

				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (listener != null) {
							listener.onCancel(new DialogMessage(dialog));
						}
					}
				}).create();
		alertDialog.show();
		return alertDialog;
	}

}
