class JObject(name: String,  var children : MutableList<JValue> = mutableListOf(), parent: JObject?): JValue(name, children, parent){

    override fun accept(v: Visitor) {
        // visitar cada filho
        children.forEach {
            v.visit(it)
        }
    }

    /*fun getObject(strList: Array<JString>, valList: Array<JValue>): String {
        //convert to value then zip then to map
        var obj = strList.map {it.value}.zip(valList.map {it.value}).toMap()
        return obj.toString().replace("=", ":")
    }*/

}