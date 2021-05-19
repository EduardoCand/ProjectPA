import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

@Target(AnnotationTarget.PROPERTY)
annotation class Inject
annotation class InjectAdd

class Injector {
    companion object {
        val map: MutableMap<String, MutableList<KClass<*>>> = mutableMapOf()

        init {
            val scanner = Scanner(File("di.properties"))
            while(scanner.hasNextLine()){
                val line = scanner.nextLine()
                val parts = line.split("=")
                val methods = parts[1].split(",")
                val methodsList: MutableList<KClass<*>> = ArrayList()
                methods.forEach {
                    methodsList.add(Class.forName(it).kotlin)
                }
                map[parts[0]] = methodsList
            }
            scanner.close()
        }

        fun <T:Any> create(type: KClass<T>) : T {
            val o: T = type.createInstance()
            type.declaredMemberProperties.forEach {
                if(it.hasAnnotation<Inject>()) {
                    it.isAccessible = true
                    val key = type.simpleName + "." + it.name
                    val obj = map[key]!!.first().createInstance()
                    (it as KMutableProperty<*>).setter.call(o, obj) //??
                }
                if (it.hasAnnotation<InjectAdd>()){
                    it.isAccessible = true
                    val key = type.simpleName + "." + it.name
                    val objs = it.getter.call(o) as MutableList<Any>
                    map[key]!!.forEach {
                        val obj = it.createInstance()
                        objs.add(obj)
                    }
                }
            }
            return o
        }
    }

}


