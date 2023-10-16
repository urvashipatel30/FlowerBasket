package com.flower.basket.orderflower.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.flower.basket.orderflower.R

class ThemeEditView : EditText {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            val typeface = ResourcesCompat.getFont(context, R.font.pangram_regular)
            setTypeface(typeface)
        }
    }
}