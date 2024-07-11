package io.github.freewulkanowy.ui.modules.timetable

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Timetable
import io.github.freewulkanowy.databinding.DialogTimetableBinding
import io.github.freewulkanowy.ui.base.BaseDialogFragment
import io.github.freewulkanowy.utils.*
import java.time.Instant

@AndroidEntryPoint
class TimetableDialog : BaseDialogFragment<DialogTimetableBinding>() {

    private lateinit var lesson: Timetable

    companion object {

        private const val ARGUMENT_KEY = "Item"

        fun newInstance(lesson: Timetable) = TimetableDialog().apply {
            arguments = bundleOf(ARGUMENT_KEY to lesson)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lesson = requireArguments().serializable(ARGUMENT_KEY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme)
            .setView(DialogTimetableBinding.inflate(layoutInflater).apply { binding = this }.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(lesson) {
            setInfo(info, canceled, changes)
            setSubject(subject, subjectOld)
            setTeacher(teacher, teacherOld)
            setGroup(group)
            setRoom(room, roomOld)
            setTime(start, end)
        }

        binding.timetableDialogClose.setOnClickListener { dismiss() }
    }

    private fun setSubject(subject: String, subjectOld: String) {
        with(binding) {
            timetableDialogLessonValue.text = subject
            if (subjectOld.isNotBlank() && subjectOld != subject) {
                timetableDialogLessonValue.run {
                    paintFlags = paintFlags or STRIKE_THRU_TEXT_FLAG
                    text = subjectOld
                }
                timetableDialogLessonNewValue.run {
                    visibility = VISIBLE
                    text = subject
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setInfo(info: String, canceled: Boolean, changes: Boolean) {
        with(binding) {
            when {
                info.isNotBlank() -> {
                    if (canceled) {
                        timetableDialogChangesTitle.setTextColor(
                            requireContext().getThemeAttrColor(
                                R.attr.colorTimetableCanceled
                            )
                        )
                        timetableDialogChangesValue.setTextColor(
                            requireContext().getThemeAttrColor(
                                R.attr.colorTimetableCanceled
                            )
                        )
                    } else {
                        timetableDialogChangesTitle.setTextColor(
                            requireContext().getThemeAttrColor(
                                R.attr.colorTimetableChange
                            )
                        )
                        timetableDialogChangesValue.setTextColor(
                            requireContext().getThemeAttrColor(
                                R.attr.colorTimetableChange
                            )
                        )
                    }

                    timetableDialogChangesValue.text = when {
                        canceled && !changes -> "Lekcja odwołana: $info"
                        else -> info.capitalise()
                    }
                }
                else -> {
                    timetableDialogChangesTitle.visibility = GONE
                    timetableDialogChangesValue.visibility = GONE
                }
            }
        }
    }

    private fun setTeacher(teacher: String, teacherOld: String) {
        with(binding) {
            when {
                teacherOld.isNotBlank() && teacherOld != teacher -> {
                    timetableDialogTeacherValue.run {
                        visibility = VISIBLE
                        paintFlags = paintFlags or STRIKE_THRU_TEXT_FLAG
                        text = teacherOld
                    }
                    if (teacher.isNotBlank()) {
                        timetableDialogTeacherNewValue.run {
                            visibility = VISIBLE
                            text = teacher
                        }
                    }
                }
                teacherOld.isNotBlank() && teacherOld == teacher -> {
                    timetableDialogTeacherValue.run {
                        visibility = GONE
                    }
                    timetableDialogTeacherNewValue.run {
                        visibility = VISIBLE
                        text = teacher
                    }
                }
                teacher.isNotBlank() -> timetableDialogTeacherValue.text = teacher
                else -> {
                    timetableDialogTeacherTitle.visibility = GONE
                    timetableDialogTeacherValue.visibility = GONE
                }
            }
        }
    }

    private fun setGroup(group: String) {
        with(binding) {
            when {
                group.isNotBlank() -> timetableDialogGroupValue.text = group
                else -> {
                    timetableDialogGroupTitle.visibility = GONE
                    timetableDialogGroupValue.visibility = GONE
                }
            }
        }
    }

    private fun setRoom(room: String, roomOld: String) {
        with(binding) {
            when {
                roomOld.isNotBlank() && roomOld != room -> {
                    timetableDialogRoomValue.run {
                        visibility = VISIBLE
                        paintFlags = paintFlags or STRIKE_THRU_TEXT_FLAG
                        text = roomOld
                    }
                    if (room.isNotBlank()) {
                        timetableDialogRoomNewValue.run {
                            visibility = VISIBLE
                            text = room
                        }
                    }
                }
                room.isNotBlank() -> timetableDialogRoomValue.text = room
                else -> {
                    timetableDialogRoomTitle.visibility = GONE
                    timetableDialogRoomValue.visibility = GONE
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTime(start: Instant, end: Instant) {
        binding.timetableDialogTimeValue.text =
            "${start.toFormattedString("HH:mm")} - ${end.toFormattedString("HH:mm")}"
    }
}
