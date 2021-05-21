import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Display

class FolderTree : FrameSetup {
    override val title: String
        get() = "FolderTree"
    fun open(skeleton: FileTree){
    }
}

class IconFolderTree : FrameSetup {
    override val title: String
        get() = "IconFolderTree"

    val imageFolder = Image(Display.getDefault(),"E:/Mestrado/PA//ProjectPA/src/main/resources/folder.gif")
    val imageFile = Image(Display.getDefault(),"E:/Mestrado/PA//ProjectPA/src/main/resources/file.gif")

    fun addIcons(skeleton: FileTree){
        skeleton.tree.traverse{
            if (it.text == "(object)"){
                it.image = imageFolder
            } else if (it.text.contains("name:")){
                it.image = imageFile
            }
        }
    }


    /*
        if (setup.title == "Phase 4"){
            //colocar icons
            tree.traverse{
                if (it.text == "(object)"){
                    it.image = imageFolder
                } else if (it.text.contains("name:")){
                    it.image = imageFile
                }
            }
        }*/

}

class Move : Action {
    override val name: String
        get() = "center"

    var x = 0
    var y = 0
    override fun execute() {
    }

    override fun undo() {
    }
}

/*
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
}*/