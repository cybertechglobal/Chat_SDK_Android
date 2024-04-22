package eu.brrm.chattestapp

import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser

object TestData {
    val user1 = BrrmUser(
        id = "0ff1074e-50d6-4b64-87aa-f14d83c2c088",
        email = "android1@myauto.com",
        name = "Android 1"
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
        id = "e92d4539-25ca-4a19-b0fc-34d6e9ba08d8",
        name = "CHAT TEST"
    )

    val group2 = BrrmGroup(
        id = "e92d4539-25ca-4a19-b0fc-34d6e9ba08d8",
        name = "CHAT TEST"
    )

}