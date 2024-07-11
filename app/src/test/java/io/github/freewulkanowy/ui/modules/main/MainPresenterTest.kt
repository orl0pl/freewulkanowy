package io.github.freewulkanowy.ui.modules.main

import io.github.freewulkanowy.MainCoroutineRule
import io.github.freewulkanowy.data.repositories.PreferencesRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.data.repositories.WulkanowyRepository
import io.github.freewulkanowy.services.sync.SyncManager
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.AdsHelper
import io.github.freewulkanowy.utils.AnalyticsHelper
import io.github.freewulkanowy.utils.AppInfo
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainPresenterTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK(relaxed = true)
    lateinit var errorHandler: ErrorHandler

    @MockK
    lateinit var studentRepository: StudentRepository

    @MockK(relaxed = true)
    lateinit var wulkanowyRepository: WulkanowyRepository

    @MockK(relaxed = true)
    lateinit var prefRepository: PreferencesRepository

    @MockK(relaxed = true)
    lateinit var syncManager: SyncManager

    @MockK
    lateinit var mainView: MainView

    @MockK(relaxed = true)
    lateinit var analytics: AnalyticsHelper

    @MockK(relaxed = true)
    lateinit var appInfo: AppInfo

    @MockK(relaxed = true)
    lateinit var adsHelper: AdsHelper

    private lateinit var presenter: MainPresenter

    @Before
    fun initPresenter() {
        MockKAnnotations.init(this)
        clearMocks(mainView)

        every { mainView.initView(any(), any(), any()) } just Runs
        presenter = MainPresenter(
            errorHandler = errorHandler,
            studentRepository = studentRepository,
            preferencesRepository = prefRepository,
            syncManager = syncManager,
            analytics = analytics,
            json = Json,
            appInfo = appInfo,
            adsHelper = adsHelper,
            wulkanowyRepository = wulkanowyRepository
        )
        presenter.onAttachView(mainView, null)
    }

    @Test
    fun onTabSelectedTest() {
        every { mainView.notifyMenuViewChanged() } just Runs

        every { mainView.switchMenuView(1) } just Runs
        presenter.onTabSelected(1, false)
        verify { mainView.switchMenuView(1) }
    }
}
