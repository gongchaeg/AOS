package com.sopt.peekabookaos.presentation.bookshelf

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.sopt.peekabookaos.R
import com.sopt.peekabookaos.databinding.FragmentBookshelfBinding
import com.sopt.peekabookaos.presentation.barcodeScanner.BarcodeScannerActivity
import com.sopt.peekabookaos.presentation.notification.NotificationActivity
import com.sopt.peekabookaos.presentation.pickModify.PickModifyActivity
import com.sopt.peekabookaos.presentation.recommendation.RecommendationActivity
import com.sopt.peekabookaos.presentation.search.user.SearchUserActivity
import com.sopt.peekabookaos.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookshelfFragment : BindingFragment<FragmentBookshelfBinding>(R.layout.fragment_bookshelf) {
    private val myShelfAdapter: BookShelfShelfAdapter?
        get() = binding.rvBookshelfBottomViewShelf.adapter as? BookShelfShelfAdapter
    private val pickAdapter: BookShelfPickAdapter?
        get() = binding.rvBookshelfPick.adapter as? BookShelfPickAdapter
    private val friendAdapter: BookShelfFriendAdapter?
        get() = binding.rvBookshelfFriendList.adapter as? BookShelfFriendAdapter
    private lateinit var itemDeco: BookshelfShelfDecoration
    private val viewModel by viewModels<BookShelfViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initAdapter()
        initItemDecoration()
        initUserClickListener()
        initFriendPlusClickListener()
        initNotificationClickListener()
        initRecommendClickListener()
        initPickModifyClickListener()
        initCreateBookClickListener()
        initObserver()
    }

    private fun initAdapter() {
        binding.rvBookshelfBottomViewShelf.adapter = BookShelfShelfAdapter()
        myShelfAdapter?.submitList(viewModel.shelfData.value)
        binding.rvBookshelfPick.adapter = BookShelfPickAdapter()
        pickAdapter?.submitList(viewModel.pickData.value)
        binding.rvBookshelfFriendList.adapter = BookShelfFriendAdapter { pos, _ ->
            viewModel.updateShelfState(FRIEND)
            viewModel.updateUserId(pos)
            binding.ivBookshelfUserProfileRedline.visibility = View.INVISIBLE
            binding.tvBookshelfUserProfileName.setTextAppearance(R.style.S2Md)
        }
        friendAdapter?.submitList(viewModel.friendUserData.value)
    }

    private fun initItemDecoration() {
        itemDeco = BookshelfShelfDecoration(requireContext())
        binding.rvBookshelfBottomViewShelf.addItemDecoration(itemDeco)
    }

    private fun initUserClickListener() {
        binding.ivBookshelfUserProfile.setOnClickListener {
            binding.ivBookshelfUserProfileRedline.visibility = View.VISIBLE
            viewModel.updateShelfState(USER)
            friendAdapter?.clearSelection()
            binding.tvBookshelfUserProfileName.setTextAppearance(R.style.S1Bd)
        }
    }

    private fun initFriendPlusClickListener() {
        binding.btnBookshelfFriendPlus.setOnClickListener {
            val toSearchUser = Intent(requireActivity(), SearchUserActivity::class.java)
            startActivity(toSearchUser)
        }
    }

    private fun initNotificationClickListener() {
        binding.btnBookshelfNotification.setOnClickListener {
            val toNotification = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(toNotification)
        }
    }

    private fun initRecommendClickListener() {
        binding.btnBookshelfRecommend.setOnClickListener {
            val toRecommendation = Intent(requireActivity(), RecommendationActivity::class.java)
            startActivity(toRecommendation)
        }
    }

    private fun initPickModifyClickListener() {
        binding.btnBookshelfPickModify.setOnClickListener {
            val toPickModify = Intent(requireActivity(), PickModifyActivity::class.java)
            startActivity(toPickModify)
        }
    }

    private fun initCreateBookClickListener() {
        binding.btnBookshelfAddBook.setOnClickListener {
            val toBarcodeScanner = Intent(requireActivity(), BarcodeScannerActivity::class.java)
            startActivity(toBarcodeScanner)
        }
    }

    private fun initObserver() {
        viewModel.userId.observe(viewLifecycleOwner) {
            if (viewModel.friendShelf.value == FRIEND) {
                friendAdapter?.updateSelectedPosition(it)
            }
        }
    }

    companion object {
        const val FRIEND = true
        const val USER = false
    }
}
