plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

minecraft {
    extraRunJvmArguments.addAll("-Xmx8G", "-Xms8G")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("org.ow2.asm:asm-commons:9.2")
}

tasks.withType<JavaCompile>().configureEach {
    options.annotationProcessorPath = configurations.annotationProcessor.get()
}

val runConfigs = listOf(
    "runClient" to "run/client_280",
    "runClient17" to "run/client17_280",
    "runClient21" to "run/client17_280",
    "runClient25" to "run/client17_280",
    "runServer" to "run/server_280",
    "runServer17" to "run/server17_280",
    "runServer21" to "run/server17_280",
    "runServer25" to "run/server17_280"
)

runConfigs.forEach { (taskName, path) ->
    tasks.named<JavaExec>(taskName) {
        workingDir = file("${projectDir}/$path")
        doFirst {
            workingDir.mkdirs()
        }
    }
}
