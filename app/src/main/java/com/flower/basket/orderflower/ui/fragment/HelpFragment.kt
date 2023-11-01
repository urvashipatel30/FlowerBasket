package com.flower.basket.orderflower.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.databinding.FragmentHelpBinding
import com.flower.basket.orderflower.ui.activity.DashboardActivity

class HelpFragment : Fragment() {

    private lateinit var activity: Activity
    private lateinit var parentActivity: DashboardActivity
    private lateinit var binding: FragmentHelpBinding

    private var question = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(inflater, container, false)

        activity = requireActivity()
        parentActivity = (activity as DashboardActivity)

        binding.btnPost.setOnClickListener {
            question = binding.edtQuestion.text.toString()

            if (!(parentActivity.isValidField(question))) {
                parentActivity.showDialog(activity, msg = getString(R.string.error_question_empty))
                binding.edtQuestion.requestFocus()
            } else {
                postQuestion()
            }
        }
        return binding.root
    }

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