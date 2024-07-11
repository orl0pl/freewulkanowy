package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.api.services.SchoolsService
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.db.entities.StudentWithSemesters
import io.github.freewulkanowy.data.pojos.IntegrityRequest
import io.github.freewulkanowy.data.pojos.LoginEvent
import io.github.freewulkanowy.ui.modules.login.LoginData
import io.github.freewulkanowy.utils.IntegrityHelper
import io.github.freewulkanowy.utils.getCurrentOrLast
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class SchoolsRepository @Inject constructor(
    private val integrityHelper: IntegrityHelper,
    private val schoolsService: SchoolsService,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
) {

    suspend fun logSchoolLogin(loginData: LoginData, students: List<StudentWithSemesters>) {
        students.forEach {
            runCatching {
                withTimeout(10.seconds) {
                    logLogin(loginData, it.student, it.semesters.getCurrentOrLast())
                }
            }
                .onFailure { Timber.e(it) }
        }
    }

    private suspend fun logLogin(loginData: LoginData, student: Student, semester: Semester) {
        val requestId = UUID.randomUUID().toString()
        val token = integrityHelper.getIntegrityToken(requestId) ?: return
        val updatedStudent = student.copy(password = loginData.password)

        val schoolInfo = wulkanowySdkFactory.create(updatedStudent, semester)
            .getSchool()

        schoolsService.logLoginEvent(
            IntegrityRequest(
                tokenString = token,
                data = LoginEvent(
                    uuid = requestId,
                    schoolAddress = schoolInfo.address,
                    schoolName = schoolInfo.name,
                    schoolShort = student.schoolShortName,
                    scraperBaseUrl = student.scrapperBaseUrl,
                    loginType = student.loginType,
                    symbol = student.symbol,
                    schoolId = student.schoolSymbol,
                )
            )
        )
    }
}
