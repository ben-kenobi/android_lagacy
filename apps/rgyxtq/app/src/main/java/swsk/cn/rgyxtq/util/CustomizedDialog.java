package swsk.cn.rgyxtq.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import swsk.cn.rgyxtq.R;

public class CustomizedDialog extends Dialog {
	public TextView title, message;

	public Button positiveButton, negativeButton;

	public CheckedTextView checkedText1;


	public static final int resId = R.layout.dialog_customized;


	public CustomizedDialog(Context context) {
		super(context);
	}

	CustomizedDialog(Context context, boolean cancelable,
					 OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public CustomizedDialog(Context context, int theme) {
		super(context, theme);
	}


	public boolean getCheckedTextStatus() {
		return checkedText1.isChecked();
	}

	// positiveButton
	public CustomizedDialog setPositiveButton(String name,
			final OnClickListener listener) {
		positiveButton.setVisibility(View.VISIBLE);
		positiveButton.setText(name);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
				if (listener != null)
					listener.onClick(CustomizedDialog.this,
							DialogInterface.BUTTON_POSITIVE);
			}
		});
		return this;
	}

	// NegativeButton
	public CustomizedDialog setNegativeButton(String name,
			final OnClickListener listener) {
		negativeButton.setVisibility(View.VISIBLE);
		negativeButton.setText(name);
		negativeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
				if (listener != null)
					listener.onClick(CustomizedDialog.this,
							DialogInterface.BUTTON_NEGATIVE);
			}
		});
		return this;
	}

	// DefaultDialog
	public static CustomizedDialog initDialog(String title, String message,
			String checkedText, float fontSize, Context context) {
		View contentView = LayoutInflater.from(context).inflate(resId, null,
				false);
		CustomizedDialog dialog = initDialog(contentView, true, false, context,
				-1);
		dialog.title = ((TextView) contentView.findViewById(R.id.title));
		dialog.message = ((TextView) contentView.findViewById(R.id.message));
		dialog.positiveButton = ((Button) contentView
				.findViewById(R.id.positiveButton));
		dialog.negativeButton = (Button) contentView
				.findViewById(R.id.negativeButton);
		dialog.checkedText1 = (CheckedTextView) contentView
				.findViewById(R.id.checkedTextView1);
		dialog.title.setText(title);
		if (fontSize != 0)
			dialog.message.setTextSize(fontSize);
		dialog.message.setText(message);
		if (checkedText != null) {
			dialog.checkedText1.setText(checkedText);
			CommonUtils
					.setOnClickListenerForCheckedTextView(dialog.checkedText1);
		} else {
			dialog.checkedText1.setVisibility(View.GONE);
		}
		dialog.positiveButton.setVisibility(View.GONE);
		dialog.negativeButton.setVisibility(View.GONE);
		return dialog;
	}




	public static CustomizedDialog initDialog(View contentView,
			boolean cancelable, boolean cancelOnTouchOutside, Context context,
			float width) {
		CustomizedDialog dialog = new CustomizedDialog(context,
				R.style.dialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
		// dialog.setContentView(contentView);
		dialog.getWindow().setContentView(contentView,
				new LayoutParams((int) width, -1));
		return dialog;
	}



	/**
	 *
	 * @author Administrator
	 * @name Builder
	 */
	public static class Builder {
		Context context;
		String title, message, positiveButton, negativeButton, checkedText;
		float fontSize;
		OnClickListener positiveButtonListener,
				negativeButtonListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setCheckedText(String text) {
			this.checkedText = text;
			return this;
		}

		public Builder setContentFontSize(float size) {
			this.fontSize = size;
			return this;
		}

		public Builder setPositiveButton(String name,
				OnClickListener listener) {
			this.positiveButton = name;
			this.positiveButtonListener = listener;
			return this;
		}

		public Builder setNegativeButton(String name,
				OnClickListener listener) {
			this.negativeButton = name;
			this.negativeButtonListener = listener;
			return this;
		}

		public CustomizedDialog create() {
			final CustomizedDialog dialog = initDialog(title, message,
					checkedText, fontSize, context);
			if (positiveButton != null) {
				dialog.positiveButton.setVisibility(View.VISIBLE);
				dialog.positiveButton.setText(positiveButton);
				dialog.positiveButton
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								if (positiveButtonListener != null)
									positiveButtonListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								dialog.dismiss();
							}
						});
			} else {
				dialog.positiveButton.setVisibility(View.GONE);
			}
			if (negativeButton != null) {
				dialog.negativeButton.setVisibility(View.VISIBLE);
				dialog.negativeButton.setText(negativeButton);
				dialog.negativeButton
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								if (negativeButtonListener != null)
									negativeButtonListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								dialog.dismiss();
							}
						});
			} else {
				dialog.negativeButton.setVisibility(View.GONE);
			}
			LayoutParams lp = dialog.getWindow().getAttributes();
			System.out.println("height=" + lp.height + ",width=" + lp.width
					+ "   @CustomizedDialog");
			return dialog;
		}
	}



}
