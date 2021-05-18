import org.eclipse.swt.SWT
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*


class FileTreeSkeleton(obj: JObject) {
    private val shell: Shell = Shell(Display.getDefault())
    val tree: Tree

    init {
        shell.setSize(250, 200)
        shell.text = obj.name
        shell.layout = GridLayout(1, false)

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                println("selected: " + tree.selection.first().data)
            }
        })
    }

    fun open(root: JObject) {

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
                // under object, will have "name" and "children"
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

        //search
        val textComposite = Composite(shell, SWT.NONE)
        textComposite.layout = GridLayout()
        var searchText = Text(textComposite, SWT.SINGLE or SWT.BORDER)
        var color = Color(0, 100, 255, 70)
        var color2 = Color(255, 255, 255, 70)

        searchText.addModifyListener {
            val item = tree.selection //duvida
            item.forEach {
                it.items.forEach {
                    if (searchText.text == "") {
                        it.background = color2
                    } else if (it.text.contains(searchText.text) ){
                        it.background = color
                    } else if (!it.text.contains(searchText.text)){
                        it.background = color2
                    }
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