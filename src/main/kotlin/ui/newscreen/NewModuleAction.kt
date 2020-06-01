package ui.newscreen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE

class NewModuleAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        NewModuleDialog(event.project!!, event.getData(VIRTUAL_FILE)?.path).show()
    }

}