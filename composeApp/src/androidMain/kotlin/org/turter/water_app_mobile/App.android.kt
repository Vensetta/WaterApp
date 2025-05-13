package org.turter.water_app_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.koinInject
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import org.turter.water_app_mobile.di.androidModule
import org.turter.water_app_mobile.di.initKoin
import org.turter.water_app_mobile.presentation.root.DefaultRootComponent
import org.turter.water_app_mobile.presentation.root.RootContent

class AppActivity : ComponentActivity() {
    val codeAuthFlowFactory = AndroidCodeAuthFlowFactory(useWebView = true, webViewEpheremalSession = true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeAuthFlowFactory.registerActivity(this)

        initKoin {
            androidContext(this@AppActivity)
            androidLogger()
            modules(
                androidModule,
                module {
                    single<CodeAuthFlowFactory> { codeAuthFlowFactory }
                }
            )
        }

        enableEdgeToEdge()
        setContent {
            val rootComponentFactory: DefaultRootComponent.Factory = koinInject()
            RootContent(rootComponentFactory.create(defaultComponentContext()))
        }
    }
}
