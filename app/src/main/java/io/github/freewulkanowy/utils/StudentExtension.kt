package io.github.freewulkanowy.utils

import io.github.freewulkanowy.data.db.entities.Student

inline val Student.nickOrName get() = nick.ifBlank { studentName }
