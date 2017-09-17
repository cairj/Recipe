package com.recipe.r.ui.dialog;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.recipe.r.R;

/**
 * 分享弹窗
 */
public class ShareDialog extends Dialog {
	private onClickback callback;

	public ShareDialog(Context context, onClickback callback) {
		this(context, R.layout.dialog_share, R.style.ACPLDialog,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.callback = callback;
		setCanceledOnTouchOutside(true);
		setListener();
	}

	public ShareDialog(final Context context, int layout, int style, int width,
			int height) {
		super(context, style);
		setContentView(layout);
		setCanceledOnTouchOutside(true);
		// 设置属性值
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = width;
		lp.height = height;
		getWindow().setAttributes(lp);
		setListener();
	}

	// 设置点击事件
	private void setListener() {
		findViewById(R.id.tv_wx).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						callback.onShare(1);
						dismiss();
					}
				});
		findViewById(R.id.tv_wxp).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						callback.onShare(2);
						dismiss();
					}
				});
		findViewById(R.id.tv_qq).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						callback.onShare(3);
						dismiss();
					}
				});
		findViewById(R.id.tv_qqkongjian).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						dismiss();
					}
				});
	}

	@Override
	public void show() {
		super.show();
		//设置dialog显示动画
		getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		// 设置显示位置为底部
		getWindow().setGravity(Gravity.BOTTOM);
	}

	public interface onClickback {

		abstract void onShare(int id);
	}
}