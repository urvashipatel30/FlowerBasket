package com.flower.basket.orderflower.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flower.basket.orderflower.api.Event
import com.flower.basket.orderflower.api.Resource
import com.flower.basket.orderflower.repository.UserFirebaseRepository

class LoginFirebaseViewModel : ViewModel() {
    private val userFirebaseRepository = UserFirebaseRepository()

    private val _sendOTPLiveData = MutableLiveData<Event<Resource<Boolean>>>()

    private val _signUpWithEmailAndPasswordLiveData = MutableLiveData<Event<Resource<Boolean>>>()
    val signUpWithEmailAndPassword: LiveData<Event<Resource<Boolean>>> =
        _signUpWithEmailAndPasswordLiveData

    private val _verifyEmailAndRegister = MutableLiveData<Event<Resource<Boolean>>>()
    val verifyEmailAndRegister: LiveData<Event<Resource<Boolean>>> =
        _verifyEmailAndRegister

    fun signUpWithEmailAndPassword(email: String, password: String) {
        _signUpWithEmailAndPasswordLiveData.value = Event(Resource.Loading())

        userFirebaseRepository.signUpWithEmailAndPassword(email, password) { success, errorMessage ->
            if (success) {
                _signUpWithEmailAndPasswordLiveData.value = Event(Resource.Success(true))
            } else {
                _signUpWithEmailAndPasswordLiveData.value =
                    Event(Resource.Error(errorMessage ?: "Email verification sent failed"))
            }
        }
    }

    fun verifyEmailAndRegister(
        userId: String,
        userType: Int,
        name: String,
        email: String,
        password: String,
        community: String,
        block: String,
        flat: String,
        activationDate: String
    ) {
        _verifyEmailAndRegister.value = Event(Resource.Loading())

        userFirebaseRepository.checkEmailVerificationStatus(
            userId,
            userType,
            name,
            email,
            password,
            community,
            block,
            flat,
            activationDate
        ) { success, errorMessage ->
            if (success) {
                _verifyEmailAndRegister.value = Event(Resource.Success(true))
            } else {
                _verifyEmailAndRegister.value =
                    Event(Resource.Error(errorMessage ?: "User registration failed"))
            }
        }
    }

    // Function to verify OTP and register the user
    fun verifyOTPAndRegister(
        verificationId: String,
        userType: String,
        name: String,
        email: String,
        password: String,
        community: String,
        block: String,
        flat: String
    ): LiveData<Event<Resource<Boolean>>> {
        val liveData = MutableLiveData<Event<Resource<Boolean>>>()


        return liveData
    }

    // Function to send OTP
    fun sendOTP(activity: Activity, mobileNumber: String) {
        _sendOTPLiveData.value = Event(Resource.Loading())

        userFirebaseRepository.sendOTP(
            activity,
            mobileNumber,
            onVerificationCompleted = { smsCode ->
                // Handle automatic verification (if needed)
                Log.e("sendOTP: ", "smsCode => $smsCode")
            },
            onVerificationFailed = { e ->
                Log.e("sendOTP: ", "e => ${e.message}")
                _sendOTPLiveData.value = Event(Resource.Error("OTP verification failed"))
            },
            onCodeSent = { verificationId, token ->
                // You can also store verificationId if needed
                Log.e("sendOTP: ", "verificationId => $verificationId, token => $token")
                _sendOTPLiveData.value = Event(Resource.Success(true))
            }
        )
    }

    fun getSendOTPLiveData(): LiveData<Event<Resource<Boolean>>> {
        return _sendOTPLiveData
    }
}