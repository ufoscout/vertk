package com.ufoscout.vertk.kodein

import com.ufoscout.vertk.kodein.stub.SimpleVerticle
import com.ufoscout.vertk.kodein.stub.VerticleWithDependencies
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jxInjectorModule
import java.util.*

class VertxkKodeinVerticleFactoryTest {

    @Test
    fun shouldReturnTheExpectedPrefix() {
        val kodein = Kodein {}
        val factory = VertxkKodeinVerticleFactory(kodein)
        assertEquals(VertxkKodeinVerticleFactory.PREFIX, factory.prefix())
    }

    @Test
    fun shouldCreateASimpleVerticleInstance() {
        val kodein = Kodein {
            import(jxInjectorModule)
        }

        val factory = VertxkKodeinVerticleFactory(kodein)

        val instance = factory.createVerticle(SimpleVerticle::class.java!!.getName(), this.javaClass.classLoader)
        assertNotNull(instance)
        assertTrue(instance is SimpleVerticle)
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
        assertNotNull(instance)
        assertTrue(instance is VerticleWithDependencies)

        assertEquals(name, (instance as VerticleWithDependencies).name)

    }


}