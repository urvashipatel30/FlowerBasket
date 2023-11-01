package com.flower.basket.orderflower.repository

import android.app.Activity
import android.util.Log
import com.flower.basket.orderflower.data.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class UserFirebaseRepository {

    private val auth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User signed up successfully
                    sendEmailVerification(callback)
                } else {
                    // Handle sign-up failure
                    callback(false, task.exception?.message)
                }
            }
    }

    private fun sendEmailVerification(callback: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Email verification link sent
                    callback(true, null)
                } else {
                    // Handle email verification sending failure
                    callback(false, task.exception?.message)
                }
            }
    }

    fun checkEmailVerificationStatus(
        userId: String,
        userType: Int,
        name: String,
        email: String,
        password: String,
        community: String,
        block: String,
        flat: String,
        activationDate: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser
        user?.reload()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    callback(user.isEmailVerified)

                    Log.e(
                        "checkEmailVerificationStatus: ",
                        "isEmailVerified => ${user.isEmailVerified}"
                    )
                    if (user.isEmailVerified) {
                        registerUser(
                            userId,
                            userType,
                            name,
                            email,
                            password,
                            community,
                            block,
                            flat,
                            activationDate,
                            callback
                        )
                    }
                } else {
                    // Handle reload failure
                    callback(false, task.exception?.message)
                }
            }
    }

    // Function to register the user and store data in Firestore
    private fun registerUser(
        userId: String,
        userType: Int,
        name: String,
        email: String,
        mobileNumber: String,
        community: String,
        block: String,
        flat: String,
        activationDate: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val user = User(
            userId,
            userType,
            name,
            email,
            mobileNumber,
            community,
            block,
            flat,
            activationDate
        )

        usersCollection.document(userId).set(user)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    fun sendOTP(
        activity: Activity,
        mobileNumber: String,
        onVerificationCompleted: (String) -> Unit,
        onVerificationFailed: (FirebaseException) -> Unit,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobileNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    onVerificationCompleted(credential.smsCode ?: "")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onVerificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onCodeSent(verificationId, token)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}