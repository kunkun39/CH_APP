package com.changhong.gdappstore.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.view.TextImageButton;

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
	
	public static ProgressDialog showCirculProDialog(Context context,String title,String content,boolean isshow) {
		ProgressDialog progressDialog=new ProgressDialog(context, R.style.Dialog_nowindowbg);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.getWindow().setTitleColor(Color.WHITE);
		progressDialog.setTitle(title);
		progressDialog.setMessage(content);
		progressDialog.setCancelable(false);
		if (isshow) {
			progressDialog.show();
		}
		return progressDialog;
	}
	
	public static Dialog showMyAlertDialog(Context context, String title,
			String content, String positiveBtnName, String negtiveBtnName,
			final DialogBtnOnClickListener listener) {
		final Dialog dialog = new Dialog(context, R.style.Dialog_nowindowbg);

		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_myalert, null);
		dialog.setContentView(view);
		LayoutParams param = dialog.getWindow().getAttributes();
		param.gravity = Gravity.CENTER;
		param.width = (int) context.getResources().getDimension(R.dimen.dialog_width);
		param.height = (int) context.getResources().getDimension(R.dimen.dialog_height);

		TextImageButton bt_submit = (TextImageButton) view.findViewById(R.id.bt_alertdia_submit);
		TextImageButton bt_cancel = (TextImageButton) view.findViewById(R.id.bt_alertdia_cancel);
		if (!TextUtils.isEmpty(positiveBtnName)) {
			bt_submit.setText(positiveBtnName);
		}
		if (!TextUtils.isEmpty(negtiveBtnName)) {
			bt_cancel.setText(negtiveBtnName);
		}
		bt_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onSubmit(new DialogMessage(dialog));
				}
			}
		});
		bt_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onCancel(new DialogMessage(dialog));
				}
			}
		});
		TextView tv_title = (TextView) view
				.findViewById(R.id.tv_alertdia_title);
		TextView tv_content = (TextView) view
				.findViewById(R.id.tv_alertdia_content);

		if (!TextUtils.isEmpty(content)) {
			tv_content.setText(content);
			if (!TextUtils.isEmpty(title)) {
				tv_title.setText(title);
			}
		} else {// 如果内容为空，内容显示标题信息，标题采用默认标题
			if (!TextUtils.isEmpty(title)) {
				tv_content.setText(title);
			}
		}
		bt_submit.requestFocus();
		dialog.getWindow().setAttributes(param);
//		dialog.getWindow()
//				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialog;
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
