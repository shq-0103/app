package com.shq.movies.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.shq.movies.R;
import com.shq.movies.aop.SingleClick;
import com.shq.movies.helper.InputTextHelper;
import com.shq.movies.http.model.HttpData;
import com.shq.movies.http.request.GetCodeApi;
import com.shq.movies.http.request.VerifyCodeApi;

public final class ResetPasswordDialog {

    public static final class Builder
            extends UIDialog.Builder<ResetPasswordDialog.Builder> {

        private final EditText et_old_password;
        private final EditText et_new_password1;
        private final EditText et_new_password2;

        private ResetPasswordDialog.OnListener mListener;


        public Builder(Context context) {
            super(context);
            setTitle(R.string.setting_password);
            setCustomView(R.layout.dialog_change_password);
            et_old_password = findViewById(R.id.et_password_old_password);
            et_new_password1 = findViewById(R.id.et_password_reset_password1);
            et_new_password2 = findViewById(R.id.et_password_reset_password2);
            InputTextHelper.with(getActivity())
                    .addView(et_new_password1)
                    .addView(et_new_password2)
                    .addView(et_old_password)
                    .setMain(findViewById(R.id.tv_ui_confirm))
                    .build();
        }


        public ResetPasswordDialog.Builder setListener(ResetPasswordDialog.OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ui_confirm:
                    if (!et_new_password1.getText().toString().equals(et_new_password2.getText().toString())) {
                        ToastUtils.show(R.string.common_password_input_unlike);
                        return;
                    }
                    autoDismiss();
                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog, String phone, String code);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}
