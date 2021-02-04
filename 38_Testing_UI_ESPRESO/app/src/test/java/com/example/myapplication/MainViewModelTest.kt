package com.example.myapplication

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class MainViewModelTest {
    //DENGAN MENGGUNAKAN INI ,DAPAT MELAKUKAN PENGUJIAN YANG BERBEDA BEDA DI SETIAP FUNGSI
    private lateinit var mainViewModel:MainViewModel
    private lateinit var cuboidModel: CuboidModel

    private val dummyWidth = 7.0
    private val dummyHeight = 6.0
    private val dummyLength = 12.0
    //Hasil ====================
    private val dummyVolume = 504.0
    private val dummyCirumference = 100.0
    private val dummySurfaceArea = 396.0

    //Anotasi @Before untuk menginisialisasi method sblm mlakukan test(@Test)
    @Before
    fun before(){
        cuboidModel = Mockito.mock(CuboidModel::class.java)
        mainViewModel = MainViewModel(cuboidModel)
    }
    //Anotasi @Test dgunakan pada method yang akan ditest
    @Test
    fun testVolume(){
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyWidth,dummyLength,dummyHeight)
        val volume = mainViewModel.getVolume()
        Assert.assertEquals(dummyVolume, volume, 0.0001)
    }

    @Test
    fun testCircumference(){
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyWidth,dummyLength,dummyHeight)
        val keliling = mainViewModel.getCirumference()
        Assert.assertEquals(dummyCirumference, keliling, 0.0001)
    }

    @Test
    fun testSurfaceArea(){
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyWidth,dummyLength,dummyHeight)
        val luasPermukaan = mainViewModel.getSurfaceArea()
        Assert.assertEquals(dummySurfaceArea, luasPermukaan, 0.0001)
    }
    //=========================================
    // METODE PENGUJIAN Mock
    //=========================================
    @Test
    fun testMockVolume(){
        //`when`() = Menandakan event dimana kita ingin memanipulasi behavior dari mockObject
        //thenReturn() = memanipulasi output dari mockObject
        Mockito.`when`(mainViewModel.getVolume()).thenReturn(dummyVolume)
        val volume = mainViewModel.getVolume()
        //verify() = memeriksa method yang dipanggil dgn argument yang diberikan -> bagian dari Mockito
        Mockito.verify(cuboidModel).getVolume()
        //assertEquals() = untuk memvalidasi output yang diharpkan dgn output sbnarny
        Assert.assertEquals(dummyVolume, volume, 0.0001)
    }
    @Test
    fun testMockCircumference(){
        Mockito.`when`(mainViewModel.getCirumference()).thenReturn(dummyCirumference)
        val keliling = mainViewModel.getCirumference()
        Mockito.verify(cuboidModel).getCirumference()
        Assert.assertEquals(dummyCirumference, keliling, 0.0001)
    }
    @Test
    fun testMockSurfaceArea(){
        Mockito.`when`(mainViewModel.getSurfaceArea()).thenReturn(dummySurfaceArea)
        val luas_permukaan = mainViewModel.getSurfaceArea()
        Mockito.verify(cuboidModel).getSurfaceArea()
        Assert.assertEquals(dummySurfaceArea, luas_permukaan, 0.0001)
    }
}