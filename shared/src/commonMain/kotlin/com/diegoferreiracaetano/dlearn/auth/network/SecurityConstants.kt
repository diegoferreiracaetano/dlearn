package com.diegoferreiracaetano.dlearn.auth.network

/**
 * Constantes de segurança utilizadas na comunicação entre App e Servidor dentro do contexto de Auth.
 */
object SecurityConstants {
    /** Header que transporta o token de desafio validado ou o ID da transação. */
    const val HEADER_CHALLENGE_TOKEN = "X-Challenge-Token"
    
    /** Header utilizado para enviar o código OTP durante a verificação. */
    const val HEADER_OTP = "X-OTP"
    
    /** Header de controle interno para rastrear a transação do desafio. */
    const val HEADER_TRANSACTION_ID = "X-Transaction-Id"

    /** 
     * Header de preferência de desafio. 
     * Permite que o App sugira ao servidor qual método de MFA ele prefere (ex: OTP_SMS, BIOMETRIC).
     */
    const val HEADER_CHALLENGE_PREFERENCE = "X-Challenge-Preference"

}
