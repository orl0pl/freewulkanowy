package io.github.freewulkanowy.ui.modules.login.symbol

import io.github.freewulkanowy.data.db.entities.AdminMessage
import io.github.freewulkanowy.data.pojos.RegisterUser
import io.github.freewulkanowy.ui.base.BaseView
import io.github.freewulkanowy.ui.modules.login.LoginData
import io.github.freewulkanowy.ui.modules.login.support.LoginSupportInfo

interface LoginSymbolView : BaseView {

    val symbolValue: String?

    val symbolNameError: CharSequence?

    fun initView()

    fun setLoginToHeading(login: String)

    fun setErrorSymbolIncorrect()

    fun setErrorSymbolInvalid()

    fun setErrorSymbolDefinitelyInvalid()

    fun setErrorSymbolRequire()

    fun setErrorSymbol(message: String)

    fun clearSymbolError()

    fun clearAndFocusSymbol()

    fun showSoftKeyboard()

    fun hideSoftKeyboard()

    fun showProgress(show: Boolean)

    fun showContent(show: Boolean)

    fun navigateToStudentSelect(loginData: LoginData, registerUser: RegisterUser)

    fun showContact(show: Boolean)

    fun openFaqPage()

    fun openSupportDialog(supportInfo: LoginSupportInfo)

    fun showAdminMessage(adminMessage: AdminMessage?)

    fun openInternetBrowser(url: String)
}
