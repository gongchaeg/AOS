package com.sopt.peekabookaos.data.entity.response

import com.sopt.peekabookaos.data.entity.BooksEntity
import com.sopt.peekabookaos.data.entity.FriendListEntity
import com.sopt.peekabookaos.data.entity.PicksEntity
import com.sopt.peekabookaos.data.entity.SelfIntroEntity
import com.sopt.peekabookaos.domain.entity.FriendShelf
import kotlinx.serialization.Serializable

@Serializable
data class FriendShelfResponse(
    val myIntro: SelfIntroEntity,
    val friendList: List<FriendListEntity>,
    val friendIntro: SelfIntroEntity,
    val picks: List<PicksEntity>,
    val bookTotalNum: Int,
    val books: List<BooksEntity>
) {
    fun toFriendShelf(): FriendShelf = FriendShelf(
        myIntro = this.myIntro.toSelfIntro(),
        friendList = this.friendList.map { friendListEntity ->
            friendListEntity.toFriendList()
        },
        friendIntro = this.friendIntro.toSelfIntro(),
        picks = this.picks.map { picksEntity ->
            picksEntity.toPicks()
        },
        bookTotalNum = this.bookTotalNum,
        books = this.books.map { booksEntity ->
            booksEntity.toBooks()
        }
    )
}
