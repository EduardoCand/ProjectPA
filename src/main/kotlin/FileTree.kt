import org.eclipse.swt.SWT
import org.eclipse.swt.custom.SashForm
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
<<<<<<< Updated upstream:src/main/kotlin/FileTree.kt
import org.eclipse.swt.layout.FillLayout
=======
>>>>>>> Stashed changes:src/main/kotlin/JsonTreeSkeleton.kt
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.layout.RowLayout
import org.eclipse.swt.widgets.*
import java.io.FileWriter
import java.io.IOException

interface FrameSetup {
    val title: String
<<<<<<< Updated upstream:src/main/kotlin/FileTree.kt
=======
    val fileTree: JsonTreeSkeleton
    fun apply(skeleton: JsonTreeSkeleton, root: JObject, shell: Shell)
>>>>>>> Stashed changes:src/main/kotlin/JsonTreeSkeleton.kt
}

interface Action {
    val name: String
    fun execute(skeleton: JsonTreeSkeleton, treeItem: TreeItem)
}

<<<<<<< Updated upstream:src/main/kotlin/FileTree.kt
class FileTree() {
=======
class JsonTreeSkeleton() {
>>>>>>> Stashed changes:src/main/kotlin/JsonTreeSkeleton.kt
    private val shell: Shell = Shell(Display.getDefault())
    val tree: Tree

    @Inject
    private lateinit var setup: FrameSetup

    @InjectAdd
    private val actions = mutableListOf<Action>()


    init {
        shell.setSize(250, 200)
        shell.layout = RowLayout(1)

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
<<<<<<< Updated upstream:src/main/kotlin/FileTree.kt

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
=======
        shell.text = setup.title

        actions.forEach {
            val button = Button(shell, SWT.HORIZONTAL)
            button.text = it.name
            button.addSelectionListener(object: SelectionAdapter(){
                override fun widgetSelected(e: SelectionEvent) {
                    it.execute(this@JsonTreeSkeleton, tree.selection.first())
>>>>>>> Stashed changes:src/main/kotlin/JsonTreeSkeleton.kt
                }
            })
        }

<<<<<<< Updated upstream:src/main/kotlin/FileTree.kt
        //procurar pelo nome
        val textComposite = Composite(shell, SWT.VERTICAL)
        textComposite.layout = GridLayout()
=======
        if(::setup.isInitialized){
            setup.apply(this, root, shell)
        } else {
            throw UninitializedPropertyAccessException("setup is not initialized")
        }


        //search for name
        val textComposite = Composite(shell, SWT.NONE)
        textComposite.layout = RowLayout()
>>>>>>> Stashed changes:src/main/kotlin/JsonTreeSkeleton.kt
        var searchText = Text(textComposite, SWT.SINGLE or SWT.BORDER or SWT.VERTICAL)
        var color = Color(0, 100, 255, 70)
        var color2 = Color(255, 255, 255, 70)

        //change background color
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

        tree.expandAll()
        shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }

    //save json file to a .txt
    fun save(saveObject: TreeItem): Boolean {
        val dialog = FileDialog(shell, SWT.SAVE)
        dialog.filterExtensions = arrayOf("*.txt")
        val filename = dialog.open()
        var writer: FileWriter? = null
        return try {
            writer = FileWriter(filename)
            writer.write(saveObject.data.toString())
            true
        } catch (e: IOException) {
            System.err.println("Exception occured: File not saved!")
            false
        } finally {
            try {
                writer!!.close()
            } catch (e: Exception) {
            }
        }
    }

    //opens new window with selected object
    fun openWindow(treeItem: TreeItem){

        val shell = Shell(Display.getDefault())
        shell.setSize(500,500)
        shell.layout = GridLayout(1, false)

        var sashForm = SashForm(shell, SWT.SINGLE or SWT.HORIZONTAL)
        val objectText = Text(sashForm, SWT.HORIZONTAL)

        objectText.text = treeItem.data.toString()

        shell.pack()
        shell.open()

    }

    //Muda o nome de um objeto
    fun changeName(treeItem: TreeItem){

        val shell = Shell(Display.getDefault())
        shell.setSize(500,500)
        shell.layout = GridLayout(2, false)

        val newNameLabel = Label(shell, SWT.NONE)
        newNameLabel.text = "New name:"

        val textComposite = Composite(shell, SWT.NONE)
        textComposite.layout = GridLayout()

        val changeButton = Button(shell, SWT.HORIZONTAL)
        changeButton.text = "Change"

        var newNameText = Text(textComposite, SWT.SINGLE or SWT.BORDER or SWT.VERTICAL)

        newNameText.addModifyListener{
            treeItem.text = newNameText.text.toString()
            val display = Display.getDefault()

            changeButton.addSelectionListener(object: SelectionAdapter(){
                override fun widgetSelected(e: SelectionEvent) {
                    while (!display.isDisposed) {
                        if (!display.readAndDispatch()) display.sleep()
                    }
                    display.dispose()
                }
            })
        }
        shell.pack()
        shell.open()
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