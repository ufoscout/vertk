package com.ufoscout.vertk.kodein

import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.kodein.di.Kodein
import org.kodein.di.jxinject.jx

class VertkKodeinVerticleFactory(val kodein: Kodein) : VerticleFactory {

    companion object {
        val PREFIX = "vertk-kodein"
    }

    override fun prefix(): String {
        return PREFIX
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