package com.diegoferreiracaetano.dlearn.domain.user

interface UseCase<in Params, out Result> {
    operator fun invoke(params: Params): Result
}