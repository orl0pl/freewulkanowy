package io.github.freewulkanowy.ui.modules.debug.notification.mock

import io.github.freewulkanowy.data.db.entities.LuckyNumber
import java.time.LocalDate
import kotlin.random.Random

val debugLuckyNumber
    get() = LuckyNumber(
        studentId = 0,
        date = LocalDate.now(),
        luckyNumber = Random.nextInt(1, 128),
    )
