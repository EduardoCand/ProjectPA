class JBoolean(name: String, value: Boolean, parent: JObject?): JValue(name, value, parent) {
    override fun accept(v: Visitor): String = "\"$name\":$value"
}