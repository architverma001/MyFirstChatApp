package com.example.myapplication

class message {
    var message:String?=null
    var senderId :String?=null
    var lst :String?=null
    var time :String?=null

    constructor(){

    }
    constructor(message:String?,senderId:String?){
        this.message = message
        this.senderId = senderId

    }
    constructor(message: String?,senderId: String?, time:String?){
        this.message = message
        this.senderId = senderId
        this.time = time
    }
}