package io.github.freewulkanowy.ui.modules.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.databinding.FragmentAccountBinding
import io.github.freewulkanowy.ui.base.BaseFragment
import io.github.freewulkanowy.ui.modules.account.accountdetails.AccountDetailsFragment
import io.github.freewulkanowy.ui.modules.login.LoginActivity
import io.github.freewulkanowy.ui.modules.main.MainActivity
import io.github.freewulkanowy.ui.modules.main.MainView
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account),
    AccountView, MainView.TitledView {

    @Inject
    lateinit var presenter: AccountPresenter

    @Inject
    lateinit var accountAdapter: AccountAdapter

    companion object {

        fun newInstance() = AccountFragment()
    }

    override val titleStringId = R.string.account_title

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAccountBinding.bind(view)
        presenter.onAttachView(this)
    }

    override fun initView() {
        binding.accountRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = accountAdapter
        }

        accountAdapter.onClickListener = presenter::onItemSelected

        binding.accountAdd.setOnClickListener { presenter.onAddSelected() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu[0].isVisible = false
    }

    override fun updateData(data: List<AccountItem<*>>) {
        with(accountAdapter) {
            items = data
            notifyDataSetChanged()
        }
    }

    override fun openLoginView() {
        activity?.let {
            startActivity(LoginActivity.getStartIntent(it))
        }
    }

    override fun openAccountDetailsView(student: Student) {
        (activity as? MainActivity)?.pushView(AccountDetailsFragment.newInstance(student))
    }
}
