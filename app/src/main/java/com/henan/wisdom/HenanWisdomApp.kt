package com.henan.wisdom

import android.app.Application
import com.henan.wisdom.core.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp

/**
 * 智慧河南 Application
 * 使用 Hilt 进行依赖注入
 */
@HiltAndroidApp
class HenanWisdomApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // 初始化数据库
        AppDatabase.getInstance(this)
    }
}