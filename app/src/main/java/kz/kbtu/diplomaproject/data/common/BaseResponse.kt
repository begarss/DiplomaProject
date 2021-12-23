package kz.kbtu.diplomaproject.data.common

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data") val data: T,
    @SerializedName("error") val error: String?
) {

    fun isError(): Boolean = error != null

    fun isSuccessful(): Boolean {
        if (error == null) {
            return true
        }
        return false
    }
}
