import org.eclipse.swt.layout.FillLayout
import java.awt.GridLayout
import java.awt.LayoutManager

class Phase3 : FrameSetup {
    override val title: String
        get() = "Phase 3"
    override val fileTree: FileTreeSkeleton
        get() = fileTree
}

abstract class Phase4 : FrameSetup {
    override val title: String
        get() = "Phase 4"
    override val fileTree: FileTreeSkeleton
        get() = fileTree
}

class Move : Action {
    override val name: String
        get() = "center"

    var x = 0
    var y = 0
    override fun execute(window: Window) {
    }

    override fun undo(window: Window) {
    }
}

class Size : Action {
    override val name: String
        get() = "change size"

    override fun execute(window: Window) {
    }

    override fun undo(window: Window) {
    }
}

class Undo : Action {
    override val name: String
        get() = "undo"

    override fun execute(window: Window) {
    }

    override fun undo(window: Window) {
    }
}