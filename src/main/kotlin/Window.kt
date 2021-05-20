import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Tree
/*
interface FrameSetup {
    val title: String
    val fileTree: FileTreeSkeleton
}

interface Action {
    val name: String
    fun execute(window: Window)
    fun undo(window: Window)
}*/

class Window {
    //private val frame = JFrame()
    //private val controlPanel = JPanel()

    private val shell: Shell = Shell(Display.getDefault())
    //val tree: Tree

    @Inject
    private lateinit var setup: FrameSetup

    @InjectAdd
    private val actions = mutableListOf<Action>()

    //private val stack = Stack<Action>()

    init {
        //frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        shell.text = setup.title
        shell.layout = FillLayout()
        shell.setSize(250, 200)
    }

    //val location get() = frame.location
    //val dimension get() = frame.size

    fun open(obj: JObject) {

        if (setup.title == "Phase 3"){
            setup.fileTree.open(obj)
        }else if (setup.title == "Phase 4"){
            setup.fileTree.open(obj)
        }


        //controlPanel.layout = FlowLayout()
        //controlPanel.add(treeView)
        //frame.add(controlPanel);
/*
        actions.forEach { action ->
            val button = JButton(action.name)
            button.addActionListener {
                if (action.name == "center" || action.name == "change size"){
                    action.execute(this)
                    stack.push(action)
                }else if (action.name == "undo" && !stack.isEmpty()){
                    val a = stack.pop()
                    action.undo(this)
                }
            }
            frame.add(button)
        }
        frame.isVisible = true*/
    }
/*
    fun move(x: Int, y: Int) {
        frame.location = Point(x, y)
    }

    fun setSize(width: Int, height: Int) {
        require(width > 0)
        require(height > 0)
        frame.size = Dimension(width, height)
    }*/

}