package com.flower.basket.orderflower.views.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.Transformation
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.flower.basket.orderflower.R


class AppAlertDialog(val activity: Activity, alertType: Int) : Dialog(
    activity, R.style.alert_dialog
), View.OnClickListener {

    //    private lateinit var activity: Activity
    private var mDialogView: View? = null
    private val mModalInAnim: AnimationSet
    private val mModalOutAnim: AnimationSet
    private val mOverlayOutAnim: Animation
    private val mErrorInAnim: Animation
    private val mErrorXInAnim: AnimationSet
    private val mSuccessLayoutAnimSet: AnimationSet
    private val mSuccessBowAnim: Animation
    private var mTitleTextView: TextView? = null
    private var mContentTextView: TextView? = null

    var titleText: String? = null
        private set
    var contentText: String? = null
        private set
    var isShowCancelButton = false
        private set
    var isShowContentText = false
        private set
    var cancelText: String? = null
        private set
    var confirmText: String? = null
        private set
    var alertType: Int
        private set

    private lateinit var mErrorFrame: FrameLayout
    private lateinit var mSuccessFrame: FrameLayout
    private var mProgressFrame: FrameLayout? = null
    private var mSuccessTick: SuccessTickView? = null
    private var mErrorX: ImageView? = null
    private var mSuccessLeftMask: View? = null
    private var mSuccessRightMask: View? = null
    private var mCustomImgDrawable: Drawable? = null
    private var mCustomImage: ImageView? = null

    private var mConfirmButton: Button? = null
    private var mCancelButton: Button? = null

    //    private ProgressHelper mProgressHelper;
    private var mWarningFrame: FrameLayout? = null

    private var mCancelClickListener: OnDialogClickListener? = null

    //    private var mConfirmClickListener: OnSweetClickListener? = null
//    private var mConfirmClickListener: (appAlertDialog: AppAlertDialog) -> Unit = {_ -> }
    private var mConfirmClickListener: ((AppAlertDialog) -> Unit)? = null

    private var mCloseFromCancel = false
    private var isDialogCancelable = false
    private var dialogLayout = -1

    interface OnDialogClickListener {
        fun onClick(appAlertDialog: AppAlertDialog)
    }

    constructor(activity: Activity) : this(activity, NORMAL_TYPE) {
//        this.activity = activity
    }

    constructor(activity: Activity, isNative: Boolean, layout: Int) : this(activity, NORMAL_TYPE) {
//        this.activity = activity
        dialogLayout = layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (dialogLayout != -1) setContentView(dialogLayout) else setContentView(R.layout.dialog_alert)

        mDialogView = window!!.decorView.findViewById(android.R.id.content)
        mTitleTextView = findViewById(R.id.title_text)
        mContentTextView = findViewById(R.id.content_text)
        mErrorFrame = findViewById(R.id.error_frame)
        mErrorX = mErrorFrame.findViewById(R.id.error_x)
        mSuccessFrame = findViewById(R.id.success_frame)
        mProgressFrame = findViewById(R.id.progress_dialog)
        mSuccessTick = mSuccessFrame.findViewById(R.id.success_tick)
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left)
        mSuccessRightMask =
            mSuccessFrame.findViewById(R.id.mask_right)
        mCustomImage = findViewById(R.id.custom_image)
        mWarningFrame = findViewById(R.id.warning_frame)
        mConfirmButton = findViewById(R.id.confirm_button)
        mCancelButton = findViewById(R.id.cancel_button)
//        mProgressHelper.setProgressWheel((ProgressWheel)findViewById(R.id.progressWheel));

        mConfirmButton?.setOnClickListener(this)
        mCancelButton?.setOnClickListener(this)

        setTitleText(titleText)
        setContentText(contentText)
        setCancelText(cancelText)
        setConfirmText(confirmText)
        changeAlertType(alertType, true)

        val wlp = window!!.attributes
        wlp.width = (activity.resources.displayMetrics.widthPixels * 0.85).toInt()
        window!!.attributes = wlp
    }

    private fun restore() {
        mCustomImage!!.visibility = View.GONE
        mErrorFrame!!.visibility = View.GONE
        mSuccessFrame!!.visibility = View.GONE
        mWarningFrame!!.visibility = View.GONE
        mProgressFrame!!.visibility = View.GONE
        mConfirmButton!!.visibility = View.VISIBLE
        mConfirmButton!!.setBackgroundResource(R.drawable.dialog_button_background)
        mErrorFrame!!.clearAnimation()
        mErrorX!!.clearAnimation()
        mSuccessTick!!.clearAnimation()
        mSuccessLeftMask!!.clearAnimation()
        mSuccessRightMask!!.clearAnimation()
    }

    private fun playAnimation() {
        if (alertType == ERROR_TYPE) {
            mErrorFrame!!.startAnimation(mErrorInAnim)
            mErrorX!!.startAnimation(mErrorXInAnim)
        } else if (alertType == SUCCESS_TYPE) {
            mSuccessTick!!.startTickAnim()
            mSuccessRightMask!!.startAnimation(mSuccessBowAnim)
        }
    }

    private fun changeAlertType(alertType: Int, fromCreate: Boolean) {
        this.alertType = alertType
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore()
            }
            when (this.alertType) {
                ERROR_TYPE -> mErrorFrame!!.visibility = View.VISIBLE
                SUCCESS_TYPE -> {
                    mSuccessFrame!!.visibility = View.VISIBLE
                    // initial rotate layout of success mask
                    mSuccessLeftMask!!.startAnimation(mSuccessLayoutAnimSet.animations[0])
                    mSuccessRightMask!!.startAnimation(mSuccessLayoutAnimSet.animations[1])
                }

                WARNING_TYPE -> {
                    mConfirmButton?.setBackgroundResource(R.drawable.red_button_background)
                    mWarningFrame!!.visibility = View.VISIBLE
                }

                CUSTOM_IMAGE_TYPE -> setCustomImage(mCustomImgDrawable)
                PROGRESS_TYPE -> {
                    mProgressFrame!!.visibility = View.VISIBLE
                    mConfirmButton?.visibility = View.GONE
                }
            }
            if (!fromCreate) {
                playAnimation()
            }
        }
    }

    fun changeAlertType(alertType: Int) {
        changeAlertType(alertType, false)
    }

    fun setTitleText(text: String?): AppAlertDialog {
        titleText = text
        if (mTitleTextView != null && titleText != null) {
            mTitleTextView!!.text = titleText
        }
        return this
    }

    fun setDialogCancelable(isCancelable: Boolean): AppAlertDialog {
        isDialogCancelable = isCancelable
        setCancelable(isDialogCancelable)
        setCanceledOnTouchOutside(isDialogCancelable)
        return this
    }

    fun setCustomImage(drawable: Drawable?): AppAlertDialog {
        mCustomImgDrawable = drawable
        if (mCustomImage != null && mCustomImgDrawable != null) {
            mCustomImage!!.visibility = View.VISIBLE
            mCustomImage!!.setImageDrawable(mCustomImgDrawable)
        }
        return this
    }

    fun setCustomImage(resourceId: Int): AppAlertDialog {
        return setCustomImage(context.resources.getDrawable(resourceId))
    }

    fun setContentText(text: String?): AppAlertDialog {
        contentText = text
        if (mContentTextView != null && contentText != null) {
            showContentText(true)
            mContentTextView!!.text = contentText
        }
        return this
    }

    fun showCancelButton(isShow: Boolean): AppAlertDialog {
        isShowCancelButton = isShow
        if (mCancelButton != null) {
            mCancelButton?.visibility =
                if (isShowCancelButton) View.VISIBLE else View.GONE
        }
        return this
    }

    private fun showContentText(isShow: Boolean): AppAlertDialog {
        isShowContentText = isShow
        if (mContentTextView != null) {
            mContentTextView?.visibility =
                if (isShowContentText) View.VISIBLE else View.GONE
        }
        return this
    }

    fun setCancelText(text: String?): AppAlertDialog {
        cancelText = text
        if (mCancelButton != null && cancelText != null) {
            showCancelButton(true)
            mCancelButton?.text = cancelText
        }
        return this
    }

    fun setConfirmText(text: String?): AppAlertDialog {
        confirmText = text
        if (mConfirmButton != null && confirmText != null) {
            mConfirmButton?.text = confirmText
        }
        return this
    }

    fun setCancelClickListener(listener: OnDialogClickListener): AppAlertDialog {
        mCancelClickListener = listener
        return this
    }

    //    fun setConfirmClickListener(listener: OnSweetClickListener): AppAlertDialog {
    fun setConfirmClickListener(listener: ((AppAlertDialog) -> Unit)): AppAlertDialog {
        mConfirmClickListener = listener
        return this
    }

    override fun onStart() {
        mDialogView!!.startAnimation(mModalInAnim)
        playAnimation()
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    override fun cancel() {
        dismissWithAnimation(true)
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    fun dismissWithAnimation() {
        dismissWithAnimation(false)
    }

    private fun dismissWithAnimation(fromCancel: Boolean) {
        mCloseFromCancel = fromCancel
        mConfirmButton!!.startAnimation(mOverlayOutAnim)
        mDialogView!!.startAnimation(mModalOutAnim)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener!!.onClick(this@AppAlertDialog)
            } else {
                dismissWithAnimation()
            }
        } else if (v.id == R.id.confirm_button) {
//                mConfirmClickListener!!.onClick(this@AppAlertDialog)
            mConfirmClickListener?.invoke(this@AppAlertDialog) ?: dismissWithAnimation()
        }
    }

    //    public ProgressHelper getProgressHelper () {
    //        return mProgressHelper;
    //    }
    companion object {
        const val NORMAL_TYPE = 0
        const val ERROR_TYPE = 1
        const val SUCCESS_TYPE = 2
        const val WARNING_TYPE = 3
        const val CUSTOM_IMAGE_TYPE = 4
        const val PROGRESS_TYPE = 5
    }

    init {
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        //        mProgressHelper = new ProgressHelper(context);
        this.alertType = alertType
        mErrorInAnim = OptAnimationLoader.loadAnimation(
            getContext(),
            R.anim.error_frame_in
        )
        mErrorXInAnim =
            OptAnimationLoader.loadAnimation(
                getContext(),
                R.anim.error_x_in
            ) as AnimationSet
        // 2.3.x system don't support alpha-animation on layer-list drawable
        // remove it from animation set
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            val childAnims = mErrorXInAnim.animations
            var idx = 0
            while (idx < childAnims.size) {
                if (childAnims[idx] is AlphaAnimation) {
                    break
                }
                idx++
            }
            if (idx < childAnims.size) {
                childAnims.removeAt(idx)
            }
        }
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(
            getContext(),
            R.anim.success_bow_roate
        )
        mSuccessLayoutAnimSet = OptAnimationLoader.loadAnimation(
            getContext(),
            R.anim.success_mask_layout
        ) as AnimationSet
        mModalInAnim =
            OptAnimationLoader.loadAnimation(
                getContext(),
                R.anim.modal_in
            ) as AnimationSet
        mModalOutAnim =
            OptAnimationLoader.loadAnimation(
                getContext(),
                R.anim.modal_out
            ) as AnimationSet
        mModalOutAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mDialogView!!.visibility = View.GONE
                mDialogView!!.post {
                    if (mCloseFromCancel) {
                        super@AppAlertDialog.cancel()
                    } else {
                        super@AppAlertDialog.dismiss()
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        // dialog overlay fade out
        mOverlayOutAnim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val wlp = window!!.attributes
                wlp.alpha = 1 - interpolatedTime
                window!!.attributes = wlp
            }
        }
        mOverlayOutAnim.setDuration(120)
    }
}