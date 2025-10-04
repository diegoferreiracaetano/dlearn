package com.diegoferreiracaetano.dlearn.domain.pokedex

import com.diegoferreiracaetano.dlearn.data.util.OrderType
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun list(
        search: String,
        type: String,
        order: OrderType,
    ): Flow<List<Pokemon>>

    fun detail(id: Int): Flow<Pokemon>
}
