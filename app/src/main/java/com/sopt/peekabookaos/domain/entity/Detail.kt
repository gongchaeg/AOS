package com.sopt.peekabookaos.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
    val description: String? = null,
    val memo: String? = null,
    val book: Book = Book()
) : Parcelable
