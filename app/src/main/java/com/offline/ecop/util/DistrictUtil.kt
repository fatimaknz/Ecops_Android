package com.offline.ecop.util

class DistrictUtil {
    val districts: LinkedHashMap<Int, String> = LinkedHashMap()

    val area: LinkedHashMap<Int, List<String>> = LinkedHashMap()

    init {

        districts[0] = "Select"
        districts[1] = "Mumbai"
        districts[2] = "Navi Mumbai"
        districts[3] = "Jalgaon"
        districts[4] = "Nagpur"
        districts[5] = "Nashik"

        area[0] = arrayListOf("Select")
        area[1] = arrayListOf("Kurla", "Ghatkopar", "Andheri", "Bandra", "Borilvali")
        area[2] = arrayListOf("Vashi", "Nerul", "Panvel", "Uran", "Karjat")
        area[3] = arrayListOf("Jamner", "Dharangaon", "Bhusawal", "Raver", "Bhadgaon")
        area[4] = arrayListOf("Kamptee", "Hingana", "Narkhed", "Umred", "Bhiwapur")
        area[5] = arrayListOf("Igatpuri", "Dindori", "Malegaon", "Chandwad", "Niphad")
    }

    fun getDistrictList(): List<String> {
        val districtList = mutableListOf<String>()
        for ((k, v) in districts) {
            districtList.add(v)
        }

        return districtList
    }

    fun getPoliceStationList(districeName: String): List<String> {
        val policeList = mutableListOf<String>()
        var index = -1
        for ((k, v) in districts) {

            if (v.equals(districeName, ignoreCase = true)) {
                index = k
                break
            }
        }
        if (index != -1) {
            return area[index]!!
        }

        return policeList
    }
}