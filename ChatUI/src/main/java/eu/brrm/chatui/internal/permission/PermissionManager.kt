package eu.brrm.chatui.internal.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

interface PermissionCallback {
    fun onPermissionsResult(result: Map<String, Boolean>)

    fun onPermissionCanceled(message: String)
}

class PermissionManager(private val activity: ComponentActivity) {

    private val contract = ActivityResultContracts.RequestMultiplePermissions()

    private var permissionCallback: PermissionCallback? = null

    private val permissionLauncher = activity.registerForActivityResult(
        contract
    ) { result ->
        result?.let {
            permissionCallback?.onPermissionsResult(it)
        }
    }

    fun checkPermissions(permissions: List<String>): Map<String, Boolean> {
        val result = permissions.mapIndexed { _, s ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val status = activity.checkSelfPermission(s)
                (s to (status == PackageManager.PERMISSION_GRANTED))
            } else {
                (s to true)
            }
        }.toMap()

        return result
    }

    fun requestPermissions(
        permissions: List<String>,
        permissionCallback: PermissionCallback? = null
    ) {
        this.permissionCallback = permissionCallback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            val result = permissions.associate {
                (it to true)
            }
            this.permissionCallback?.onPermissionsResult(result)
        }
    }

    fun permissionCanceled(message: String) {
        permissionCallback?.onPermissionCanceled(message)
    }
}