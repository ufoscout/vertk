package com.ufoscout.vertxk

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.jxinject.jxInjectorModule
import com.github.salomonbrys.kodein.singleton
import com.ufoscout.vertxk.stub.SimpleVerticle
import com.ufoscout.vertxk.stub.VerticleWithDependencies
import org.junit.Assert
import org.junit.Test
import java.util.*

class VertxkKodeinVerticleFactoryTest: BaseTest() {

    @Test
    fun shouldReturnTheExpectedPrefix() {
        val kodein = Kodein {}
        val factory = VertxkKodeinVerticleFactory(kodein)
        Assert.assertEquals(VertxkKodein.PREFIX, factory.prefix())
    }

    @Test
    fun shouldCreateASimpleVerticleInstance() {
        val kodein = Kodein {
            import(jxInjectorModule)
        }

        val factory = VertxkKodeinVerticleFactory(kodein)

        val instance = factory.createVerticle(SimpleVerticle::class.java!!.getName(), this.javaClass.classLoader)
        Assert.assertNotNull(instance)
        Assert.assertTrue(instance is SimpleVerticle)
    }

    @Test
    fun shouldCreateAVerticleInstanceWithDependencies() {
        val name = UUID.randomUUID().toString()
        val kodein = Kodein {
            import(jxInjectorModule)
            bind<String>() with singleton { name }
        }

        val factory = VertxkKodeinVerticleFactory(kodein)

        val instance = factory.createVerticle(VerticleWithDependencies::class.java!!.getName(), this.javaClass.classLoader)
        Assert.assertNotNull(instance)
        Assert.assertTrue(instance is VerticleWithDependencies)

        Assert.assertEquals(name, (instance as VerticleWithDependencies).name)

    }

}