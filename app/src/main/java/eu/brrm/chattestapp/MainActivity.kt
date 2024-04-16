package eu.brrm.chattestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import eu.brrm.chattestapp.databinding.ActivityMainBinding
import eu.brrm.chatui.BrrmChat
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser

class MainActivity : AppCompatActivity() {
    private val user1 = BrrmUser(
        id = "d736ecee-a78d-47f2-8a5e-bb9eabc1ffa3",
        email = "seki@catch.com",
        name = "Semsudin Tafilovic"
    )

    private val user2 = BrrmUser(
        id = "d736ece2-a78g-47f2-8a5e-bb9eabc1ffa3",
        email = "nikola@catch.com",
        name = "Nikola Denic"
    )

    private val user3 = BrrmUser(
        id = "c42256fd-a854-4e41-92f1-25becc76f97c",
        email = "chat1@house.com",
        name = "chat 1"
    )
    private val user4 = BrrmUser(
        id = "c42256fd-a854-4e41-92f1-25becc76f97c",
        email = "chat1@house.com",
        name = "chat 1"
    )
    private val group = BrrmGroup(
        id = "e92d4539-25ca-4a19-b0fc-34d6e9ba08d8",
        name = "CHAT TEST"
    )

    private val group2 = BrrmGroup(
        id = "e92d4539-25ca-4a19-b0fc-34d6e9ba08d8",
        name = "CHAT TEST"
    )

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenChatList1.let { button ->
            button.setOnClickListener {
                openChat1()
            }
        }

        binding.btnOpenChatList2.let { button ->
            button.setOnClickListener {
                openChat2()
            }
        }

        binding.btnOpenChatList3.let { button ->
            button.setOnClickListener {
                openChat3()
            }
        }

        binding.btnOpenChatList4.let { button ->
            button.setOnClickListener {
                openChat4()
            }
        }
    }

    private fun openChat1() {
        BrrmChat.instance.setUser(user1)
        BrrmChat.instance.setGroup(group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openChat2(){
        BrrmChat.instance.setUser(user2)
        BrrmChat.instance.setGroup(group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openChat3(){
        BrrmChat.instance.setUser(user3)
        BrrmChat.instance.setGroup(group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openChat4(){
        BrrmChat.instance.setUser(user4)
        BrrmChat.instance.setGroup(group2)
        BrrmChat.instance.openChatList(this)
    }

    private fun openTest() {
        startActivity(Intent(this, TestActivity::class.java))
    }
}