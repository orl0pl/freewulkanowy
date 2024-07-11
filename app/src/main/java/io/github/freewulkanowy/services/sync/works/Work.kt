package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student

interface Work {

    suspend fun doWork(student: Student, semester: Semester, notify: Boolean)
}
