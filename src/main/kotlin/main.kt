fun main() {

    var test = JObject("test", parent = null)
    var testtest = JObject("testtest", parent = test)
    var testtesttest = JObject("testtesttest", parent = testtest)

    testtesttest.children = arrayOf(JString("nomestr1", "str1", testtesttest))

    testtest.children = arrayOf(JString("nomestr2", "str2", testtest), testtesttest)

    var objInArray = JObject("objInArray", parent = null)

    objInArray.children = arrayOf(JString("chave3", "valor3", objInArray))

    test.children = arrayOf(
        JString("age", "Nome", test),
        JNumber("lol", 20, test),
        JArray("array", arrayOf(
            JString("", "test1", null),
            objInArray,
            JNumber("", 1, null),
            JArray("array2", arrayOf(
                JString("", "test3", null),
                JNumber("", 2, null)), null),
            JArray("age", arrayOf(
                JString("", "test4", null),
                JArray("", arrayOf(
                    JString("", "test5", null)
                ), null)), null)), test),
        testtest)

    //println(toJsonString(testtest))
    //var numero = countObj(testtesttest)

    /*var listaStrs = getStrings(test)
    listaStrs.forEach{
        println(it.string)
    }*/

    //var nomes = getPropertie("age", test)
    //println(nomes)
    //println(numero)

    /*val obj = Teste()
    println(obj.age)
    println(toJsonString(toJson(obj)))*/

    //var file = FileTreeSkeleton(test).open(test)

    val w = Injector.create(FileTree::class)
    w.open(test)

}


/*
pega no objeto json e visita todos os childs
se o child for um leaf, da append a string para json string
se nao visita outros childs
 */


fun countObj(obj: JObject): Int{
    var count = 0
    obj.children.forEach{
        when(obj){
            is JObject -> count++
        }
    }

    return count
}

fun getPropertie(str: String, obj: JObject): Int{
    var count = 0

    fun aux(array: JArray){
        array.getJArray().forEach {
            if (it.name == str){
                count++
                if (it is JArray){
                    aux(it)
                }
                if (it is JObject){
                    getPropertie(str, it)
                }
            }
        }
    }

    obj.children.forEach {
        if(it.name == str){
            count++
            if (it is JArray){
                aux(it)
            }
            if (it is JObject){
                getPropertie(str, it)
            }
        }
    }
    return count
}

fun getStrings(obj: JObject): MutableList<JString> {
    val strList = mutableListOf<JString>()

    fun aux(array: JArray, list: MutableList<JString>){
        array.getJArray().forEach {
            when(it){
                is JString -> strList.add(it)
                is JObject -> getStrings(it)
                is JArray -> aux(it, list)
            }
        }
    }

    obj.children.forEach(){
        when(it){
            is JString -> strList.add(it)
            is JArray -> aux(it, strList)
            is JObject -> getStrings(it)
        }
    }

    return strList
}