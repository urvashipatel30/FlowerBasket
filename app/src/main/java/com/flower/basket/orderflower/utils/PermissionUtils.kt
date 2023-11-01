package com.flower.basket.orderflower.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object PermissionUtils {

    const val IMAGE_PERMISSION = Manifest.permission.READ_MEDIA_IMAGES
    var permissionAllowed: (() -> Unit)? = null

    fun askForStorage(
        activity: Activity,
        onPermissionAllowed: () -> Unit,
        vararg permissions: String
    ) {
        permissionAllowed = onPermissionAllowed

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(activity.applicationContext)
                .withPermissions(
                    *permissions
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                        if (report.areAllPermissionsGranted()) {
                            permissionAllowed?.invoke()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog(activity)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        } else {
            Dexter.withContext(activity.applicationContext)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            permissionAllowed?.invoke()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog(activity)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }

    fun showSettingsDialog(activity: Activity) {
        AppAlertDialog(activity)
            .setTitleText(activity.getString(R.string.dialog_permission_title))
            .setContentText(activity.getString(R.string.dialog_permission_message))
            .setCancelText(activity.getString(R.string.dialog_cancel))
            .setCancelClickListener(object : AppAlertDialog.OnSweetClickListener {
                override fun onClick(appAlertDialog: AppAlertDialog) {
                    appAlertDialog.dismissWithAnimation()
                }
            })
            .setConfirmText(activity.getString(R.string.dialog_settings))
            .setConfirmClickListener { appAlertDialog ->
                appAlertDialog.dismissWithAnimation()
                openSettings(activity)
            }
            .show()
    }

    private fun openSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, 101, null)
    }
}