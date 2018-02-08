# vertxk

Vertxk is a set of tools to simplify [Vertx](http://vertx.io/) application development in [Kotlin Programming Language](https://kotlinlang.org/) 

## VertxK: Dependency injection with [Kodein](https://github.com/SalomonBrys/Kodein)
[Kodein](https://github.com/SalomonBrys/Kodein) is a simple, easy to use and easy to configure dependency retrieval container.

By default, Vertx does not provide any real support for dependency injection framerworks. 

Vertxk solves this issue enabling simple DI in Vertx Verticles through Kodein. 

BTW, it works with coroutines too!

Some Features
-------------
- Only 20Kb 
- No external dependencies (well... it requires Vertx and Kodein of course!)
- Works with Vertx 3.5+ and Kodein 4.1+ 

Getting Started
---------------

1. To get started, add vertxk-kodein dependency to your project:
 
```xml
        <dependency>
            <groupId>com.ufoscout.vertxk</groupId>
            <artifactId>vertxk-kodein</artifactId>
            <version>${vertxk.version}</version>
        </dependency>
```

2. Inject the VertxkKodein Module in your Kodein Container:

```Kotlin
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.jxinject.jxInjectorModule
import com.ufoscout.vertxk.VertxkKodein
...

        // setup vertx
        val vertx = Vertx.vertx()

        val kodein = Kodein {
            import(jxInjectorModule)
            import(VertxkKodein.module(vertx))
            import(// IMPORT YOUR MODULES)
        }

        VertxkKodein.registerFactory(vertx, kodein)

        awaitResult<String> {
            VertxkKodein.deployVerticle<MainVerticle>(vertx, it)
        }
```

3. Register vertxk Factory in Vertx:

```Kotlin
        VertxkKodein.registerFactory(vertx, kodein)
```

4. Everything's ready now! You can now inject whatever bean in your Verticles:

```Kotlin
// In this example I use a Coroutine based Verticle. 
// It works with standard verticles too.
// WARN: To use the coroutines you need to import the vertx-lang-kotlin-coroutines dependency
class MyVerticle (val myServiceOne: MyServiceOne, val myServiceTwo: MyServiceTwo) : CoroutineVerticle() {

    override suspend fun start() {
        println("Start main verticle with injected services!!!!")
    }

} 
```

5. Deploy your verticles and have fun :)
```Kotlin
    // With coroutines. it works even with default async deploy.   
    awaitResult<String> {
        VertxkKodein.deployVerticle<MyVerticle>(vertx, it)
    }
```

The complete example
--------------------
Let's put all together.

Creata a Verticle and inject your services:

```Kotlin
class MyVerticle (val myServiceOne: MyServiceOne, val myServiceTwo: MyServiceTwo) : CoroutineVerticle() {

    override suspend fun start() {
        println("Start main verticle with injected services!!!!")
    }

} 
```

Instantiate Vertx and Kodein:

```Kotlin
        val vertx = Vertx.vertx()

        val kodein = Kodein {
            import(jxInjectorModule)
            import(VertxkKodein.module(vertx))
            import(// IMPORT YOUR MODULES)
        }

        VertxkKodein.registerFactory(vertx, kodein)

        awaitResult<String> {
            VertxkKodein.deployVerticle<MyVerticle>(vertx, it)
        }
```

Cheers.

A complete working exmple can be found at [https://github.com/ufoscout/kotlin-vertx3](https://github.com/ufoscout/kotlin-vertx3)

