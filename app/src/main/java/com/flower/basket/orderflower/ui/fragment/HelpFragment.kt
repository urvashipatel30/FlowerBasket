package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.api.AppData
import com.flower.basket.orderflower.api.RetroClient
import com.flower.basket.orderflower.data.preference.AppPreference
import com.flower.basket.orderflower.data.vendor.VendorContactResponse
import com.flower.basket.orderflower.databinding.FragmentHelpBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity
import com.flower.basket.orderflower.utils.NetworkUtils
import com.flower.basket.orderflower.views.dialog.AppAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HelpFragment : ParentFragment() {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentHelpBinding

    private var question = ""

    private var playLink = "https://play.google.com/store/apps/details?id="
    val wpPackage = "com.whatsapp"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = (activity as DashboardActivity)

        getVendorDetail()

        binding.backLayout.ivBackAction.setOnClickListener {
            parentActivity.backToHome()
        }

        binding.tvAppPhoneNumber.setOnClickListener {
            val number = binding.tvAppPhoneNumber.text.toString().replace("+91 ", "91")
            openWhatsApp(number)
        }

        binding.tvVendorPhoneNumber.setOnClickListener {
            val number = binding.tvVendorPhoneNumber.text.toString().replace("+91 ", "91")
            openWhatsApp(number)
        }

        binding.tvAppEmailID.setOnClickListener {
            val email = binding.tvAppEmailID.text.toString()
            parentActivity.openEmail(email)
        }

        binding.tvVendorEmailID.setOnClickListener {
            val email = binding.tvVendorEmailID.text.toString()
            parentActivity.openEmail(email)
        }

//        binding.btnPost.setOnClickListener {
//            question = binding.edtQuestion.text.toString()
//
//            if (!(parentActivity.isValidField(question))) {
//                parentActivity.showDialog(activity, msg = getString(R.string.error_question_empty))
//                binding.edtQuestion.requestFocus()
//            } else {
//                postQuestion()
//            }
//        }
        return binding.root
    }

    private fun getVendorDetail() {

        if (NetworkUtils.isNetworkAvailable(activity)) {
            parentActivity.showLoader(activity)

            val userDetails = AppPreference(activity).getUserDetails()

            RetroClient.apiService.getVendorContact("${AppData.vendorURL}/${userDetails?.communityId!!}")
                .enqueue(object : Callback<VendorContactResponse> {
                    override fun onResponse(
                        call: Call<VendorContactResponse>,
                        response: Response<VendorContactResponse>
                    ) {
                        parentActivity.dismissLoader()

                        // if response is not successful
                        if (!response.isSuccessful) {
                            binding.llVendorContact.visibility = View.GONE
                            showDialog(
                                parentActivity,
                                dialogType = AppAlertDialog.ERROR_TYPE,
                                msg = response.message() ?: getString(R.string.error_went_wrong)
                            )
                            return
                        }

                        val vendorResponse = response.body()

                        if (vendorResponse != null) {
                            if (vendorResponse.succeeded) {
                                // Handle the retrieved user data
                                val vendorData = vendorResponse.data

                                binding.llVendorContact.visibility = View.VISIBLE
                                binding.apply {
                                    tvVendorName.text = vendorData.userName
                                    tvVendorEmailID.text = vendorData.email
                                    tvVendorPhoneNumber.text =
                                        vendorData.mobileNumber.replace("+91", "+91 ")
                                }

                            } else {
                                showDialog(
                                    parentActivity,
                                    dialogType = AppAlertDialog.ERROR_TYPE,
                                    title = getString(R.string.failed),
                                    msg = vendorResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<VendorContactResponse>, t: Throwable) {
                        parentActivity.dismissLoader()

                        binding.llVendorContact.visibility = View.GONE
                        showDialog(
                            parentActivity,
                            dialogType = AppAlertDialog.ERROR_TYPE,
                            msg = t.message ?: getString(R.string.error_went_wrong)
                        )
                    }
                })
        } else {
            binding.llVendorContact.visibility = View.GONE
            showDialog(
                parentActivity,
                dialogType = AppAlertDialog.ERROR_TYPE,
                title = getString(R.string.error_no_internet),
                msg = getString(R.string.error_internet_msg)
            )
        }
    }

    private fun openWhatsApp(num: String) {
        val isAppInstalled = appInstalledOrNot(wpPackage)
        if (isAppInstalled) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$num"))
            startActivity(intent)
        } else {
            openPlayStore(wpPackage)
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = requireActivity().packageManager
        return try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openPlayStore(packageName: String) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "market://details?id=$packageName"
                    )
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(playLink + packageName)))
        }
    }

//    private fun openWhatsApp(number: String) {
//        val smsNumber = "91$number" //without '+'
//        try {
//            val sendIntent = Intent("android.intent.action.MAIN")
//            sendIntent.action = Intent.ACTION_SEND
//            sendIntent.type = "text/plain"
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "")
//            sendIntent.putExtra("jid", "$smsNumber@s.whatsapp.net")
//            sendIntent.setPackage("com.whatsapp")
//            startActivity(sendIntent)
//        } catch (e: Exception) {
//            Toast.makeText(activity, "Error\n$e", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun postQuestion() {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ksatyagupta@yahoo.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        emailIntent.putExtra(Intent.EXTRA_TEXT, question)
        emailIntent.selector = selectorIntent
        startActivity(Intent.createChooser(emailIntent, "Send mail"))
    }
}