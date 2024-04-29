package eu.brrm.chatui.internal.network

internal enum class Method(val methodName: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"), ;

    override fun toString(): String {
        return "Method(methodName='$methodName')"
    }
}