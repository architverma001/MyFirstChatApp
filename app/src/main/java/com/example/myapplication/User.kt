package com.example.myapplication

class User {
    var name:String ?= null
    var email:String ?= null
    var uid:String ?= null
    var profile:String?=null
    var lastMessage:String?= null
    constructor(){

    }

    constructor(name: String?, email: String?, uid: String?) {
        this.name = name
        this.email = email
        this.uid = uid
    }

    constructor(name: String?, email: String?, uid: String?, lastMessage:String?) {
        this.name = name
        this.email = email
        this.uid = uid
        this. lastMessage =  lastMessage
    }
    constructor(name: String?, email: String?, uid: String?, lastMessage:String?,profile:String?) {
        this.name = name
        this.email = email
        this.uid = uid
        this. lastMessage =  lastMessage
        this.profile = profile
    }


}