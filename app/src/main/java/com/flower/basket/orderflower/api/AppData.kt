package com.flower.basket.orderflower.api

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

    private external fun getKey(): String

    private external fun getIV(): String

    external fun getCommunityURL(): String
//    external fun getRegisterURL(): String
//    external fun getLoginURL(): String
//    external fun getAllUsersURL(): String
//    external fun getUpdateUserURL(): String
//    external fun getChangePasswordURL(): String
//    external fun getAllFlowersURL(): String
//    external fun getAddSubscriptionURL(): String
//    external fun getGetAllSubscriptionsURL(): String
//    external fun getGetSubscriptionURL(): String
//    external fun getUpdateSubscriptionURL(): String
//    external fun getManageVacationModeURL(): String
//    external fun getDeleteSubscriptionURL(): String
//    external fun getGetAllOrdersURL(): String
//    external fun getGenerateOrderURL(): String
//    external fun getUpdateOrderStatusURL(): String
//    external fun getVendorURL(): String
//    external fun getUpdateFlowerURL(): String
//    external fun getAllOrdersURL(): String
//    external fun getTotalFlowersURL(): String
}