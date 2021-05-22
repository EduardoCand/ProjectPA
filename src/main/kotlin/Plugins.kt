import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.*

class TreeSkeleton : FrameSetup {

    override val title: String
        get() = "Tree Skeleton"

    override val fileTree: JsonTreeSkeleton
        get() = fileTree

    override fun apply(skeleton: JsonTreeSkeleton, root: JObject, shell: Shell){
        var temp = TreeItem(skeleton.tree, SWT.NONE)
        temp.text = "(object)"
        temp.data = toJsonString(root)

        // root
        var name = TreeItem(temp, SWT.NONE)
        name.text = "name: \"${root.name}\""
        name.data = root.name

        var children = TreeItem(temp, SWT.NONE)
        children.text = "children"
        children.data = "children"

        root.accept(object: Visitor {
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
    }

}

class IconTreeSkeleton : FrameSetup {

    override val title: String
        get() = "Icon Tree Skeleton"

    override val fileTree: JsonTreeSkeleton
        get() = fileTree

    override fun apply(skeleton: JsonTreeSkeleton, root: JObject, shell: Shell){

        // image paths
        val imageFolder = Image(Display.getDefault(),"E:/Mestrado/PA//ProjectPA/src/main/resources/folder.gif")
        val imageFile = Image(Display.getDefault(),"E:/Mestrado/PA//ProjectPA/src/main/resources/file.gif")

        // set root
        var temp = TreeItem(skeleton.tree, SWT.NONE)
        temp.text = root.name
        temp.data = toJsonString(root)

        // visitor
        root.accept(object: Visitor {
            override fun visit(obj: JObject): String {
                // set children
                var item = TreeItem(temp, SWT.NONE)
                item.text = obj.name
                item.data = toJsonString(obj)
                // set temp = item for recursion
                temp = item
                // accept this visitor to visit children
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

            override fun visit(value: JValue): String {
                when(value) {
                    is JObject -> visit(value)
                }
                return ""
            }

            override fun visit(boolean: JBoolean): String {
                return ""
            }

            override fun endVisit(obj: JObject) {
                if (temp.parentItem != null){
                    temp = temp.parentItem
                }
            }
        })

        //set icons
        skeleton.tree.traverse{
            if (it.itemCount > 0){
                it.image = imageFolder
            } else{
                it.image = imageFile
            }
        }
    }
}

class Save : Action {
    override val name: String
        get() = "Save"

    override fun execute(skeleton: JsonTreeSkeleton, treeItem: TreeItem) {
        skeleton.save(treeItem)
    }

}

class View : Action {
    override val name: String
        get() = "Open"

    override fun execute(skeleton: JsonTreeSkeleton, treeItem: TreeItem) {
        skeleton.openWindow(treeItem)
    }
}

class Change : Action {
    override val name: String
        get() = "Change Name"

    override fun execute(skeleton: JsonTreeSkeleton, treeItem: TreeItem) {
        skeleton.changeName(treeItem)
    }
}