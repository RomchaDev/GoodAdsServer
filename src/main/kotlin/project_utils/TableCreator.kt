package project_utils

fun main() {

    val input = "package model.entity.user\n" +
            "\n" +
            "import model.entity.ad.Ad\n" +
            "import project_utils.NoArg\n" +
            "import javax.persistence.*\n" +
            "\n" +
            "@Entity\n" +
            "@NoArg\n" +
            "@Table(\n" +
            "    name = \"users\",\n" +
            "    uniqueConstraints = [UniqueConstraint(columnNames = [\"username\"])]\n" +
            ")\n" +
            "open class DatabaseUser(\n" +
            "    @Id\n" +
            "    @Column(name = \"id\", nullable = false) var id: Long,\n" +
            "    @Column(unique = true) var username: String,\n" +
            "    @Column var password: String,\n" +
            "    @Column var postPrice: String,\n" +
            "    @Column var storyPrice: String,\n" +
            "    @Column var cardNumber: String?,\n" +
            "    @Column var adId: Long? = null,\n" +
            "    @OneToMany(targetEntity = Ad::class, mappedBy = \"adId\", fetch = FetchType.LAZY)\n" +
            "    @Column var ads: MutableList<Ad>? = null\n" +
            ") "

    val types = mapOf(
        "String" to "VARCHAR(25)",
        "Int" to "INTEGER",
        "Long" to "NUMERIC"
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