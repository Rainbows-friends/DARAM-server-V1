package Rainbow_Frends.domain.time.presentation.dto.response

data class TimesResponse(
    val hoursUntilLimit: Long,
    val minutesUntilLimit: Long,
    val secondsUntilLimit: Long
)