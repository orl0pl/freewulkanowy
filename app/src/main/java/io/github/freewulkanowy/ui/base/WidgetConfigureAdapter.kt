package io.github.freewulkanowy.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.db.entities.StudentWithSemesters
import io.github.freewulkanowy.databinding.ItemAccountBinding
import io.github.freewulkanowy.utils.createNameInitialsDrawable
import io.github.freewulkanowy.utils.getThemeAttrColor
import io.github.freewulkanowy.utils.nickOrName
import javax.inject.Inject

class WidgetConfigureAdapter @Inject constructor() :
    RecyclerView.Adapter<WidgetConfigureAdapter.ItemViewHolder>() {

    var items = emptyList<StudentWithSemesters>()

    var selectedId = -1L

    var onClickListener: (Student) -> Unit = {}

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val (student, semesters) = items[position]
        val semester = semesters.maxByOrNull { it.semesterId }
        val context = holder.binding.root.context
        val checkBackgroundColor = context.getThemeAttrColor(R.attr.colorSurface)
        val avatar = context.createNameInitialsDrawable(student.nickOrName, student.avatarColor)
        val isDuplicatedStudent = items.filter {
            val studentToCompare = it.student

            studentToCompare.studentId == student.studentId
                && studentToCompare.schoolSymbol == student.schoolSymbol
                && studentToCompare.symbol == student.symbol
        }.size > 1

        with(holder.binding) {
            accountItemName.text = "${student.nickOrName} ${semester?.diaryName.orEmpty()}"
            accountItemSchool.text = student.schoolName
            accountItemImage.setImageDrawable(avatar)

            with(accountItemAccountType) {
                setText(if (student.isParent) R.string.account_type_parent else R.string.account_type_student)
                isVisible = isDuplicatedStudent
            }

            with(accountItemCheck) {
                isVisible = student.id == selectedId
                borderColor = checkBackgroundColor
                circleColor = checkBackgroundColor
            }

            root.setOnClickListener { onClickListener(student) }
        }
    }

    class ItemViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)
}
