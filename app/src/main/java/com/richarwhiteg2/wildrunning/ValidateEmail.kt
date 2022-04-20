package com.richarwhiteg2.wildrunning
import java.util.regex.Matcher
import java.util.regex.Pattern

class ValidateEmail {

    class ValidateEmail {
        companion object{
            var pat: Pattern?= null  //creamos el patron
            var mat: Matcher?= null

            fun isEmail(email:String): Boolean{
                pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$")
                mat = pat!!.matcher(email)
                return mat!!.find()
            }
        }
    }
}