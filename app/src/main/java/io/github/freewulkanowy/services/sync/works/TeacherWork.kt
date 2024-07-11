package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.TeacherRepository
import io.github.freewulkanowy.data.waitForResult

import javax.inject.Inject

class TeacherWork @Inject constructor(private val teacherRepository: TeacherRepository) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        teacherRepository.getTeachers(student, semester, true).waitForResult()
    }
}
