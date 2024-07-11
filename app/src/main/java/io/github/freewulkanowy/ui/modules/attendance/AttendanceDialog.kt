package io.github.freewulkanowy.ui.modules.attendance

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Attendance
import io.github.freewulkanowy.databinding.DialogAttendanceBinding
import io.github.freewulkanowy.ui.base.BaseDialogFragment
import io.github.freewulkanowy.utils.descriptionRes
import io.github.freewulkanowy.utils.getThemeAttrColor
import io.github.freewulkanowy.utils.serializable
import io.github.freewulkanowy.utils.toFormattedString

@AndroidEntryPoint
class AttendanceDialog : BaseDialogFragment<DialogAttendanceBinding>() {

    private lateinit var attendance: Attendance

    companion object {

        private const val ARGUMENT_KEY = "Item"

        fun newInstance(exam: Attendance) = AttendanceDialog().apply {
            arguments = bundleOf(ARGUMENT_KEY to exam)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attendance = requireArguments().serializable(ARGUMENT_KEY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme)
            .setView(DialogAttendanceBinding.inflate(layoutInflater).apply { binding = this }.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            attendanceDialogSubjectValue.text = attendance.subject
            attendanceDialogDescriptionValue.setText(attendance.descriptionRes)
            attendanceDialogDescriptionValue.setTextColor(
                root.context.getThemeAttrColor(
                    when {
                        attendance.absence && !attendance.excused -> R.attr.colorAttendanceAbsence
                        attendance.lateness && !attendance.excused -> R.attr.colorAttendanceLateness
                        else -> android.R.attr.textColorSecondary
                    }
                )
            )

            attendanceDialogDateValue.text = attendance.date.toFormattedString()
            attendanceDialogNumberValue.text = attendance.number.toString()
            attendanceDialogClose.setOnClickListener { dismiss() }
        }
    }
}
