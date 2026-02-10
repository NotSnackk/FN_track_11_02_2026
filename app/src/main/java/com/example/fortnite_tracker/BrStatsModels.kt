package com.example.fortnite_tracker.models

import com.google.gson.annotations.SerializedName

data class BrStatsResponse(
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("data")
    val data: BrStatsData? = null,
    @SerializedName("error")
    val error: String? = null
)

data class BrStatsData(
    @SerializedName("account")
    val account: BrAccount? = null,
    @SerializedName("stats")
    val stats: BrStats? = null
)

data class BrAccount(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null
)

data class BrStats(
    @SerializedName("all")
    val all: BrStatsModes? = null,
    @SerializedName("keyboardmouse")
    val keyboardMouse: BrStatsModes? = null,
    @SerializedName("gamepad")
    val gamepad: BrStatsModes? = null,
    @SerializedName("touch")
    val touch: BrStatsModes? = null
)

data class BrStatsModes(
    @SerializedName("overall")
    val overall: BrModeStats? = null,
    @SerializedName("solo")
    val solo: BrModeStats? = null,
    @SerializedName("duo")
    val duo: BrModeStats? = null,
    @SerializedName("squad")
    val squad: BrModeStats? = null
)

data class BrModeStats(
    @SerializedName("wins")
    val wins: Int? = null,
    @SerializedName("kills")
    val kills: Int? = null,
    @SerializedName("matches")
    val matches: Int? = null,
    @SerializedName("matchesPlayed")
    val matchesPlayed: Int? = null,
    @SerializedName("matchesplayed")
    val matchesPlayedLegacy: Int? = null
)
