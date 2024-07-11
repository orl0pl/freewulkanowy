package io.github.freewulkanowy.ui.modules.homework

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Homework
import io.github.freewulkanowy.databinding.FragmentHomeworkBinding
import io.github.freewulkanowy.ui.base.BaseFragment
import io.github.freewulkanowy.ui.modules.homework.add.HomeworkAddDialog
import io.github.freewulkanowy.ui.modules.homework.details.HomeworkDetailsDialog
import io.github.freewulkanowy.ui.modules.main.MainActivity
import io.github.freewulkanowy.ui.modules.main.MainView
import io.github.freewulkanowy.ui.widgets.DividerItemDecoration
import io.github.freewulkanowy.utils.dpToPx
import io.github.freewulkanowy.utils.getThemeAttrColor
import javax.inject.Inject

@AndroidEntryPoint
class HomeworkFragment : BaseFragment<FragmentHomeworkBinding>(R.layout.fragment_homework),
    HomeworkView, MainView.TitledView, MainView.MainChildView {

    @Inject
    lateinit var presenter: HomeworkPresenter

    @Inject
    lateinit var homeworkAdapter: HomeworkAdapter

    companion object {
        private const val SAVED_DATE_KEY = "CURRENT_DATE"

        fun newInstance() = HomeworkFragment()
    }

    override val titleStringId get() = R.string.homework_title

    override val isViewEmpty get() = homeworkAdapter.items.isEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeworkBinding.bind(view)
        messageContainer = binding.homeworkRecycler
        presenter.onAttachView(this, savedInstanceState?.getLong(SAVED_DATE_KEY))
    }

    override fun initView() {
        homeworkAdapter.onClickListener = presenter::onHomeworkItemSelected

        with(binding.homeworkRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = homeworkAdapter
            addItemDecoration(DividerItemDecoration(context))
        }

        with(binding) {
            homeworkSwipe.setOnRefreshListener(presenter::onSwipeRefresh)
            homeworkSwipe.setColorSchemeColors(requireContext().getThemeAttrColor(R.attr.colorPrimary))
            homeworkSwipe.setProgressBackgroundColorSchemeColor(requireContext().getThemeAttrColor(R.attr.colorSwipeRefresh))
            homeworkErrorRetry.setOnClickListener { presenter.onRetry() }
            homeworkErrorDetails.setOnClickListener { presenter.onDetailsClick() }

            homeworkPreviousButton.setOnClickListener { presenter.onPreviousDay() }
            homeworkNextButton.setOnClickListener { presenter.onNextDay() }

            openAddHomeworkButton.setOnClickListener { presenter.onHomeworkAddButtonClicked() }

            homeworkNavContainer.elevation = requireContext().dpToPx(3f)
        }
    }

    override fun updateData(data: List<HomeworkItem<*>>) {
        with(homeworkAdapter) {
            items = data
            notifyDataSetChanged()
        }
    }

    override fun clearData() {
        with(homeworkAdapter) {
            items = emptyList()
            notifyDataSetChanged()
        }
    }

    override fun updateNavigationWeek(date: String) {
        binding.homeworkNavDate.text = date
    }

    override fun showRefresh(show: Boolean) {
        binding.homeworkSwipe.isRefreshing = show
    }

    override fun showEmpty(show: Boolean) {
        binding.homeworkEmpty.visibility = if (show) VISIBLE else GONE
    }

    override fun showErrorView(show: Boolean) {
        binding.homeworkError.visibility = if (show) VISIBLE else GONE
    }

    override fun setErrorDetails(message: String) {
        binding.homeworkErrorMessage.text = message
    }

    override fun showProgress(show: Boolean) {
        binding.homeworkProgress.visibility = if (show) VISIBLE else GONE
    }

    override fun enableSwipe(enable: Boolean) {
        binding.homeworkSwipe.isEnabled = enable
    }

    override fun showContent(show: Boolean) {
        binding.homeworkRecycler.visibility = if (show) VISIBLE else GONE
    }

    override fun showPreButton(show: Boolean) {
        binding.homeworkPreviousButton.visibility = if (show) VISIBLE else View.INVISIBLE
    }

    override fun showNextButton(show: Boolean) {
        binding.homeworkNextButton.visibility = if (show) VISIBLE else View.INVISIBLE
    }

    override fun showHomeworkDialog(homework: Homework) {
        (activity as? MainActivity)?.showDialogFragment(HomeworkDetailsDialog.newInstance(homework))
    }

    override fun showAddHomeworkDialog() {
        (activity as? MainActivity)?.showDialogFragment(HomeworkAddDialog())
    }

    override fun onFragmentReselected() {
        if (::presenter.isInitialized) presenter.onViewReselected()
    }

    override fun resetView() {
        binding.homeworkRecycler.smoothScrollToPosition(0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SAVED_DATE_KEY, presenter.currentDate.toEpochDay())
    }

    override fun onDestroyView() {
        presenter.onDetachView()
        super.onDestroyView()
    }
}
