package io.github.freewulkanowy.ui.modules.about.contributor

import io.github.freewulkanowy.data.pojos.Contributor
import io.github.freewulkanowy.ui.base.BaseView

interface ContributorView : BaseView {

    fun initView()

    fun updateData(data: List<Contributor>)

    fun openUserGithubPage(username: String)

    fun openGithubContributorsPage()

    fun showProgress(show: Boolean)
}
