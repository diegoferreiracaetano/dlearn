package com.diegoferreiracaetano.dlearn.navigation

import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest

/**
 * Utilitário para construção e parse de caminhos (paths) de navegação.
 * Utiliza [AppRequest] como contrato único entre Backend e Frontend.
 */
object AppPath {
    /**
     * Constrói uma string de PATH a partir de um [AppRequest].
     */
    fun build(request: AppRequest): String {
        val path = request.path.trim('/')
        val queryString =
            request.params
                ?.filter { it.key.isNotBlank() && it.value.isNotBlank() }
                ?.entries
                ?.joinToString("&") { "${it.key}=${it.value}" }

        return if (!queryString.isNullOrEmpty()) "$path?$queryString" else path
    }

    /**
     * BUILD: Constrói uma string de PATH formatada.
     */
    operator fun invoke(
        path: String,
        params: Map<String, String>? = null,
        metadata: Map<String, String>? = null,
    ): String = build(parse(path, params, metadata))

    /**
     * PARSE: Interpreta uma string de PATH e converte em um [AppRequest].
     */
    fun parse(
        fullPath: String?,
        params: Map<String, String>? = null,
        metadata: Map<String, String>? = null,
    ): AppRequest {
        if (fullPath.isNullOrBlank()) return AppRequest(path = "", params = params, metadata = metadata)

        val (pathPart, queryPart) =
            fullPath
                .split("?", limit = 2)
                .let { it[0] to it.getOrNull(1) }

        val queryMap =
            queryPart
                ?.split("&")
                ?.mapNotNull {
                    val parts = it.split("=", limit = 2)
                    if (parts.size == 2) parts[0] to parts[1] else null
                }?.toMap()

        val combinedParams =
            when {
                queryMap != null && params != null -> queryMap + params
                queryMap != null -> queryMap
                else -> params
            }

        return AppRequest(
            path = pathPart.trim('/'),
            params = combinedParams,
            metadata = metadata,
        )
    }
}
