package com.example.myapplication.models

class message {
    var message:String?=null
    var senderId :String?=null
    var lst :String?=null
    var time :String?=null
    var name :String?=null
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

    constructor(message: String?,senderId: String?, time:String?, name :String?){
        this.message = message
        this.senderId = senderId
        this.time = time
        this.name=name
    }
}