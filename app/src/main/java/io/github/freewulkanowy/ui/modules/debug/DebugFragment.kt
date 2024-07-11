package io.github.freewulkanowy.ui.modules.debug

import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.databinding.FragmentDebugBinding
import io.github.freewulkanowy.ui.base.BaseFragment
import io.github.freewulkanowy.ui.modules.debug.logviewer.LogViewerFragment
import io.github.freewulkanowy.ui.modules.debug.notification.NotificationDebugFragment
import io.github.freewulkanowy.ui.modules.main.MainActivity
import io.github.freewulkanowy.ui.modules.main.MainView
import javax.inject.Inject

@AndroidEntryPoint
class DebugFragment : BaseFragment<FragmentDebugBinding>(R.layout.fragment_debug), DebugView,
    MainView.TitledView {

    @Inject
    lateinit var presenter: DebugPresenter

    private val debugAdapter = DebugAdapter()

    override val titleStringId: Int
        get() = R.string.debug_title

    companion object {
        fun newInstance() = DebugFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDebugBinding.bind(view)
        presenter.onAttachView(this)
    }

    override fun initView() {
        debugAdapter.onItemClickListener = presenter::onItemSelect
        with(binding.debugRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = debugAdapter
        }
    }

    override fun setItems(itemList: List<DebugItem>) {
        with(debugAdapter) {
            items = itemList
            notifyDataSetChanged()
        }
    }

    override fun openLogViewer() {
        (activity as? MainActivity)?.pushView(LogViewerFragment.newInstance())
    }

    override fun openNotificationsDebug() {
        (activity as? MainActivity)?.pushView(NotificationDebugFragment.newInstance())
    }

    override fun clearWebkitCookies() {
        CookieManager.getInstance().removeAllCookies(null)
    }

    override fun onDestroyView() {
        presenter.onDetachView()
        super.onDestroyView()
    }
}
