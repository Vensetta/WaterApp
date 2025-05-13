package org.turter.water_app_mobile.data.remote

object ApiEndpoint {
    private const val BASE_URL = "http://192.168.30.110:8081/api/v1/user-log"

    val userLogByPeriod = "$BASE_URL/by-period"

    val addUserWaterIntake = "$BASE_URL/add"

    val cleanUpUserLog = "$BASE_URL/clean-up"

}