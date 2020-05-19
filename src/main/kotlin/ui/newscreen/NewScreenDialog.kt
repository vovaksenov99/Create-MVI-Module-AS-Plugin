package ui.newscreen

import com.intellij.execution.Executor
import com.intellij.execution.RunManagerEx
import com.intellij.execution.executors.DefaultRunExecutor
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
import data.file.*
import data.repository.CreateModuleRepository
import data.repository.ModuleRepositoryImpl
import data.repository.SettingsRepositoryImpl
import data.repository.SourceRootRepositoryImpl
import model.AndroidComponent
import org.gradle.cli.CommandLineArgumentException
import org.jetbrains.plugins.gradle.util.GradleConstants
import javax.swing.JComponent


class NewScreenDialog(val project: Project, currentPath: CurrentPath?) : DialogWrapper(true), NewScreenView {

    private val panel = NewScreenPanel()

    private val presenter: NewScreenPresenter

    init {
        project1 = project
        val projectStructure = ProjectStructureImpl(project)
        val createModuleRepository = CreateModuleRepository(project)
        val sourceRootRepository = SourceRootRepositoryImpl(projectStructure)
        val fileCreator = FileCreatorImpl(SettingsRepositoryImpl(project), sourceRootRepository)
        val packageExtractor = PackageExtractorImpl(currentPath, sourceRootRepository)
        val writeActionDispatcher = WriteActionDispatcherImpl()
        val moduleRepository = ModuleRepositoryImpl(projectStructure)
        presenter = NewScreenPresenter(this, fileCreator, packageExtractor, writeActionDispatcher, moduleRepository, createModuleRepository, currentPath)
        init()
    }

    override fun doOKAction() =
            presenter.onOkClick(
                    panel.packageTextField.text,
                    panel.nameTextField.text,
                    AndroidComponent.values()[panel.androidComponentComboBox.selectedIndex],
                    panel.moduleTextField.text,
                    project.basePath ?: "")


    override fun createCenterPanel(): JComponent {
        presenter.onLoadView()
        return panel
    }

    override fun close() = close(DialogWrapper.OK_EXIT_CODE)

    override fun showPackage(packageName: String) {
        panel.packageTextField.text = packageName
    }


    private fun getExecutor(): Executor? {
        return DefaultRunExecutor.getRunExecutorInstance()
        //ExecutorRegistry.getInstance().getExecutorById(ToolWindowId.DEBUG)
    }

    companion object{

        lateinit var project1: Project
    fun runGradle(fullCommandLine: String,
                          workDirectory: String) {
        val taskExecutionInfo: ExternalTaskExecutionInfo
        try {
            taskExecutionInfo = buildTaskInfo(TaskData(GradleConstants.SYSTEM_ID,"build",workDirectory, fullCommandLine))
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
            ExternalSystemNotificationManager.getInstance(project1).showNotification(GradleConstants.SYSTEM_ID, notificationData)
            return
        }
            ExternalSystemUtil.runTask(taskExecutionInfo.settings, taskExecutionInfo.executorId, project1, GradleConstants.SYSTEM_ID)
        val configuration = ExternalSystemUtil.createExternalSystemRunnerAndConfigurationSettings(taskExecutionInfo.settings,
                project1, GradleConstants.SYSTEM_ID)
                ?: return
        val runManager = RunManagerEx.getInstanceEx(project1)
        val existingConfiguration = runManager.findConfigurationByName(configuration.name)
        if (existingConfiguration == null) {
            runManager.setTemporaryConfiguration(configuration)
        } else {
            runManager.selectedConfiguration = existingConfiguration
        }
    }
    }
}