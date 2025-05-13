@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package waterappmobile.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi

private object CommonMainDrawable0 {
  public val ic_cyclone: DrawableResource by 
      lazy { init_ic_cyclone() }

  public val ic_dark_mode: DrawableResource by 
      lazy { init_ic_dark_mode() }

  public val ic_light_mode: DrawableResource by 
      lazy { init_ic_light_mode() }

  public val ic_rotate_right: DrawableResource by 
      lazy { init_ic_rotate_right() }
}

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("ic_cyclone", CommonMainDrawable0.ic_cyclone)
  map.put("ic_dark_mode", CommonMainDrawable0.ic_dark_mode)
  map.put("ic_light_mode", CommonMainDrawable0.ic_light_mode)
  map.put("ic_rotate_right", CommonMainDrawable0.ic_rotate_right)
}

internal val Res.drawable.ic_cyclone: DrawableResource
  get() = CommonMainDrawable0.ic_cyclone

private fun init_ic_cyclone(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:ic_cyclone",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/drawable/ic_cyclone.xml", -1, -1),
    )
)

internal val Res.drawable.ic_dark_mode: DrawableResource
  get() = CommonMainDrawable0.ic_dark_mode

private fun init_ic_dark_mode(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:ic_dark_mode",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/drawable/ic_dark_mode.xml", -1, -1),
    )
)

internal val Res.drawable.ic_light_mode: DrawableResource
  get() = CommonMainDrawable0.ic_light_mode

private fun init_ic_light_mode(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:ic_light_mode",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/drawable/ic_light_mode.xml", -1, -1),
    )
)

internal val Res.drawable.ic_rotate_right: DrawableResource
  get() = CommonMainDrawable0.ic_rotate_right

private fun init_ic_rotate_right(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:ic_rotate_right",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/drawable/ic_rotate_right.xml", -1, -1),
    )
)
