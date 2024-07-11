package io.github.freewulkanowy.ui.modules.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.freewulkanowy.R
import io.github.freewulkanowy.ui.base.BaseActivity
import io.github.freewulkanowy.ui.modules.main.MainView
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(), MainView.TitledView, SettingsView {

    companion object {

        fun newInstance() = SettingsFragment()
    }

    override val titleStringId get() = R.string.settings_title

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.scheme_preferences, rootKey)
        Timber.i("Settings view was initialized")
    }

    override fun showError(text: String, error: Throwable) {}

    override fun showMessage(text: String) {}

    override fun showExpiredCredentialsDialog() {}

    override fun onCaptchaVerificationRequired(url: String?) = Unit

    override fun showDecryptionFailedDialog() {}

    override fun openClearLoginView() {}

    override fun showErrorDetailsDialog(error: Throwable) {}

    override fun showChangePasswordSnackbar(redirectUrl: String) {}

    override fun showAuthDialog() {}
}
