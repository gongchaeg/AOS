package com.sopt.peekabookaos.presentation.book

import android.app.Activity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.sopt.peekabookaos.R
import com.sopt.peekabookaos.databinding.ActivityBookBinding
import com.sopt.peekabookaos.util.binding.BindingActivity
import com.sopt.peekabookaos.util.extensions.activityTransition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookActivity : BindingActivity<ActivityBookBinding>(R.layout.activity_book) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSearchFragmentView()
    }

    private fun initSearchFragmentView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_book) as NavHostFragment
        val navController = navHostFragment.navController

        when (intent.getStringExtra(LOCATION) ?: CREATE) {
            RECOMMEND -> {
                navController.navigate(R.id.action_barcodeScannerFragment_to_searchBookFragment)
                activityTransition(
                    Activity.OVERRIDE_TRANSITION_OPEN,
                    R.animator.anim_from_right,
                    R.animator.anim_to_left
                )
            }

            else -> {
                navController.navigate(R.id.barcodeScannerFragment)
                activityTransition(
                    Activity.OVERRIDE_TRANSITION_OPEN,
                    R.animator.anim_from_bottom,
                    R.animator.anim_to_top
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        activityTransition(
            Activity.OVERRIDE_TRANSITION_CLOSE,
            R.animator.anim_from_left,
            R.animator.anim_to_right
        )
    }

    companion object {
        const val UPDATE = "update"
        const val BOOK_COMMENT = "book_comment"
        const val RECOMMEND = "recommend"
        const val CREATE = "create"
        const val BOOK_INFO = "book_info"
        const val LOCATION = "location"
        const val FRIEND_INFO = "friend_info"
        const val BOOK_ID = "book_id"
    }
}
