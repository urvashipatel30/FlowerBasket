package com.flower.basket.orderflower.ui.fragment

import androidx.fragment.app.Fragment
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.ui.activity.MainActivity
import com.flower.basket.orderflower.views.dialog.AppAlertDialog

open class ParentFragment : Fragment() {

    open fun showDialog(
        activity: DashboardActivity,
        dialogType: Int = AppAlertDialog.NORMAL_TYPE,
        title: String? = null,
        msg: String
    ) {
        activity.showDialog(activity, dialogType, title, msg)
    }
}