package io.github.freewulkanowy.ui.modules.login.support

import io.github.freewulkanowy.data.pojos.RegisterUser
import io.github.freewulkanowy.ui.modules.login.LoginData
import java.io.Serializable

data class LoginSupportInfo(
    val loginData: LoginData,
    val registerUser: RegisterUser?,
    val lastErrorMessage: String?,
    val enteredSymbol: String?,
) : Serializable
