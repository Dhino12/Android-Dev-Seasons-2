package com.example.myapplication

import kotlin.random.Random

internal object NumberGenerator{
    /*
    * Karena sifatnya yang static, kita tak perlu menginisiasi
    * NumberGenerator terlebih dahul untuk menggunakannya.
    * Kelas NumberGenerator merupakan kelas helper yang memudahkan kita untuk membuat angka secara acak.
    *  Inputan berupa bilangan bertipe integer digunakan sebagai nilai maksimum yang
    * bisa dicapai oleh fungsi generator kita. Generator angka dimulai dari 0 hingga nilai maksimum.
    * */
    fun generate(max:Int):Int{
        val random = Random
        return random.nextInt(max)
    }
}