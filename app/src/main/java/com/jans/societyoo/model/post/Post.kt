package com.jans.societyoo.model.post

import java.math.BigInteger

data class Post (
    var id : BigInteger,
    var desc :String,
    var images: List<String>
)