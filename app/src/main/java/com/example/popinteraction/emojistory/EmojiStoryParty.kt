package com.example.popinteraction.emojistory

data class EmojiStoryParty (
    var currentParty: String,
    val levelList: MutableList<EmojiStoryObject>,
    var score: Int,
){
    constructor() : this("", mutableListOf(), 0)
}