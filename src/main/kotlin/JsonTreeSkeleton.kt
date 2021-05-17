import org.eclipse.swt.SWT
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*


class FileTreeSkeleton(obj: JObject) {
    val shell: Shell
    val tree: Tree

    init {
        shell = Shell(Display.getDefault())
        shell.setSize(250, 200)
        shell.text = obj.name
        shell.layout = GridLayout(1, false)

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                println("selected: " + tree.selection.first().data)
            }
        })

        val label = Label(shell, SWT.NONE)
        label.text = "test"




        /*
        searchText.addSelectionListener(object : SelectionAdapter(){
            override fun widgetSelected(e: SelectionEvent?) {
                val item = tree.selection.first()
                item.items.forEach {
                    while(it.text.contains(searchText.text)){
                        tree.select(it)
                    }
                }
            }
        })*/

        //temporario
        /*
        val searchButton = Button(shell, SWT.PUSH)
        searchButton.text = "search"
        searchButton.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                val item = tree.selection.first()
                item.items.forEach {
                    if(it.text.contains(searchText.text)){
                        tree.select(it)
                        label.text = it.depth().toString()
                    }
                }
            }
        })*/

        val button = Button(shell, SWT.PUSH)
        button.text = "depth"
        button.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                val item = tree.selection.first()
                label.text = item.depth().toString()
            }
        })
    }

    fun TreeItem.depth(): Int =
        if (parentItem == null) 0
        else 1 + parentItem.depth()

    fun open(root: JObject) {
        var temp = TreeItem(tree, SWT.NONE)
        temp.text = "(object)"
        temp.data = toJsonString(root)

        //search
        val textComposite = Composite(shell, SWT.NONE)
        textComposite.layout = GridLayout()
        var searchText = Text(textComposite, SWT.SINGLE or SWT.BORDER)
        var color = Color(0, 100, 255, 70)
        var color2 = Color(255, 255, 255, 70)

        root.accept(object: Visitor{
            override fun visit(obj: JObject): String {
                val item = TreeItem(temp, SWT.NONE)
                obj.accept(this)
                item.text = "(object)"
                item.data = toJsonString(obj)
                temp = item
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

            override fun visit(value: JValue): String {
                val item = TreeItem(temp, SWT.NONE)
                item.text = value.name
                when(value){
                    is JObject -> item.data = visit(value)
                    is JArray -> item.data = visit(value)
                    is JString -> item.data = visit(value)
                    is JNumber -> item.data = visit(value)
                    is JValue -> item.data = value.value
                }
                searchText.addModifyListener {
                    val item = tree.selection.first()
                    item.items.forEach {
                        if (searchText.text == "") {
                            it.background = color2
                        } else if (it.text.contains(searchText.text) ){
                            it.background = color
                        }
                    }
                }
                return ""
            }

            override fun endVisit(obj: JObject){
                if (temp.parentItem != null){
                    temp = temp.parentItem
                }
            }
        })

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