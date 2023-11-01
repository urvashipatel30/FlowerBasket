package com.flower.basket.orderflower.ui.activity

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.views.dialog.AppAlertDialog

open class ParentActivity : AppCompatActivity() {

    lateinit var loader: Dialog

    open fun showDialog(
        activity: Activity,
        dialogType: Int = AppAlertDialog.NORMAL_TYPE,
        title: String? = null,
        msg: String
    ) {
        AppAlertDialog(activity, dialogType)
            .setTitleText(
                title
                    ?: if (dialogType == AppAlertDialog.ERROR_TYPE) getString(R.string.dialog_error_title)
                    else getString(R.string.dialog_warning)
            )
            .setContentText(msg)
            .setConfirmText(getString(R.string.dialog_ok))
            .setConfirmClickListener { appAlertDialog ->
                appAlertDialog.dismissWithAnimation()
            }
            .show()
    }

    open fun isValidField(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target)
    }

    protected fun isValidMobileNumber(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && target?.length == 10
    }

    open fun showLoader(activity: Activity, title: String = "", msg: String = "") {
//        if (loader == null) {
        loader = createLoader(activity, "")
//        }
        if (!activity.isFinishing && !loader.isShowing) loader.show()
    }

    fun dismissLoader() {
        if (loader != null && loader.isShowing) loader.dismiss()
    }

    protected open fun createLoader(
        activity: Activity,
        msg: String
    ): Dialog {
        val mDialog = Dialog(activity, R.style.DialogTheme)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mInflater = LayoutInflater.from(activity)
        val layout = mInflater.inflate(R.layout.dialog_progress, null)
        mDialog.setContentView(layout)

        val tvContentText = (layout.findViewById<View>(R.id.tvContentText) as TextView)

        if (msg != "") {
            tvContentText.visibility = View.VISIBLE
            tvContentText.text = msg
        } /*else tvContentText.visibility = View.GONE*/

        mDialog.setCancelable(false)
        mDialog.window!!
            .setLayout(
                (activity.resources.displayMetrics.widthPixels * 0.82).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        return mDialog
    }
}