package com.example.server

object Utils {
    fun TagsParser(tags: String?): MutableSet<Int>{
        if (tags != null) {
            val res = mutableSetOf<Int>()
            val lst = tags.split(",").map{ if(it.isNotEmpty()){it.toInt()}else{-1} }
            for(i in lst){
                if(i!=-1){
                    res.add(i)
                }
            }
            return res
        }else{
            return mutableSetOf()
        }
    }

    fun TagsParser(tags: MutableSet<Int>): String{
        var res = ""
        for(i in tags){
            res+="${i},"
        }
        return res
    }
}