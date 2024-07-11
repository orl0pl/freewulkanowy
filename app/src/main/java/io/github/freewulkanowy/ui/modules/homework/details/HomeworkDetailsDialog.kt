package io.github.freewulkanowy.ui.modules.homework.details

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Homework
import io.github.freewulkanowy.databinding.DialogHomeworkBinding
import io.github.freewulkanowy.ui.base.BaseDialogFragment
import io.github.freewulkanowy.utils.openInternetBrowser
import io.github.freewulkanowy.utils.serializable
import javax.inject.Inject

@AndroidEntryPoint
class HomeworkDetailsDialog : BaseDialogFragment<DialogHomeworkBinding>(), HomeworkDetailsView {

    @Inject
    lateinit var presenter: HomeworkDetailsPresenter

    @Inject
    lateinit var detailsAdapter: HomeworkDetailsAdapter

    override val homeworkDeleteSuccess: String
        get() = getString(R.string.homework_delete_success)

    private lateinit var homework: Homework

    companion object {

        private const val ARGUMENT_KEY = "Item"

        fun newInstance(homework: Homework) = HomeworkDetailsDialog().apply {
            arguments = bundleOf(ARGUMENT_KEY to homework)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homework = requireArguments().serializable(ARGUMENT_KEY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme)
            .setView(DialogHomeworkBinding.inflate(layoutInflater).apply { binding = this }.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttachView(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        with(binding) {
            homeworkDialogRead.text =
                view?.context?.getString(if (homework.isDone) R.string.homework_mark_as_undone else R.string.homework_mark_as_done)
            homeworkDialogRead.setOnClickListener { presenter.toggleDone(homework) }
            homeworkDialogClose.setOnClickListener { dismiss() }
        }

        with(binding.homeworkDialogRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = detailsAdapter.apply {
                onAttachmentClickListener = { context.openInternetBrowser(it, ::showMessage) }
                onDeleteClickListener = { homework -> presenter.deleteHomework(homework) }
                homework = this@HomeworkDetailsDialog.homework
            }
        }
    }

    override fun closeDialog() {
        dismiss()
    }

    override fun updateMarkAsDoneLabel(isDone: Boolean) {
        binding.homeworkDialogRead.text =
            view?.context?.getString(if (isDone) R.string.homework_mark_as_undone else R.string.homework_mark_as_done)
    }

    override fun onDestroyView() {
        presenter.onDetachView()
        super.onDestroyView()
    }
}
