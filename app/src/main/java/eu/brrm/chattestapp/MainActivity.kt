package eu.brrm.chattestapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import eu.brrm.chattestapp.TestData.group
import eu.brrm.chattestapp.TestData.user1
import eu.brrm.chattestapp.TestData.user2
import eu.brrm.chattestapp.TestData.user3
import eu.brrm.chattestapp.TestData.user4
import eu.brrm.chattestapp.databinding.ActivityMainBinding
import eu.brrm.chatui.BrrmChat
import eu.brrm.chatui.internal.permission.PermissionCallback
import eu.brrm.chatui.internal.permission.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

        binding.btnRegisterAndSubscribe.setOnClickListener {
            registerAndSubscribe()
        }

        binding.btnOpenChatList1.setOnClickListener {
            openChat1()
        }

        binding.btnOpenChatList2.setOnClickListener {
            openChat2()
        }

        binding.btnOpenChatList3.setOnClickListener {
            openChat3()
        }

        binding.btnOpenChatList4.setOnClickListener {
            openChat4()
        }
    }

    private fun registerAndSubscribe() {
        lifecycleScope.launch(Dispatchers.IO) {
            val token = Firebase.messaging.token.await()
            BrrmChat.instance.register(user1, group, token)
        }
    }

    private fun openChat1() {
        lifecycleScope.launch(Dispatchers.IO) {
            val token = Firebase.messaging.token.await()
            BrrmChat.instance.register(user1, group, token)
            withContext(Dispatchers.Main) {
                BrrmChat.instance.openChatList(this@MainActivity)
            }
        }
    }

    private fun openChat2() {
        BrrmChat.instance.register(user2, group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openChat3() {
        BrrmChat.instance.register(user3, group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openChat4() {
        BrrmChat.instance.register(user4, group)
        BrrmChat.instance.openChatList(this)
    }

    private fun openTest() {
        startActivity(Intent(this, TestActivity::class.java))
    }
}