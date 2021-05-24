import org.eclipse.swt.SWT
import org.eclipse.swt.custom.SashForm
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.layout.RowLayout
import org.eclipse.swt.widgets.*
import java.io.FileWriter
import java.io.IOException

interface FrameSetup {
    val title: String
    val fileTree: JsonTreeSkeleton
    fun apply(skeleton: JsonTreeSkeleton, root: JObject, shell: Shell)
}

interface Action {
    val name: String
    fun execute(skeleton: JsonTreeSkeleton, treeItem: TreeItem)
}

class JsonTreeSkeleton() {
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
        shell.text = setup.title

        actions.forEach {
            val button = Button(shell, SWT.HORIZONTAL)
            button.text = it.name
            button.addSelectionListener(object: SelectionAdapter(){
                override fun widgetSelected(e: SelectionEvent) {
                    it.execute(this@JsonTreeSkeleton, tree.selection.first())
                }
            })
        }

        if(::setup.isInitialized){
            setup.apply(this, root, shell)
        } else {
            throw UninitializedPropertyAccessException("setup is not initialized")
        }


        //search for name
        val textComposite = Composite(shell, SWT.NONE)
        textComposite.layout = RowLayout()
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
        val objectText = Text(sashForm, SWT.NONE)

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

            changeButton.addSelectionListener(object: SelectionAdapter(){
                override fun widgetSelected(e: SelectionEvent) {
                    treeItem.text = newNameText.text.toString()
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