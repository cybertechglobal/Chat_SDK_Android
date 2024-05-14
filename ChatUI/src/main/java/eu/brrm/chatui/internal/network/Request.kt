package eu.brrm.chatui.internal.network


internal abstract class Request(
    val path: String, val method: Method,
    val requestBody: RequestBody? = null,
    val queryParams: Map<String, *> = emptyMap<String, String>(),
    val headers: Map<String, *> = emptyMap<String, String>(),
    val doInput: Boolean = true,
    val doOutput: Boolean = true,
) {

    internal class Post(
        path: String,
        requestBody: RequestBody?,
        queryParams: Map<String, *> = emptyMap<String, String>(),
        headers: Map<String, *> = emptyMap<String, String>()
    ) : Request(path, Method.POST, requestBody,queryParams, headers)

    internal class Get(
        path: String,
        queryParams: Map<String, *> = emptyMap<String, String>(),
        headers: Map<String, *> = emptyMap<String, String>()
    ) : Request(path, Method.GET, null, queryParams, headers)

    override fun toString(): String {
        return "Request(path='$path', method=$method, requestBody=$requestBody, queryParams=$queryParams, headers=$headers, doInput=$doInput, doOutput=$doOutput)"
    }


}