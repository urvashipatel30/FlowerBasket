package com.flower.basket.orderflower.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.flower.basket.orderflower.R

class ThemeBoldText : AppCompatTextView {
    constructor(mContext: Context?) : super(mContext!!) {
        setTypeface()
    }

    constructor(mContext: Context?, set: AttributeSet?) : super(
        mContext!!, set
    ) {
        setTypeface()
    }

    constructor(mContext: Context?, set: AttributeSet?, defaultStyle: Int) : super(
        mContext!!, set, defaultStyle
    ) {
        setTypeface()
    }

    private fun setTypeface() {
        val typeface = ResourcesCompat.getFont(context, R.font.pangram_bold)
        setTypeface(typeface)
    }
}