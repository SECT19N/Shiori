package com.section.shiori.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val listSelected: ImageVector
    get() {
        if (_list != null) {
            return _list!!
        }
        _list =
            ImageVector.Builder(
                name = "list",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(8.14f, 8.86f)
                        quadTo(7.66f, 8.86f, 7.34f, 8.53f)
                        reflectiveQuadTo(7.01f, 7.72f)
                        reflectiveQuadTo(7.34f, 6.92f)
                        reflectiveQuadTo(8.14f, 6.59f)
                        horizontalLineTo(20.07f)
                        quadToRelative(0.48f, 0f, 0.81f, 0.33f)
                        reflectiveQuadTo(21.2f, 7.72f)
                        reflectiveQuadTo(20.87f, 8.53f)
                        reflectiveQuadTo(20.07f, 8.86f)
                        horizontalLineTo(8.14f)
                        close()
                        moveToRelative(0f, 4.28f)
                        quadToRelative(-0.48f, 0f, -0.81f, -0.33f)
                        reflectiveQuadTo(7.01f, 12f)
                        reflectiveQuadTo(7.34f, 11.19f)
                        reflectiveQuadTo(8.14f, 10.86f)
                        horizontalLineTo(20.07f)
                        quadToRelative(0.48f, 0f, 0.81f, 0.33f)
                        reflectiveQuadTo(21.2f, 12f)
                        reflectiveQuadToRelative(-0.33f, 0.81f)
                        reflectiveQuadToRelative(-0.81f, 0.33f)
                        horizontalLineTo(8.14f)
                        close()
                        moveToRelative(0f, 4.28f)
                        quadToRelative(-0.48f, 0f, -0.81f, -0.33f)
                        reflectiveQuadTo(7.01f, 16.28f)
                        reflectiveQuadTo(7.34f, 15.47f)
                        reflectiveQuadTo(8.14f, 15.14f)
                        horizontalLineTo(20.07f)
                        quadToRelative(0.48f, 0f, 0.81f, 0.33f)
                        reflectiveQuadToRelative(0.33f, 0.81f)
                        reflectiveQuadToRelative(-0.33f, 0.81f)
                        reflectiveQuadToRelative(-0.81f, 0.33f)
                        horizontalLineTo(8.14f)
                        close()
                        moveTo(3.93f, 8.86f)
                        quadTo(3.45f, 8.86f, 3.12f, 8.54f)
                        reflectiveQuadTo(2.8f, 7.73f)
                        reflectiveQuadTo(3.12f, 6.92f)
                        reflectiveQuadTo(3.93f, 6.59f)
                        reflectiveQuadTo(4.74f, 6.92f)
                        reflectiveQuadTo(5.07f, 7.73f)
                        quadToRelative(0f, 0.48f, -0.33f, 0.81f)
                        reflectiveQuadTo(3.93f, 8.86f)
                        close()
                        moveToRelative(0f, 4.28f)
                        quadToRelative(-0.48f, 0f, -0.81f, -0.33f)
                        reflectiveQuadTo(2.8f, 11.99f)
                        reflectiveQuadTo(3.12f, 11.19f)
                        reflectiveQuadTo(3.93f, 10.86f)
                        reflectiveQuadToRelative(0.81f, 0.32f)
                        reflectiveQuadToRelative(0.33f, 0.81f)
                        reflectiveQuadTo(4.74f, 12.81f)
                        reflectiveQuadTo(3.93f, 13.14f)
                        close()
                        moveToRelative(0f, 4.27f)
                        quadToRelative(-0.48f, 0f, -0.81f, -0.33f)
                        reflectiveQuadTo(2.8f, 16.27f)
                        reflectiveQuadTo(3.12f, 15.46f)
                        reflectiveQuadTo(3.93f, 15.13f)
                        reflectiveQuadToRelative(0.81f, 0.33f)
                        reflectiveQuadToRelative(0.33f, 0.81f)
                        reflectiveQuadTo(4.74f, 17.08f)
                        reflectiveQuadTo(3.93f, 17.41f)
                        close()
                    }
                }
                .build()
        return _list!!
    }

private var _list: ImageVector? = null
