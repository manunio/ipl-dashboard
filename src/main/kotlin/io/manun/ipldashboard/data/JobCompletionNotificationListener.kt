package io.manun.ipldashboard.data

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component


@Component
class JobCompletionNotificationListener(
    @Autowired var jdbcTemplate: JdbcTemplate
) : JobExecutionListenerSupport() {

    private val log: Logger = LoggerFactory.getLogger(JobCompletionNotificationListener::class.java)

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status === BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results")
            jdbcTemplate.query(
                "SELECT team1,team2, date FROM match"
            ) { rs, _ -> "Team 1 " + rs.getString(1) + "Team 2 " + rs.getString(2) + "Date " + rs.getString(3) }
                .forEach { println(it) }
        }
    }
}