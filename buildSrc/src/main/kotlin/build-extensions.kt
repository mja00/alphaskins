import org.gradle.api.Project
import org.gradle.kotlin.dsl.expand
import org.gradle.language.jvm.tasks.ProcessResources

val Project.mod: ModData get() = ModData(this)
fun Project.prop(key: String): String? = findProperty(key)?.toString()
fun String.upperCaseFirst() = replaceFirstChar { it.uppercaseChar() }

fun ProcessResources.properties(files: Iterable<String>, vararg properties: Pair<String, Any>) {
    for ((name, value) in properties) inputs.property(name, value)
    filesMatching(files) {
        expand(properties.toMap())
    }
}

@JvmInline
value class ModData(private val project: Project) {
    val id: String get() = req("mod.id")
    val name: String get() = req("mod.name")
    val version: String get() = req("mod.version")
    val group: String get() = req("mod.group")
    val description: String get() = req("mod.description")
    val author: String get() = req("mod.author")
    val license: String get() = req("mod.license")

    fun prop(key: String): String = req("mod.$key")
    fun prop(key: String, fallback: String): String = project.prop("mod.$key") ?: fallback
    fun dep(key: String): String = req("deps.$key")

    private fun req(key: String): String =
        requireNotNull(project.prop(key)) { "Missing '$key'" }
}
