package project_utils

fun main() {

    val input = "@Entity\n" +
            "@NoArg\n" +
            "@Table(name = \"users\")\n" +
            "open class DatabaseUser(\n" +
            "    @Id\n" +
            "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
            "    @Column(name = \"id\", nullable = false) var id: Long?,\n" +
            "    @Column var username: String?,\n" +
            "    @Column var password: String?,\n" +
            "    @Column var postPrice: String?,\n" +
            "    @Column var storyPrice: String?\n" +
            ")"

    val types = mapOf(
        "String" to "VARCHAR(25)",
        "Int" to "INTEGER",
        "Long" to "INTEGER"
    )

    val strings = input.split('\n')
    var res = "CREATE TABLE IF NOT EXISTS "

    strings.forEach { str ->

        if (str.contains("@Table(name = \"")) {
            res += str.split("@Table(name = \"")[1]
                .split("\")")[0]
            res += " (\n"
        }

        if (str.contains("var")) {
            val noVar = str.split("var")[1]
            val parts = noVar.split(": ")
            val name = parts[0]
            var type = "NOT_DEFINED"

            for (t in types)
                if (parts[1].contains(t.key)) type = t.value

            res += "    $name $type,\n"
        }
    }

    res = res.replaceFirst("INTEGER", "SERIAL PRIMARY KEY")

    val cInd = res.length - 2
    res = res.removeRange(cInd..cInd)

    res += ");"

    println(res)
}