import androidx.compose.ui.window.ComposeUIViewController
import org.turter.water_app_mobile.App
import org.turter.water_app_mobile.di.initKoin
import org.turter.water_app_mobile.di.iosModule
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        initKoin {
            modules(iosModule)
        }
    }
) { App() }
