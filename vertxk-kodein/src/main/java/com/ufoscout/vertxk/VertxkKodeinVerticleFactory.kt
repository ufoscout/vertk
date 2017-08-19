package com.ufoscout.vertxk

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.jxinject.jx
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory

internal class VertxkKodeinVerticleFactory(val kodein: Kodein) : VerticleFactory {

    override fun prefix(): String {
        return VertxkKodein.PREFIX
    }

    override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
        val className = VerticleFactory.removePrefix(verticleName)

        /*
        Class clazz;
        if (verticleName.endsWith(".java")) {
            CompilingClassLoader compilingLoader = new CompilingClassLoader(classLoader, verticleName);
            String className = compilingLoader.resolveMainClassName();
            clazz = compilingLoader.loadClass(className);
        } else {
            clazz = classLoader.loadClass(verticleName);
        }
        */

        val clazz = classLoader.loadClass(className)

        return kodein.jx.newInstance(clazz) as Verticle

    }

}