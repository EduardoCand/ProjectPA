interface Visitor {
    fun visit(obj: JObject)
    fun visit(string: JString)
    fun visit(num: JNumber)
    fun visit(array: JArray)
    fun visit(value: JValue)
}

class JsonVisitor : Visitor {
    var json = "{\n"

    override fun visit(value: JValue) {
        when(value) {
            is JString -> visit(value)
            is JNumber -> visit(value)
            is JArray -> visit(value)
            is JObject -> visit(value)
        }
    }

    override fun visit(obj: JObject) {
        // abre parentesis curvos e mete nome do objeto
        json += "\"${obj.name}\": {"
        obj.accept(this)
        json = json.dropLast(1)
        json += "},"
    }

    override fun visit(string: JString) {
        json += "\"${string.name}\": ${string.value},"
    }

    override fun visit(num: JNumber) {
        json += "\"${num.name}\": ${num.value},"
    }

    override fun visit(array: JArray) {
        json += "\"${array.name}\": ${array.getArray()},"
    }
}