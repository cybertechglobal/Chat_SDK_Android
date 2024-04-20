package eu.brrm.chattestapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import eu.brrm.chattestapp.TestData.group
import eu.brrm.chattestapp.TestData.group2
import eu.brrm.chattestapp.TestData.user1
import eu.brrm.chattestapp.TestData.user2
import eu.brrm.chattestapp.TestData.user3
import eu.brrm.chattestapp.TestData.user4
import eu.brrm.chattestapp.databinding.ActivityMainBinding
import eu.brrm.chatui.BrrmChat
import eu.brrm.chatui.internal.data.BrrmGroup
import eu.brrm.chatui.internal.data.BrrmUser
import eu.brrm.chatui.internal.permission.PermissionCallback
import eu.brrm.chatui.internal.permission.PermissionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionManager = PermissionManager(this@MainActivity)
            permissionManager.requestPermissions(
                listOf(Manifest.permission.POST_NOTIFICATIONS),
                permissionCallback = object : PermissionCallback {
                    override fun onPermissionsResult(result: Map<String, Boolean>) {
                        if (result[Manifest.permission.POST_NOTIFICATIONS] == false) {
                            binding.root.children.forEach {
                                it.isEnabled = false
                            }
                        }
                    }

                    override fun onPermissionCanceled(message: String) {
                        binding.root.children.forEach {
                            it.isEnabled = false
                        }
                    }
                })
        }
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

    private fun openChat2() {
        BrrmChat.instance.setUser(user2)
        BrrmChat.instance.setGroup(group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openChat3() {
        BrrmChat.instance.setUser(user3)
        BrrmChat.instance.setGroup(group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openChat4() {
        BrrmChat.instance.setUser(user4)
        BrrmChat.instance.setGroup(group2)
        BrrmChat.instance.openChatList(this)
    }

    private fun openTest() {
        startActivity(Intent(this, TestActivity::class.java))
    }
}