# Read me

**Current version**
[![](https://jitpack.io/v/cybertechglobal/Chat_SDK_Android.svg)](https://jitpack.io/#cybertechglobal/Chat_SDK_Android)

#### How to

**To get a Git project into your build:**

### [+] Step 1. Add the JitPack repository to your build file
```
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
      mavenCentral()
      maven { url 'https://jitpack.io' }
    }
}
 ```

### [+] Step 2. Add the dependency

```
dependencies {
    implementation 'com.github.cybertechglobal:Chat_SDK_Android:X.X.X'
}
```

### [+] Step 3. Set custom notification icon and color

    Insert following snippet in the AndroidManifest.xml. 
	`ic_chat_icon` and `ic_chat_icon_color` are resources defined in the respective folders.

```
<meta-data
          android:name="eu.brrm.chatui.default_notification_icon"
          android:resource="@drawable/ic_chat_icon" />
        <meta-data
            android:name="eu.brrm.chatui.default_notification_color"
            android:resource="@color/ic_chat_icon_color" />
```