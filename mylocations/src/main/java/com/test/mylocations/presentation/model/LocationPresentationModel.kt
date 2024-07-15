package com.test.mylocations.presentation.model

data class LocationPresentationModel(val latitude:Double,
                                     val longitude:Double,
                                     val timestamp: Long = System.currentTimeMillis()
)
