import kotlin.reflect.full.declaredMemberProperties

/**
 * parses any object to json.
 */
fun toJson(obj: Any, parent: JObject? = null, name: String = ""): JObject{
    var result = JObject(name, parent = parent)
    lateinit var array: List<JValue> //??
    // jsonify map
    if(obj is Map<*, *>) {
        // parse pairs
        array = obj.map {
            // map map entry to Jvalues in array
            if(it.key is String) {
                // get type
                when (it.value) {
                    is String -> JString(it.key as String, it.value as String, parent)
                    is Number -> JNumber(it.key as String, it.value as Number, parent)
                    is Array<*> -> JArray(it.key as String, (it.value as Array<Any>).map {elem -> elem.toTypedJson()}.toTypedArray(), parent)
                    is Collection<*> -> JArray(it.key as String, (it.value as Collection<JValue>).map{ elem -> elem.toTypedJson() }.toTypedArray(), parent)
                    is Boolean -> JBoolean(it.key as String, it.value as Boolean, parent)
                    is Map<*, *> -> toJson(it.value as Map<String, Any>, parent)
                    else -> toJson(it.value!!, parent, it.key as String)
                }
            } else {
                throw Exception("map keys are not strings.")
            }
        }
    } else {
        array = obj.javaClass.kotlin.declaredMemberProperties.map {
            var value = it.get(obj)!!
            when(value) {
                is String -> JString(it.name, value, result)
                is Number -> JNumber(it.name, value, result)
                is Array<*> -> JArray(it.name, (value as Array<Any>).map{elem -> elem.toTypedJson()}.toTypedArray(), result)
                is Collection<*> -> JArray(it.name, (value as Collection<Any>).map{ elem -> elem.toTypedJson() }.toTypedArray(), result)
                is Boolean -> JBoolean(it.name, value, result)
                // return jobject
                is Map<*, *> -> toJson(value, result, it.name)
                // assume else means Any type
                else -> toJson(value, result, it.name)
            }
        }
    }
    result.children = array.toTypedArray()
    return result
}


fun Any.toTypedJson(): JValue {
    return when(this) {
        is String -> JString("", this, null)
        is Number -> JNumber("", this, null)
        is Array<*> -> JArray("", this.map { it!!.toTypedJson() }.toTypedArray(), null)
        is Collection<*> -> JArray("", this.map { it!!.toTypedJson() }.toTypedArray(), null)
        is Boolean -> JBoolean("", this, null)
        is Map<*, *> -> toJson(this)
        else -> toJson(this)
    }
}

/**
 * retorna a string em formato json
 */
fun toJsonString(obj: JObject): String {
    var v = JsonVisitor()
    var x = obj.accept(v)
    // drop last ,
    return "${x.dropLast(1)}\n}"
}