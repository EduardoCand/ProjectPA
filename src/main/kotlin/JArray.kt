class JArray(name:String, var array: Array<JValue>, parent: JObject?) : JValue(name, array, parent){

    /**
     * obtem a JString deste array
     */
    fun getArray(): String{
        // recursively traverses this array
        var jvalue = recurseArray(this)
        // remove the last comma
        return jvalue.substring(0, jvalue.length - 2)
    }

    fun getJArray(): Array<JValue>{
        return array
    }

    override fun accept(v: Visitor) {
        TODO("Not yet implemented")
    }

    /**
     * varrer JArrays dentro de JArrays recursivamente
     */
    fun recurseArray(array: JArray): String {
        // Abre o parentesis
        var string = "["
        array.array.forEach {
            var visitor = JsonVisitor()
            //ver o tipo do elemento. se o for um array percorre novamente e se for JObject, append o json String do jObject em questão, se nao so append o valor
            string += when(it) {
                is JArray -> recurseArray(it)
                is JObject -> {
                    visitor.visit(it)
                    // drop the first {
                    visitor.json.drop(1)
                }
                else -> "${it.value}, "
            }
        }
        // retorna o json string com a ultima virgula do ultimo elemento removida
        return "${string.substring(0, string.length - 2)}], "
    }
}