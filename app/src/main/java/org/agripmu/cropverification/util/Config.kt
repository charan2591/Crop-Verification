package org.agripmu.cropverification.util

 interface Config {

     companion object {
         const val PREF_HOME = "PREF_HOME"
         const val PREF_IS_LOGIN = "PREF_IS_LOGIN"
         const val PREF_LANGUAGE = "PREF_LANGUAGE"

         const val PREF_USER_ID= "PREF_USER_ID"
         const val PREF_USER_NAME= "PREF_USER_NAME"
         const val PREF_USER_MOBILE= "PREF_USER_MOBILE"
         const val PREF_USER_JURISDICTION = "PREF_USER_JURISDICTION"
         const val PREF_USER_TOKEN = "PREF_USER_TOKEN"
         const val FIRST_TIME = "isFirstRun"
     }

 }