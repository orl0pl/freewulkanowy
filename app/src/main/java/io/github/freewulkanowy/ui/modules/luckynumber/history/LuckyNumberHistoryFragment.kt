package io.github.freewulkanowy.ui.modules.luckynumber.history

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.LuckyNumber
import io.github.freewulkanowy.databinding.FragmentLuckyNumberHistoryBinding
import io.github.freewulkanowy.ui.base.BaseFragment
import io.github.freewulkanowy.ui.modules.main.MainView
import io.github.freewulkanowy.ui.widgets.DividerItemDecoration
import io.github.freewulkanowy.utils.dpToPx
import io.github.freewulkanowy.utils.firstSchoolDayInSchoolYear
import io.github.freewulkanowy.utils.openMaterialDatePicker
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class LuckyNumberHistoryFragment :
    BaseFragment<FragmentLuckyNumberHistoryBinding>(R.layout.fragment_lucky_number_history),
    LuckyNumberHistoryView,
    MainView.TitledView {

    @Inject
    lateinit var presenter: LuckyNumberHistoryPresenter

    @Inject
    lateinit var luckyNumberHistoryAdapter: LuckyNumberHistoryAdapter

    companion object {
        fun newInstance() = LuckyNumberHistoryFragment()
    }

    override val titleStringId: Int
        get() = R.string.lucky_number_history_title

    override val isViewEmpty get() = luckyNumberHistoryAdapter.items.isEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLuckyNumberHistoryBinding.bind(view)
        messageContainer = binding.luckyNumberHistoryRecycler
        presenter.onAttachView(this)
    }

    override fun initView() {
        with(binding.luckyNumberHistoryRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = luckyNumberHistoryAdapter
            addItemDecoration(DividerItemDecoration(context))
        }

        with(binding) {
            luckyNumberHistoryNavDate.setOnClickListener { presenter.onPickDate() }
            luckyNumberHistoryErrorRetry.setOnClickListener { presenter.onRetry() }
            luckyNumberHistoryErrorDetails.setOnClickListener { presenter.onDetailsClick() }

            luckyNumberHistoryPreviousButton.setOnClickListener { presenter.onPreviousWeek() }
            luckyNumberHistoryNextButton.setOnClickListener { presenter.onNextWeek() }

            luckyNumberHistoryNavContainer.elevation = requireContext().dpToPx(3f)
        }
    }

    override fun updateData(data: List<LuckyNumber>) {
        with(luckyNumberHistoryAdapter) {
            items = data
            notifyDataSetChanged()
        }
    }

    override fun clearData() {
        with(luckyNumberHistoryAdapter) {
            items = emptyList()
            notifyDataSetChanged()
        }
    }

    override fun showEmpty(show: Boolean) {
        binding.luckyNumberHistoryEmpty.visibility = if (show) VISIBLE else GONE
    }

    override fun showErrorView(show: Boolean) {
        binding.luckyNumberHistoryError.visibility = if (show) VISIBLE else GONE
    }

    override fun setErrorDetails(message: String) {
        binding.luckyNumberHistoryErrorMessage.text = message
    }

    override fun updateNavigationWeek(date: String) {
        binding.luckyNumberHistoryNavDate.text = date
    }

    override fun showProgress(show: Boolean) {
        binding.luckyNumberHistoryProgress.visibility = if (show) VISIBLE else GONE
    }

    override fun showPreButton(show: Boolean) {
        binding.luckyNumberHistoryPreviousButton.visibility = if (show) VISIBLE else View.INVISIBLE
    }

    override fun showNextButton(show: Boolean) {
        binding.luckyNumberHistoryNextButton.visibility = if (show) VISIBLE else View.INVISIBLE
    }

    override fun showDatePickerDialog(selectedDate: LocalDate) {
        openMaterialDatePicker(
            selected = selectedDate,
            rangeStart = selectedDate.firstSchoolDayInSchoolYear,
            rangeEnd = LocalDate.now().plusWeeks(1),
            onDateSelected = {
                presenter.onDateSet(it.year, it.monthValue, it.dayOfMonth)
            }
        )
    }

    override fun showContent(show: Boolean) {
        binding.luckyNumberHistoryRecycler.visibility = if (show) VISIBLE else GONE
    }

    override fun onDestroyView() {
        presenter.onDetachView()
        super.onDestroyView()
    }
}
