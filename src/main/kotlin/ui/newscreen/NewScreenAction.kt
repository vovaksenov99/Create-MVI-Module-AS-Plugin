package ui.newscreen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE

class NewScreenAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        NewScreenDialog(event.project!!, event.getData(VIRTUAL_FILE)?.path).show()
    }

}