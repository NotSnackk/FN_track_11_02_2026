package com.example.fortnite_tracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fortnite_tracker.api.ApiClient
import com.example.fortnite_tracker.databinding.MainActivityBinding
import com.example.fortnite_tracker.models.BrModeStats
import com.example.fortnite_tracker.models.BrStats
import com.example.fortnite_tracker.models.BrStatsData
import com.example.fortnite_tracker.models.BrStatsModes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemePreferences.applyTheme(this)
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "onCreate")

        binding.themeToggle.isChecked = ThemePreferences.isDarkTheme(this)
        binding.themeToggle.setOnCheckedChangeListener { _, isChecked ->
            ThemePreferences.setDarkTheme(this, isChecked)
        }

        binding.error.visibility = View.GONE
        binding.loading.visibility = View.GONE

        val nickname = intent.getStringExtra("SEARCH_QUERY")
        if (!nickname.isNullOrBlank()) {
            binding.nickname.text = nickname
            clearStats()
            loadStats(nickname.trim())
        } else {
            Toast.makeText(this, "Brak nicku gracza", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadStats(nickname: String) {
        setLoading(true)
        clearError()

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) { fetchStats(nickname) }
            setLoading(false)

            if (result.error != null) {
                showError(result.error)
                return@launch
            }

            val stats = result.stats
            if (stats == null) {
                showError("Nie udalo sie pobrac statystyk gracza.")
                return@launch
            }

            displayStats(stats, result.displayName ?: nickname)
        }
    }

    private suspend fun fetchStats(nickname: String): StatsResult {
        return try {
            val statsResponse = ApiClient.retrofitService.getBrStats(nickname, accountType = "epic")
            if (!statsResponse.isSuccessful) {
                return StatsResult(error = "Blad komunikacji z API.")
            }

            val statsBody = statsResponse.body()
            if (statsBody?.status != null && statsBody.status != 200) {
                return StatsResult(error = statsBody.error ?: "Nie udalo sie pobrac statystyk gracza.")
            }

            val statsData = statsBody?.data
            if (statsData == null) {
                return StatsResult(error = "Nie udalo sie pobrac statystyk gracza.")
            }

            val displayName = statsData.account?.name ?: nickname
            StatsResult(stats = statsData, displayName = displayName)
        } catch (e: Exception) {
            StatsResult(error = "Blad komunikacji z API: ${e.message}")
        }
    }

    private fun displayStats(stats: BrStatsData, displayName: String) {
        clearError()
        binding.nickname.text = displayName

        val statsGroup = pickStatsGroup(stats.stats)
        val modeStats = pickModeStats(statsGroup)
        val modeName = pickModeName(statsGroup)

        if (modeStats == null || modeName == null) {
            showError("Brak statystyk do wyswietlenia.")
            clearStats()
            return
        }

        val matchesValue = modeStats.matches ?: modeStats.matchesPlayed ?: modeStats.matchesPlayedLegacy

        binding.textStatsTitle.text = "Statystyki $modeName"
        binding.kills.text = formatStat(modeStats.kills)
        binding.wins.text = formatStat(modeStats.wins)
        binding.matches.text = formatStat(matchesValue)
    }

    private fun pickStatsGroup(stats: BrStats?): BrStatsModes? {
        return stats?.all ?: stats?.keyboardMouse ?: stats?.gamepad ?: stats?.touch
    }

    private fun pickModeStats(statsGroup: BrStatsModes?): BrModeStats? {
        return statsGroup?.solo ?: statsGroup?.duo ?: statsGroup?.squad ?: statsGroup?.overall
    }

    private fun pickModeName(statsGroup: BrStatsModes?): String? {
        return when {
            statsGroup?.solo != null -> "Solo"
            statsGroup?.duo != null -> "Duo"
            statsGroup?.squad != null -> "Squad"
            statsGroup?.overall != null -> "Overall"
            else -> null
        }
    }

    private fun formatStat(value: Int?): String = value?.toString() ?: "-"

    private fun setLoading(loading: Boolean) {
        binding.loading.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        binding.error.text = message
        binding.error.visibility = View.VISIBLE
    }

    private fun clearError() {
        binding.error.text = ""
        binding.error.visibility = View.GONE
    }

    private fun clearStats() {
        binding.textStatsTitle.text = "Statystyki"
        binding.kills.text = "-"
        binding.wins.text = "-"
        binding.matches.text = "-"
    }

    private data class StatsResult(
        val stats: BrStatsData? = null,
        val displayName: String? = null,
        val error: String? = null
    )
}
