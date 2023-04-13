package com.sopt.peekabookaos.presentation.recommend

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.sopt.peekabookaos.R
import com.sopt.peekabookaos.databinding.FragmentRecommendBinding
import com.sopt.peekabookaos.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendFragment : BindingFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {
    private val recommendViewModel: RecommendViewModel by viewModels()
    private val recommendAdapter: BookRecommendAdapter?
        get() = binding.rvRecommend.adapter as? BookRecommendAdapter

    override fun onResume() {
        super.onResume()
        recommendViewModel.getRecommend()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = recommendViewModel
        initTextAppearance()
        initAdapter()
        initRecommendBookObserve()
        initRecommendedClickListener()
        initRecommendingClickListener()
    }

    private fun initRecommendedClickListener() {
        binding.tvRecommendRecommended.setOnClickListener {
            recommendAdapter?.submitList(recommendViewModel.recommendedBook.value)
            with(binding) {
                tvRecommendRecommended.isSelected = true
                tvRecommendRecommending.isSelected = false
                if (tvRecommendRecommended.isSelected) {
                    tvRecommendRecommending.setTextAppearance(R.style.H4)
                    tvRecommendRecommended.setTextAppearance(R.style.NameBd)
                }
                recommendedEmpty()
            }
        }
    }

    private fun initRecommendingClickListener() {
        binding.tvRecommendRecommending.setOnClickListener {
            recommendAdapter?.submitList(recommendViewModel.recommendingBook.value)
            with(binding) {
                tvRecommendRecommended.isSelected = false
                tvRecommendRecommending.isSelected = true
                if (tvRecommendRecommending.isSelected) {
                    tvRecommendRecommending.setTextAppearance(R.style.NameBd)
                    tvRecommendRecommended.setTextAppearance(R.style.H4)
                }
                recommendingEmpty()
            }
        }
    }

    private fun recommendedEmpty() {
        with(binding) {
            if (requireNotNull(recommendViewModel.recommendedBook.value).isEmpty()) {
                tvRecommendRecommendedEmpty.visibility = View.VISIBLE
                tvRecommendRecommendingEmpty.visibility = View.INVISIBLE
            } else {
                tvRecommendRecommendedEmpty.visibility = View.GONE
                tvRecommendRecommendingEmpty.visibility = View.GONE
            }
        }
    }

    private fun recommendingEmpty() {
        with(binding) {
            if (requireNotNull(recommendViewModel.recommendingBook.value).isEmpty()) {
                tvRecommendRecommendingEmpty.visibility = View.VISIBLE
                tvRecommendRecommendedEmpty.visibility = View.INVISIBLE
            } else {
                tvRecommendRecommendingEmpty.visibility = View.GONE
                tvRecommendRecommendedEmpty.visibility = View.GONE
            }
        }
    }

    private fun initTextAppearance() {
        with(binding) {
            tvRecommendRecommended.isSelected = true
            tvRecommendRecommended.setTextAppearance(R.style.NameBd)
        }
    }

    private fun initAdapter() {
        binding.rvRecommend.adapter = BookRecommendAdapter()
    }

    private fun initRecommendBookObserve() {
        recommendViewModel.recommendedBook.observe(viewLifecycleOwner) { book ->
            recommendAdapter?.submitList(book)
            recommendedEmpty()
        }
    }
}
