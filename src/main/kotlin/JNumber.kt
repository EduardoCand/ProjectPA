class JNumber(name: String, num : Number, parent: JObject?): JValue(name, num, parent) {

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}