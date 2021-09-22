package com.decagon.android.sq007.ui

object ValidateRegistration {


    //Validate Phone Number
    fun validatePhoneNumber(phoneNumber: String): Boolean {
        val pattern = "^(\\+234|234|0)[789]\\d{9}".toRegex()
        return phoneNumber.matches(pattern)
    }


}