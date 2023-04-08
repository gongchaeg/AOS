package com.sopt.peekabookaos.data.service

import com.sopt.peekabookaos.data.entity.BaseResponse
import com.sopt.peekabookaos.data.entity.SelfIntroEntity
import retrofit2.http.GET

interface MyPageService {
    @GET("mypage/profile")
    suspend fun getMyPage(): BaseResponse<List<SelfIntroEntity>>
}
