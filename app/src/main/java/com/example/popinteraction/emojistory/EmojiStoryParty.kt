package com.example.popinteraction.emojistory

class EmojiStoryParty (
    var currentParty: EmojiStoryCurrent,
    val levelList: MutableList<EmojiStoryObject>,
    var score: Int
    ){
    constructor() : this(EmojiStoryCurrent(), mutableListOf(), 0)
}
