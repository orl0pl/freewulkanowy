package io.github.freewulkanowy.ui.modules.about.contributor

import io.github.freewulkanowy.data.*
import io.github.freewulkanowy.data.pojos.Contributor
import io.github.freewulkanowy.data.repositories.AppCreatorRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import javax.inject.Inject

class ContributorPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val appCreatorRepository: AppCreatorRepository
) : BasePresenter<ContributorView>(errorHandler, studentRepository) {

    override fun onAttachView(view: ContributorView) {
        super.onAttachView(view)
        view.initView()
        loadData()
    }

    fun onItemSelected(contributor: Contributor) {
        view?.openUserGithubPage(contributor.githubUsername)
    }

    fun onSeeMoreClick() {
        view?.openGithubContributorsPage()
    }

    private fun loadData() {
        resourceFlow { appCreatorRepository.getAppCreators() }
            .onResourceLoading { view?.showProgress(true) }
            .onResourceSuccess { view?.updateData(it) }
            .onResourceNotLoading { view?.showProgress(false) }
            .onResourceError { errorHandler.dispatch(it) }
            .launch()
    }
}
