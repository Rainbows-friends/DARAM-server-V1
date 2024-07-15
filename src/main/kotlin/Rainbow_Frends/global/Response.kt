package Rainbow_Frends.global

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response<T>(
    val success: String, val massage: String, val data: T?
)