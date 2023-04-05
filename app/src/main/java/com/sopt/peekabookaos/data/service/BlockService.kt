package com.sopt.peekabookaos.data.service

import com.sopt.peekabookaos.data.entity.BaseResponse
import com.sopt.peekabookaos.data.entity.BlockEntity
import retrofit2.http.GET

interface BlockService {
    @GET("mypage/blocklist")
    suspend fun getBlock(): BaseResponse<List<BlockEntity>>
}
