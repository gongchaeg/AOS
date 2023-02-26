package com.sopt.peekabookaos.presentation.book

import android.os.Bundle
import com.sopt.peekabookaos.R
import com.sopt.peekabookaos.databinding.ActivityBookBinding
import com.sopt.peekabookaos.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookActivity : BindingActivity<ActivityBookBinding>(R.layout.activity_book) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    companion object {
        const val UPDATE = "update"
        const val BOOK_COMMENT = "book_comment"
        const val RECOMMEND = "recommend"
        const val CREATE = "create"
        const val BOOK = "book"
        const val LOCATION = "location"
        const val BOOK_INFO = "book_info"
        const val FRIEND_INFO = "friend_info"
        const val BUNDLE = "bundle"
    }
}
