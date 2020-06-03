package ui.newscreen

import com.intellij.openapi.externalSystem.action.ExternalSystemActionUtil.buildTaskInfo
import com.intellij.openapi.externalSystem.model.execution.ExternalTaskExecutionInfo
import com.intellij.openapi.externalSystem.model.task.TaskData
import com.intellij.openapi.externalSystem.service.notification.ExternalSystemNotificationManager
import com.intellij.openapi.externalSystem.service.notification.NotificationCategory
import com.intellij.openapi.externalSystem.service.notification.NotificationData
import com.intellij.openapi.externalSystem.service.notification.NotificationSource
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import data.file.FileCreatorImpl
import data.file.WriteActionDispatcherImpl
import data.repository.SettingsRepositoryImpl
import model.ModuleType
import org.gradle.cli.CommandLineArgumentException
import org.jetbrains.plugins.gradle.util.GradleConstants
import java.util.*
import javax.swing.JComponent


class NewModuleDialog(val project: Project, val selectedFolder: String?) : DialogWrapper(true), NewModuleView {

    private val panel = NewModulePanel()

    private val presenter: NewModulePresenter

    init {
        val fileCreator = FileCreatorImpl(SettingsRepositoryImpl(project))
        val writeActionDispatcher = WriteActionDispatcherImpl()
        presenter = NewModulePresenter(this, fileCreator, writeActionDispatcher)
        init()
    }

    override fun doOKAction() {
        presenter.onOkClick(
                panel.packageTextField.text,
                panel.featureNameTextField.text,
                ModuleType.values()[panel.moduleTypeComboBox.selectedIndex],
                selectedFolder ?: "")
        Timer("SyncGradle", false).schedule(object : TimerTask() {
            override fun run() {
                runGradle("build", project.basePath ?: "")

            }
        }, NewModulePresenter.DELAY_GRADLE_SYNC)
    }


    override fun createCenterPanel(): JComponent {
        presenter.onLoadView(project.basePath)
        return panel
    }

    override fun close() = close(OK_EXIT_CODE)

    override fun showPackage(packageName: String) {
        panel.packageTextField.text = packageName
    }

    fun runGradle(
            fullCommandLine: String,
            workDirectory: String
    ) {
        val taskExecutionInfo: ExternalTaskExecutionInfo
        try {
            taskExecutionInfo = buildTaskInfo(TaskData(GradleConstants.SYSTEM_ID, "build", workDirectory, fullCommandLine))
        } catch (ex: CommandLineArgumentException) {
            val notificationData = NotificationData(
                    "<b>Command-line arguments cannot be parsed</b>",
                    """
                        <i>$fullCommandLine</i> 
                        ${ex.message}
                        """.trimIndent(),
                    NotificationCategory.WARNING, NotificationSource.TASK_EXECUTION
            )
            notificationData.isBalloonNotification = true
            ExternalSystemNotificationManager.getInstance(project).showNotification(GradleConstants.SYSTEM_ID, notificationData)
            return
        }
        ExternalSystemUtil.runTask(taskExecutionInfo.settings, taskExecutionInfo.executorId, project, GradleConstants.SYSTEM_ID)
    }

}