package com.weweb.core.json

import com.ufoscout.vertxk.kodein.VertxKComponent
import java.io.OutputStream
import kotlin.reflect.KClass

/**
 *
 * @author Francesco Cina'
 */
interface JsonSerializerService: VertxKComponent {

    /**
     * Return the json representation of the Bean
     * @param obj
     * @return
     */
    fun toJson(obj: Any): String

    /**
     * Return the json representation of the Bean
     * @param obj
     */
    fun toJson(obj: Any, out: OutputStream)

    /**
     * Return the json representation of the Bean
     * WARN: it is slower than the other method!
     * @param obj
     * @return
     */
    fun toPrettyPrintedJson(obj: Any): String

    /**
     * Return the json representation of the Bean
     * WARN: it is slower than the other method!
     * @param obj
     */
    fun toPrettyPrintedJson(obj: Any, out: OutputStream)

    /**
      * Method to deserialize JSON content from given JSON content String.
      */
    fun <T: Any> fromJson(clazz: KClass<T>, json: String): T

}

/**
 * Method to deserialize JSON content from given JSON content String.
 */
inline fun <reified T: Any> JsonSerializerService.fromJson(json: String) = fromJson(T::class, json)