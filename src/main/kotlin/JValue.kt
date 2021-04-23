abstract class JValue(var name: String, var value: Any, val parent: JObject?){
    abstract fun accept(v: Visitor)
}