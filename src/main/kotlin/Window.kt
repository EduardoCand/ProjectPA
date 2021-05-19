import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.LayoutManager
import java.awt.Point
import java.util.*
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode


interface FrameSetup {
    val title: String
    val layoutManager: LayoutManager
    val fileTree: FileTreeSkeleton
}

interface Action {
    val name: String
    fun execute(window: Window)
    fun undo(window: Window)
}

class Window {
    private val frame = JFrame()
    private val controlPanel = JPanel()

    // 1) eliminar dependencia de DefaultSetup
    @Inject
    private lateinit var setup: FrameSetup

    // 2) eliminar dependencias das acoes concretas (Center, Size)
    @InjectAdd
    private val actions = mutableListOf<Action>()

    private val stack = Stack<Action>()

    init {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.size = Dimension(300, 200)
    }

    val location get() = frame.location
    val dimension get() = frame.size

    fun open(obj: JObject) {
        frame.title = setup.title
        frame.layout = setup.layoutManager

        fun jsonToTree(obj: JObject): DefaultMutableTreeNode{
            var node = DefaultMutableTreeNode(obj.name)
            var leaf: DefaultMutableTreeNode
            if (obj.parent == null){
                node = DefaultMutableTreeNode(obj.name)
            }else{
                leaf = DefaultMutableTreeNode(obj.name)
                node.add(leaf)
            }
            return node
        }

        val teste = jsonToTree(obj)
        /*val department = DefaultMutableTreeNode("Department")
        val salesDepartment = DefaultMutableTreeNode("Sales")
        val employee1 = DefaultMutableTreeNode("Robert")
        salesDepartment.add(employee1);
        department.add(salesDepartment);
        */

        val tree = JTree(teste)
        val treeView = JScrollPane(tree)

        controlPanel.layout = FlowLayout()
        controlPanel.add(treeView)
        frame.add(controlPanel);

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
        frame.isVisible = true
    }

    fun move(x: Int, y: Int) {
        frame.location = Point(x, y)
    }

    fun setSize(width: Int, height: Int) {
        require(width > 0)
        require(height > 0)
        frame.size = Dimension(width, height)
    }

}