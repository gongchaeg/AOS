package com.sopt.peekabookaos.presentation.userInput

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.sopt.peekabookaos.R
import com.sopt.peekabookaos.databinding.ActivityUserInputBinding
import com.sopt.peekabookaos.presentation.main.MainActivity
import com.sopt.peekabookaos.util.binding.BindingActivity
import com.sopt.peekabookaos.util.extensions.ToastMessageUtil
import com.sopt.peekabookaos.util.extensions.setSingleOnClickListener
import kotlin.system.exitProcess

class UserInputActivity : BindingActivity<ActivityUserInputBinding>(R.layout.activity_user_input) {
    private val viewModel: UserInputViewModel by viewModels()
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.updateProfileImage(uri)
        }
    }
    private var onBackPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initCheckClickListener()
        initDuplicateClickListener()
        initObserver()
        initProfileClickListener()
        initBackPressedCallback()
    }

    private fun initBackPressedCallback() {
        onBackPressedDispatcher.addCallback {
            if (System.currentTimeMillis() - onBackPressedTime >= WAITING_DEADLINE) {
                onBackPressedTime = System.currentTimeMillis()
                ToastMessageUtil.showToast(
                    this@UserInputActivity,
                    getString(R.string.finish_app_toast_msg)
                )
            } else {
                finishAffinity()
                System.runFinalization()
                exitProcess(0)
            }
        }
    }

    private fun initCheckClickListener() {
        binding.tvUserInputCheck.setSingleOnClickListener {
            if (viewModel.isNicknameDuplicate.value == !DUPLICATION) {
                val toMainActivity = Intent(this, MainActivity::class.java)
                startActivity(toMainActivity)
                finish()
            } else {
                viewModel.updateNicknameCheck()
            }
        }
    }

    private fun initDuplicateClickListener() {
        binding.tvUserInputDuplicationCheck.setSingleOnClickListener {
            viewModel.getDuplication()
        }
    }

    private fun initObserver() {
        viewModel.nickname.observe(this) {
            viewModel.updateButtonState()
        }
        viewModel.introduce.observe(this) {
            viewModel.updateButtonState()
        }
    }

    private fun initProfileClickListener() {
        binding.ivUserInputAdd.setSingleOnClickListener {
            launcher.launch("image/*")
        }
    }

    companion object {
        private const val DUPLICATION = true
        private const val WAITING_DEADLINE = 2000L
    }
}