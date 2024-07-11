package io.github.freewulkanowy.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.freewulkanowy.data.db.entities.GradePartialStatistics
import kotlinx.coroutines.flow.Flow

@Dao
interface GradePartialStatisticsDao : BaseDao<GradePartialStatistics> {

    @Query("SELECT * FROM GradePartialStatistics WHERE student_id = :studentId AND semester_id = :semesterId")
    fun loadAll(semesterId: Int, studentId: Int): Flow<List<GradePartialStatistics>>
}
