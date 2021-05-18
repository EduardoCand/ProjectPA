interface Visitor {
    fun visit(obj: JObject): String
    fun visit(string: JString): String
    fun visit(num: JNumber): String
    fun visit(array: JArray): String
    fun visit(value: JValue): String
    fun visit(boolean: JBoolean): String
    fun endVisit(obj: JObject)
}

class JsonVisitor : Visitor {
    var json = "{"

    override fun visit(value: JValue): String {
        return when(value) {
            is JString -> visit(value)
            is JNumber -> visit(value)
            is JArray -> visit(value)
            is JObject -> visit(value)
            is JBoolean -> visit(value)
            else -> ""
        }
    }

    override fun visit(obj: JObject): String {
        // abre parentesis curvos e mete nome do objeto
        //json += "\"${obj.name}\": {"
        //aceitar este visitor
        //vai ver os children
        return obj.accept(this)
        //json = json.dropLast(1)
        //json += "},"
    }

    override fun visit(string: JString): String {
        return string.accept(this)
    }

    override fun visit(num: JNumber): String {
        return num.accept(this)
    }

    override fun visit(array: JArray): String {
        return array.accept(this)
    }

    override fun visit(boolean: JBoolean): String {
        return boolean.accept(this)
    }

    override fun endVisit(obj: JObject) {}
}