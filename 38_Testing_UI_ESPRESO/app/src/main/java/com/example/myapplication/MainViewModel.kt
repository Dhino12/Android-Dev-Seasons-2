package com.example.myapplication

import com.example.myapplication.CuboidModel

class MainViewModel(private val cuboidModel: CuboidModel) {
    fun getCirumference():Double = cuboidModel.getCirumference()
    fun getSurfaceArea():Double = cuboidModel.getSurfaceArea()
    fun getVolume():Double = cuboidModel.getVolume()

    fun save(l:Double,w:Double,h:Double){
        cuboidModel.save(l,w,h)
    }
}