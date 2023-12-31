package com.example.popinteraction.emojistory

data class EmojiStoryParty (
    var currentParty: EmojiStoryObject,
    val levelList: MutableList<EmojiStoryObject>,
    var score: Int,
    val answerIsGood: Boolean = false){
}