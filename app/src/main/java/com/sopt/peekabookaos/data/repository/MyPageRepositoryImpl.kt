package com.sopt.peekabookaos.data.repository

import com.sopt.peekabookaos.data.source.remote.MyPageDataSource
import com.sopt.peekabookaos.domain.entity.User
import com.sopt.peekabookaos.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageDataSource: MyPageDataSource
) : MyPageRepository {
    override suspend fun getMyPage(): Result<User> =
        kotlin.runCatching { myPageDataSource.getMyPage() }.map { response ->
            requireNotNull(response.data).toUser()
        }
}
