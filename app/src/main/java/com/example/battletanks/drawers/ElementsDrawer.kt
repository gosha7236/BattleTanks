package com.example.battletanks.drawers

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RemoteViews.RemoteViewOutlineProvider
import androidx.annotation.DrawableRes
import com.example.battletanks.CELL_SIZE
import com.example.battletanks.R
import com.example.battletanks.enums.Material
import com.example.battletanks.models.Coordinate
import com.example.battletanks.models.Element
import com.example.battletanks.utils.kt.getElementByCoordinates

class ElementsDrawer(val container:FrameLayout) {
    var currentMaterial = Material.EMPTY
    val elementsOnContainer = mutableListOf<Element>()
    fun onTouchContainer(x: Float, y: Float)
    {
        val topMargin = y.toInt() - (y.toInt() % CELL_SIZE)
        val leftMargin = x.toInt() - (x.toInt() % CELL_SIZE)
        val coordinate = Coordinate(topMargin, leftMargin)
        if (currentMaterial == Material.EMPTY) {
            eraseView(coordinate)
        }
        else
        {
            drawOrReplaceView(coordinate)
        }
    }
    private fun drawOrReplaceView(coordinate: Coordinate)
    {
     val viewOnCoordinate = getElementByCoordinates(coordinate, elementsOnContainer)
        if (viewOnCoordinate == null){
            drawView(coordinate)
            return
        }
        if (viewOnCoordinate.material != currentMaterial){
            replaceView(coordinate)
        }
    }
fun drawElementsList(elements: List<Element>?)
{
    if (elements == null)
    {
        return
    }
    for (element in elements)
    {
        currentMaterial = element.material
        drawView((element.coordinate))
    }
}
    private fun replaceView(coordinate: Coordinate)
    {
        eraseView(coordinate)
        drawView(coordinate)
    }
    private fun eraseView(coordinate: Coordinate)
    {
        val elementOnCoordinate = getElementByCoordinates(coordinate, elementsOnContainer)
        if (elementOnCoordinate != null) {
            val erasingView = container.findViewById<View>(elementOnCoordinate.viewId)
            container.removeView(erasingView)
            elementsOnContainer.remove(elementOnCoordinate)
        }
    }

    private fun removeExistingEagle(){
        elementsOnContainer.firstOrNull{it.material == Material.EAGLE}?.coordinate?.let{
            eraseView(it)
        }
    }
    private fun drawView(coordinate: Coordinate){
        removeExistingEagle()
        val view = ImageView(container.context)
        val layoutParams = FrameLayout.LayoutParams(
            currentMaterial.width * CELL_SIZE,
            currentMaterial.height * CELL_SIZE
        )
        view.setImageResource(currentMaterial.image)
        layoutParams.topMargin = coordinate.top
        layoutParams.leftMargin = coordinate.left
        val element = Element(
                material = currentMaterial,
                coordinate = coordinate,
                width = currentMaterial.width,
                height = currentMaterial.height
            )
                view.id = element.viewId
                view.layoutParams = layoutParams
                container.addView(view)
                elementsOnContainer.add(element)
    }
}