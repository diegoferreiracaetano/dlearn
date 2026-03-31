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
        val queryString = request.params
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
        metadata: Map<String, String>? = null
    ): String {
        return build(AppRequest(path = path, params = params, metadata = metadata))
    }

    /**
     * PARSE: Interpreta uma string de PATH e converte em um [AppRequest].
     */
    fun parse(fullPath: String?): AppRequest {
        if (fullPath.isNullOrBlank()) return AppRequest(path = "")

        val (pathPart, queryPart) = fullPath.split("?", limit = 2)
            .let { it[0] to it.getOrNull(1) }

        val queryMap = queryPart?.split("&")?.mapNotNull {
            val parts = it.split("=", limit = 2)
            if (parts.size == 2) parts[0] to parts[1] else null
        }?.toMap()

        return AppRequest(
            path = pathPart.trim('/'),
            params = queryMap
        )
    }

    /**
     * Atalho para parse usando invoke.
     */
    operator fun invoke(fullPath: String?): AppRequest = parse(fullPath)
}
