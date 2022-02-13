package main

import model.datasource.dao.UniversalDAO
import model.entity.ad.Ad
import org.hibernate.cfg.Configuration
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class App {
}

fun main() {
    //SpringApplication.run(main.App::class.java)

    val factory = Configuration().configure().buildSessionFactory()
    val adsDAO = UniversalDAO<Ad, Int>(factory, Ad::class.java)

    adsDAO.create(
        Ad(
            1,
            123,
            "300",
            3,
            2
        )
    )
}