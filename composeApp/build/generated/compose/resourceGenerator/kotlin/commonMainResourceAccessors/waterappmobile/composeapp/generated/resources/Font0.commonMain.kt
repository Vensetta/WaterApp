@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package waterappmobile.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi

private object CommonMainFont0 {
  public val IndieFlower_Regular: FontResource by 
      lazy { init_IndieFlower_Regular() }
}

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("IndieFlower_Regular", CommonMainFont0.IndieFlower_Regular)
}

internal val Res.font.IndieFlower_Regular: FontResource
  get() = CommonMainFont0.IndieFlower_Regular

private fun init_IndieFlower_Regular(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:IndieFlower_Regular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(), "composeResources/waterappmobile.composeapp.generated.resources/font/IndieFlower-Regular.ttf", -1, -1),
    )
)
