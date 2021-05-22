class JObject(name: String,  var children: Array<JValue> = arrayOf(), parent: JObject?): JValue(name, children, parent){

    override fun accept(v: Visitor): String {
        var json = if(parent != null) "\"$name\": {" else "{"
        // visitar cada filho
        children.forEach {
            json += v.visit(it)
        }
        v.endVisit(this)
        return json.dropLast(1) + "},"
    }
}