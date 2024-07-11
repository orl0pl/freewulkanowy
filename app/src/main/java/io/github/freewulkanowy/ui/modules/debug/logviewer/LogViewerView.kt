package io.github.freewulkanowy.ui.modules.debug.logviewer

import io.github.freewulkanowy.ui.base.BaseView
import java.io.File

interface LogViewerView : BaseView {

    fun initView()

    fun setLines(lines: List<String>)

    fun shareLogs(files: List<File>)
}
