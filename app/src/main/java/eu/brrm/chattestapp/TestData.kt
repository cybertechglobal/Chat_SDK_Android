package eu.brrm.chattestapp

import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser

object TestData {
    val user1 = BrrmUser(
        id = "e19af7c9-bb04-4dc0-bc78-551acc36f218",
        email = "sekiprod@user.com",
        name = "Semsudin Tafilovic"
    )

    val user2 = BrrmUser(
        id = "d736ece2-a78g-47f2-8a5e-bb9eabc1ffa3",
        email = "nikola@catch.com",
        name = "Nikola Denic"
    )

    val user3 = BrrmUser(
        id = "c42256fd-a854-4e41-92f1-25becc76f97c",
        email = "chat1@house.com",
        name = "chat 1"
    )
    val user4 = BrrmUser(
        id = "c42256fd-a854-4e41-92f1-25becc76f97c",
        email = "chat1@house.com",
        name = "chat 1"
    )
    val group = BrrmGroup(
        id = "1c0f2e00-6ae7-48a1-a1df-62c5e3138ad3",
        name = "TEST DEALERSHIP"
    )

    val group2 = BrrmGroup(
        id = "e92d4539-25ca-4a19-b0fc-34d6e9ba08d8",
        name = "CHAT TEST"
    )

}