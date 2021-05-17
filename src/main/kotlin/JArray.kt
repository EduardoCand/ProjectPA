class JArray(name:String, var array: Array<JValue>, parent: JObject?) : JValue(name, array, parent){

    /**
     * obtem a JString deste array
     */
    fun getArray(): String{
        // varre o array recursivamente
        var jvalue = recurseArray(this)
        // remove a ultima virgula
        return jvalue.substring(0, jvalue.length - 2)
    }

    fun getJArray(): Array<JValue>{
        return array
    }

    override fun accept(v: Visitor): String = "\"${name}\": ${getArray()},"

    /**
     * varrer JArrays dentro de JArrays recursivamente
     */
    fun recurseArray(array: JArray): String {
        // Abre o parentesis reto
        var string = "["
        array.array.forEach {
            var visitor = JsonVisitor()
            //ver o tipo do elemento. se o for um array percorre novamente e se for JObject, append o json String do jObject em questão, se nao so append o valor
            string += when(it) {
                is JArray -> recurseArray(it)
                is JObject -> {
                    visitor.visit(it)
                    // tira a primeira {
                    visitor.json.drop(1)
                }
                else -> "${it.value}, "
            }
        }
        // retorna o json string com a ultima virgula do ultimo elemento removida
        return "${string.substring(0, string.length - 2)}], "
    }
}