import org.eclipse.swt.SWT
import org.eclipse.swt.custom.SashForm
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.TreeListener
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

interface FrameSetup {
    val title: String
    val fileTree: FileTreeSkeleton
}

interface Action {
    val name: String
    fun execute(window: Window)
    fun undo(window: Window)
}

class FileTreeSkeleton() {
    private val shell: Shell = Shell(Display.getDefault())
    val tree: Tree

    @Inject
    private lateinit var setup: FrameSetup

    @InjectAdd
    private val actions = mutableListOf<Action>()

    init {
        shell.setSize(250, 200)
        shell.text = ""
        shell.layout = FillLayout()

        //onde mostra o objeto
        var sashForm = SashForm(shell, SWT.SINGLE or SWT.HORIZONTAL)

        tree = Tree(sashForm, SWT.SINGLE or SWT.HORIZONTAL)
        val objectText = Text(sashForm, SWT.HORIZONTAL)

        //mostrar o objeto
        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                objectText.text = tree.selection.first().data.toString()
            }
        })
    }
    fun open(root: JObject) {
        shell.text = setup.title
        val imageFolder = Image(Display.getDefault(),"C:/Users/Eduardo/IdeaProjects/projetoPA/src/main/resources/folder.gif")
        val imageFile = Image(Display.getDefault(),"C:/Users/Eduardo/IdeaProjects/projetoPA/src/main/resources/file.gif")

        var temp = TreeItem(tree, SWT.NONE)
        temp.text = "(object)"
        temp.data = toJsonString(root)

        // root
        var name = TreeItem(temp, SWT.NONE)
        name.text = "name: \"${root.name}\""
        name.data = root.name

        var children = TreeItem(temp, SWT.NONE)
        children.text = "children"
        children.data = "children"

        root.accept(object: Visitor{
            override fun visit(obj: JObject): String {
                val item = TreeItem(children, SWT.NONE)
                item.text = "(object)"
                item.data = obj.name
                temp = item
                // objeto abaixo, vai ter "name" e "children"
                name = TreeItem(item, SWT.NONE)
                name.text = "name: \"${obj.name}\""
                name.data = toJsonString(obj)

                children = TreeItem(item, SWT.NONE)
                children.text = "children"
                children.data = "children"
                obj.accept(this)

                return ""
            }

            override fun visit(string: JString): String {
                return ""
            }

            override fun visit(num: JNumber): String {
                return ""
            }

            override fun visit(array: JArray): String {
                return ""
            }

            override fun visit(boolean: JBoolean): String {
                return ""
            }

            override fun visit(value: JValue): String {
                when(value){
                    is JObject -> visit(value)
                    is JArray -> visit(value)
                    is JString -> visit(value)
                    is JNumber -> visit(value)
                    is JBoolean -> visit(value)
                }
                return ""
            }

            override fun endVisit(obj: JObject){
                if (temp.parentItem != null){
                    temp = temp.parentItem
                }
            }
        })
        //procurar pelo nome
        val textComposite = Composite(shell, SWT.VERTICAL)
        textComposite.layout = GridLayout()
        var searchText = Text(textComposite, SWT.SINGLE or SWT.BORDER or SWT.VERTICAL)
        var color = Color(0, 100, 255, 70)
        var color2 = Color(255, 255, 255, 70)

        searchText.addModifyListener {
            tree.traverse {
                if (searchText.text == "") {
                    it.background = color2
                } else if (it.text.contains(searchText.text) ){
                    it.background = color
                } else if (!it.text.contains(searchText.text)){
                    it.background = color2
                }
            }
        }
        if (setup.title == "Phase 4"){
            //colocar icons
            tree.traverse{
                if (it.text == "(object)"){
                    it.image = imageFolder
                } else if (it.text.contains("name:")){
                    it.image = imageFile
                }
            }
        }

        tree.expandAll()
        shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }
}

fun Tree.expandAll() = traverse { it.expanded = true }

fun Tree.traverse(visitor: (TreeItem) -> Unit) {
    fun TreeItem.traverse() {
        visitor(this)
        items.forEach {
            it.traverse()
        }
    }
    items.forEach { it.traverse() }
}