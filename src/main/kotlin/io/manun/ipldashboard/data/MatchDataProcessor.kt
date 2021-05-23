package io.manun.ipldashboard.data

import io.manun.ipldashboard.model.Match
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import java.time.LocalDate

class MatchDataProcessor : ItemProcessor<MatchInput, Match> {

    private val log: Logger = LoggerFactory.getLogger(MatchDataProcessor::class.java)

    override fun process(input: MatchInput): Match {

        val firstInningsTeam: String
        val secondInningsTeam: String

        if ("bat" == input.toss_decision) {
            firstInningsTeam = input.toss_winner
            secondInningsTeam = if (input.toss_winner == input.team1) input.team2 else input.team1
        } else {
            secondInningsTeam = input.toss_winner
            firstInningsTeam = if (input.toss_winner == input.team1) input.team2 else input.team1
        }

        val match = Match(
            id = input.id.toLong(),
            city = input.city,
            date = LocalDate.parse(input.date),
            playerOfMatch = input.player_of_match,
            venue = input.venue,
            team1 = firstInningsTeam,
            team2 = secondInningsTeam,
            tossWinner = input.toss_winner,
            tossDecision = input.toss_decision,
            result = input.result,
            resultMargin = input.result_margin,
            umpire1 = input.umpire1,
            umpire2 = input.umpire2,
            matchWinner = input.winner
        )

        return match
    }
}