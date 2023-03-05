package com.example.mycanvas.model

import com.google.android.gms.maps.model.LatLng

data class PointsWithDistance(
    val coordinatesId: Int,
    val distance: Float = 0f,
    val coordinates: List<Any>,
    val sLatLng: LatLng,
    val tLatLng: LatLng,
)
