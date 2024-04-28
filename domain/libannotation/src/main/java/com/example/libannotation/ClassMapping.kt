package com.example.libannotation

abstract class ClassMapping<L, R> {
    abstract fun convert(data: L): R

    abstract fun revers(data: R): L

    //需要在写代码时提前预知生成的实现类的名字
    class Builder<T : ClassMapping<*, *>>(private val klass: Class<T>) {
        fun build(): T {
            val fullPackage = klass.getPackage()!!.name
            val name: String = klass.simpleName.toString()
            val implName = Ex.getGenClassName(klass.canonicalName)
            return try {
                val fullClassName = if (fullPackage.isEmpty()) {
                    implName
                } else {
                    "$fullPackage.$implName"
                }
                val aClass = Class.forName(
                    fullClassName, true, klass.classLoader
                ) as Class<T>
                aClass.getDeclaredConstructor().newInstance()
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(
                    "Cannot find implementation for ${klass.canonicalName}. $implName does not " +
                            "exist"
                )
            } catch (e: IllegalAccessException) {
                throw RuntimeException(
                    "Cannot access the constructor ${klass.canonicalName}"
                )
            } catch (e: InstantiationException) {
                throw RuntimeException(
                    "Failed to create an instance of ${klass.canonicalName}"
                )
            }
        }
    }
}