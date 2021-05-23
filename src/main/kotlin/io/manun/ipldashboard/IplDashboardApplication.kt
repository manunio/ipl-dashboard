package io.manun.ipldashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IplDashboardApplication

fun main(args: Array<String>) {
    runApplication<IplDashboardApplication>(*args)
}


//@Configuration
//class Config {
//    @Bean
//    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
//        val initializer = ConnectionFactoryInitializer()
//        initializer.setConnectionFactory(connectionFactory)
//        return initializer
//    }

//    @Bean
//    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
//        return JdbcTemplate(dataSource)
//    }

//}