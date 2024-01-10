package com.example.popinteraction.emojistory

open class EmojiStoryPartyObject (
    var currentParty: EmojiStoryCurrentObject,
    val levelList: MutableList<EmojiStoryObject>,
    var score: Int
    ){
    constructor() : this(EmojiStoryCurrentObject(), mutableListOf(), 0)
}
