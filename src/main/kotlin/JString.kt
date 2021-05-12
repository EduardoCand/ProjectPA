class JString(name:String, str: String, parent: JObject?): JValue(name, "\"$str\"", parent){

    //primary constructor -> initialize the passing variable str
    var string = str

    override fun accept(v: Visitor): String = "\"${name}\": ${value},"
}