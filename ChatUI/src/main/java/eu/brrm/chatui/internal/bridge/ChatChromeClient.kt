package eu.brrm.chatui.internal.bridge

import android.Manifest
import android.os.Build
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import eu.brrm.chatui.internal.permission.PermissionCallback
import eu.brrm.chatui.internal.permission.PermissionManager

internal class ChatChromeClient(
    private val permissionManager: PermissionManager,
    private val callback: Callback? = null
) :
    WebChromeClient() {
    private val TAG = ChatChromeClient::class.java.name


    interface Callback {
        fun onPermissionCanceled(message: String)
    }

    private val microphonePermissions = listOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MODIFY_AUDIO_SETTINGS
    )

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        Log.d(TAG, "onProgressChanged - progress: $newProgress")
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d(
            TAG,
            "WebChromeConsole Log: ${consoleMessage?.lineNumber()} : ${consoleMessage?.message()}"
        )
        return true
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        Log.d(TAG, "onPermissionRequest - request: $request")
        val permission = request?.resources?.firstOrNull() ?: return

        when (permission) {
            PermissionRequest.RESOURCE_AUDIO_CAPTURE -> {
                requestAudioCapture(request)
            }
        }
    }

    private fun requestAudioCapture(request: PermissionRequest?) {
        val callback = object : PermissionCallback {
            override fun onPermissionsResult(result: Map<String, Boolean>) {
                handleMicrophonePermissionResult(request, result)
            }

            override fun onPermissionCanceled(message: String) {
                request?.deny()
                callback?.onPermissionCanceled(message)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = permissionManager.checkPermissions(microphonePermissions)
            val permissionsGranted = result.entries.all { it.value }
            if (permissionsGranted) {
                grantMicrophonePermission(request)
            } else {
                permissionManager.requestPermissions(
                    microphonePermissions,
                    callback
                )
            }
        } else {
            grantMicrophonePermission(request)
        }
    }

    override fun onPermissionRequestCanceled(request: PermissionRequest?) {
        val permission = request?.resources?.firstOrNull() ?: return
        when (permission) {
            PermissionRequest.RESOURCE_AUDIO_CAPTURE -> {
                permissionManager.permissionCanceled("Request to acquire recording audion permission was canceled.")
            }
        }
    }

    private fun handleMicrophonePermissionResult(
        request: PermissionRequest?,
        result: Map<String, Boolean>
    ) {
        val hasNonGrantedPermissions = result.entries.any { !it.value }
        if (hasNonGrantedPermissions) {
            request?.deny()
            permissionManager.permissionCanceled("Request to acquire recording audion permission was canceled.")
        } else {
            grantMicrophonePermission(request)
        }
    }

    private fun grantMicrophonePermission(request: PermissionRequest?) {
        request?.grant(
            arrayOf(
                PermissionRequest.RESOURCE_AUDIO_CAPTURE,
            )
        )
    }

}