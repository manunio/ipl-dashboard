package io.manun.ipldashboard.data

import io.manun.ipldashboard.model.Match
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource


@Configuration
@EnableBatchProcessing
class BatchConfig {

    private val fieldNames = arrayOf(
        "id",
        "city",
        "date",
        "player_of_match",
        "venue",
        "neutral_venue",
        "team1",
        "team2",
        "toss_winner",
        "toss_decision",
        "winner",
        "result",
        "result_margin",
        "eliminator",
        "method",
        "umpire1",
        "umpire2"
    )

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory


    @Bean
    fun reader(): FlatFileItemReader<MatchInput> {
        return FlatFileItemReaderBuilder<MatchInput>()
            .name("MatchItemReader")
            .resource(ClassPathResource("match-data.csv")).delimited().names(*fieldNames)
            .fieldSetMapper(object : BeanWrapperFieldSetMapper<MatchInput>() {
                init {
                    setTargetType(MatchInput::class.java)
                }
            })
            .build()
    }

    @Bean
    fun processor(): MatchDataProcessor {
        return MatchDataProcessor()
    }

    @Bean
    fun writer(dataSource: DataSource): JdbcBatchItemWriter<Match> {
        return JdbcBatchItemWriterBuilder<Match>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(
                "INSERT INTO match (id, city, date, player_of_match, venue, team1, team2, toss_winner, toss_decision, match_winner, result, result_margin, umpire1, umpire2) " +
                        "VALUES (:id, :city, :date, :playerOfMatch, :venue, :team1, :team2, :tossWinner, :tossDecision, :matchWinner, :result, :resultMargin, :umpire1, :umpire2)"
            )
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun importUserJob(listener: JobCompletionNotificationListener, step1: Step): Job {
        return jobBuilderFactory
            .get("importUserJob")
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build()
    }

    @Bean
    fun step1(writer: JdbcBatchItemWriter<Match>): Step {
        return stepBuilderFactory
            .get("step1")
            .chunk<MatchInput, Match>(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build()
    }

}