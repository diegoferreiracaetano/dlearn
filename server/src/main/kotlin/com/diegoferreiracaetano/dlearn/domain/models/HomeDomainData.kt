package com.diegoferreiracaetano.dlearn.domain.models

import com.diegoferreiracaetano.dlearn.domain.video.Video

data class HomeDomainData(
    val banner: Video?,
    val top10: List<Video>,
    val popular: List<Video>,
    val categories: Map<String, List<Video>>,
)
