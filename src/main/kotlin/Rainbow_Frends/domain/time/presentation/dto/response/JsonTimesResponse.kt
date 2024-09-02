package Rainbow_Frends.domain.time.presentation.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class JsonTimesResponse<T>(
    val success: String, val massage: String, val data: T?
)