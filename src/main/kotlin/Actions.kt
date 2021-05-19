import java.awt.GridLayout
import java.awt.LayoutManager

class DefaultSetup : FrameSetup {
    override val title: String
        get() = "Test"
    override val layoutManager: LayoutManager
        get() = GridLayout(2, 1)
    override val fileTree: FileTreeSkeleton// duvida!
        get() = fileTree
}

abstract class TreeSetup : FrameSetup {
    override val title: String
        get() = "TreeSetup"
    override val fileTree: FileTreeSkeleton
        get() = fileTree
}

class Move : Action {
    override val name: String
        get() = "center"

    var x = 0
    var y = 0
    override fun execute(window: Window) {
        x = window.location.x
        y = window.location.y
        window.move(500, 500)
    }

    override fun undo(window: Window) {
        window.move(x, y)
    }
}

class Size : Action {
    override val name: String
        get() = "change size"

    override fun execute(window: Window) {
        window.setSize(500, 500)
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