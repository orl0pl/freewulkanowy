package io.github.freewulkanowy.ui.modules.schoolannouncement

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.data.db.entities.SchoolAnnouncement
import io.github.freewulkanowy.databinding.DialogSchoolAnnouncementBinding
import io.github.freewulkanowy.ui.base.BaseDialogFragment
import io.github.freewulkanowy.utils.parseUonetHtml
import io.github.freewulkanowy.utils.serializable
import io.github.freewulkanowy.utils.toFormattedString

@AndroidEntryPoint
class SchoolAnnouncementDialog : BaseDialogFragment<DialogSchoolAnnouncementBinding>() {

    private lateinit var announcement: SchoolAnnouncement

    companion object {

        private const val ARGUMENT_KEY = "item"

        fun newInstance(announcement: SchoolAnnouncement) = SchoolAnnouncementDialog().apply {
            arguments = bundleOf(ARGUMENT_KEY to announcement)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        announcement = requireArguments().serializable(ARGUMENT_KEY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme)
            .setView(
                DialogSchoolAnnouncementBinding.inflate(layoutInflater)
                    .apply { binding = this }.root
            )
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            announcementDialogSubjectValue.text = announcement.subject
            announcementDialogDateValue.text = announcement.date.toFormattedString()
            announcementDialogDescriptionValue.text = announcement.content.parseUonetHtml()

            announcementDialogClose.setOnClickListener { dismiss() }
        }
    }
}
