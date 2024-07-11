package io.github.freewulkanowy.ui.modules.notifications

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.databinding.FragmentNotificationsBinding
import io.github.freewulkanowy.ui.base.BaseFragment
import io.github.freewulkanowy.ui.modules.login.LoginActivity
import io.github.freewulkanowy.utils.openNotificationSettings

@AndroidEntryPoint
class NotificationsFragment :
    BaseFragment<FragmentNotificationsBinding>(R.layout.fragment_notifications) {

    private val permission = "android.permission.POST_NOTIFICATIONS"

    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) {
        if (it) {
            navigateToFinish()
        } else showSettingsDialog()
    }

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationsBinding.bind(view)
        initView()
    }

    private fun initView() {
        with(binding) {
            notificationsSkip.setOnClickListener { navigateToFinish() }
            notificationsEnable.setOnClickListener { requestPermission() }
        }
    }

    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.notifications_header_title)
            .setMessage(R.string.notifications_header_description)
            .setNegativeButton(R.string.notifications_skip) { dialog, _ ->
                dialog.dismiss()
                navigateToFinish()
            }
            .setPositiveButton(R.string.pref_notification_go_to_settings) { _, _ ->
                requireActivity().openNotificationSettings()
            }
            .show()
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(permission)
    }

    private fun navigateToFinish() {
        (requireActivity() as LoginActivity).navigateToFinish()
    }
}
