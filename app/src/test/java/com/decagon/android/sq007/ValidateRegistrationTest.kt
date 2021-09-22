package com.decagon.android.sq007

import com.decagon.android.sq007.ui.ValidateRegistration
import junit.framework.TestCase
import org.junit.Test

class ValidateRegistrationTest : TestCase() {

    //Mock Input Data
    private val numberIncorrect: String = "0813553775"
    private val numberCorrect: String = "+2348492015369"


    //Test Validate Phone Number Function with a Correct Number
    @Test
    fun testValidatePhoneNumber_isTrue() {
        val result = ValidateRegistration.validatePhoneNumber(numberCorrect)
        assertTrue(result)
    }

    //Test Validate Phone Number Function with a Incorrect Number
    @Test
    fun testValidatePhoneNumber_isFalse() {
        val result = ValidateRegistration.validatePhoneNumber(numberIncorrect)
        assertFalse(result)
    }


}