package io.manun.ipldashboard.model

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Match(
    @Id val id: Long,
    val city: String,
    val date: LocalDate,
    val playerOfMatch: String,
    val venue: String,
    val team1: String,
    val team2: String,
    val tossWinner: String,
    val tossDecision: String,
    val matchWinner: String,
    val result: String,
    val resultMargin: String,
    val umpire1: String,
    val umpire2: String
)
