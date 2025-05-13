@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package waterappmobile.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource

private object CommonMainString0 {
  public val cyclone: StringResource by 
      lazy { init_cyclone() }

  public val open_github: StringResource by 
      lazy { init_open_github() }

  public val run: StringResource by 
      lazy { init_run() }

  public val stop: StringResource by 
      lazy { init_stop() }

  public val theme: StringResource by 
      lazy { init_theme() }
}

@InternalResourceApi
internal fun _collectCommonMainString0Resources(map: MutableMap<String, StringResource>) {
  map.put("cyclone", CommonMainString0.cyclone)
  map.put("open_github", CommonMainString0.open_github)
  map.put("run", CommonMainString0.run)
  map.put("stop", CommonMainString0.stop)
  map.put("theme", CommonMainString0.theme)
}

internal val Res.string.cyclone: StringResource
  get() = CommonMainString0.cyclone

private fun init_cyclone(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cyclone", "cyclone",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/values/strings.commonMain.cvr", 10, 27),
    )
)

internal val Res.string.open_github: StringResource
  get() = CommonMainString0.open_github

private fun init_open_github(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:open_github", "open_github",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/values/strings.commonMain.cvr", 38, 35),
    )
)

internal val Res.string.run: StringResource
  get() = CommonMainString0.run

private fun init_run(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:run", "run",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/values/strings.commonMain.cvr", 74, 15),
    )
)

internal val Res.string.stop: StringResource
  get() = CommonMainString0.stop

private fun init_stop(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:stop", "stop",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/values/strings.commonMain.cvr", 90, 20),
    )
)

internal val Res.string.theme: StringResource
  get() = CommonMainString0.theme

private fun init_theme(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:theme", "theme",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/values/strings.commonMain.cvr", 111, 21),
    )
)
