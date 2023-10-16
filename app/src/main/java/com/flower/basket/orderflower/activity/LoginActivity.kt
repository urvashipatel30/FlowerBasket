package com.flower.basket.orderflower.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import com.flower.basket.orderflower.R
import com.flower.basket.orderflower.databinding.ActivityLoginBinding

class LoginActivity : ParentActivity(), OnClickListener {

    private lateinit var activity: Activity

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@LoginActivity

        binding.rbLogin.setTextColor(ContextCompat.getColor(activity, R.color.white))
        binding.rbSignup.setTextColor(ContextCompat.getColor(activity, R.color.unselectedLoginTabColor))

        binding.btnLogin.setOnClickListener(this)

        binding.radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbLogin -> {
                    binding.llLogin.visibility = View.VISIBLE
                    binding.llSignup.visibility = View.GONE
                    binding.rbLogin.setTextColor(ContextCompat.getColor(activity, R.color.white))
                    binding.rbSignup.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.unselectedLoginTabColor
                        )
                    )
                }

                R.id.rbSignup -> {
                    binding.llLogin.visibility = View.GONE
                    binding.llSignup.visibility = View.VISIBLE
                    binding.rbLogin.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.unselectedLoginTabColor
                        )
                    )
                    binding.rbSignup.setTextColor(ContextCompat.getColor(activity, R.color.white))
                }
            }
        }
    }

    override fun onClick(view: View?) {

        when (view) {
            binding.btnLogin -> {
                val intent = Intent(activity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}