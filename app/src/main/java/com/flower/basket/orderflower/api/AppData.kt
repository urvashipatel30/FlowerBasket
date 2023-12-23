package com.flower.basket.orderflower.api

import com.flower.basket.orderflower.api.AppData.getData
import com.flower.basket.orderflower.utils.secure.AESHelper.getIV
import com.flower.basket.orderflower.utils.secure.EasyAES

object AppData {
    fun getData(data: String): String {
        val aes = EasyAES(getKey(), 256, getIV())
        return aes.decrypt(data)
    }

    fun scrambleData(data: String): String {
        val aes = EasyAES(getKey(), 256, getIV())
        return aes.encrypt(data)
    }

    val communityURL = getData(getCommunity())
    val registerURL = getData(getRegister())
    val loginURL = getData(getLogin())
    val allUsersURL = getData(getAllUsers())
    val updateUserURL = getData(getUpdateUser())
    val changePasswordURL = getData(getChangePassword())
    val deleteUserURL = getData(getDeleteUser())
    val allFlowersURL = getData(getAllFlowers())
    val addSubscriptionURL = getData(getAddSubscription())
    val allSubscriptionURL = getData(getAllSubscriptions())
    val subscriptionURL = getData(getSubscription())
    val updateSubscriptionURL = getData(getUpdateSubscription())
    val manageVacationModeURL = getData(getManageVacationMode())
    val deleteSubscriptionURL = getData(getDeleteSubscription())
    val allOrderURL = getData(getAllOrders())
    val generateOrderURL = getData(getGenerateOrder())
    val updateOrderStatusURL = getData(getUpdateOrderStatus())
    val vendorURL = getData(getVendor())
    val updateFlowerURL = getData(getUpdateFlower())
    val vendorReportOrdersURL = getData(getReportOrders())
    val totalFlowersURL = getData(getTotalFlowers())

    private external fun getKey(): String

    private external fun getIV(): String

    private external fun getCommunity(): String
    private external fun getRegister(): String
    private external fun getLogin(): String
    private external fun getAllUsers(): String
    private external fun getUpdateUser(): String
    private external fun getDeleteUser(): String
    private external fun getChangePassword(): String
    private external fun getAllFlowers(): String
    private external fun getAddSubscription(): String
    private external fun getAllSubscriptions(): String
    private external fun getSubscription(): String
    private external fun getUpdateSubscription(): String
    private external fun getManageVacationMode(): String
    private external fun getDeleteSubscription(): String
    private external fun getAllOrders(): String
    private external fun getGenerateOrder(): String
    private external fun getUpdateOrderStatus(): String
    private external fun getVendor(): String
    private external fun getUpdateFlower(): String
    private external fun getReportOrders(): String
    private external fun getTotalFlowers(): String
}