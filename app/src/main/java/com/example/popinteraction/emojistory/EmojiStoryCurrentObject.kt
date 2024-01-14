package com.example.popinteraction.emojistory

open class EmojiStoryCurrentObject (
    name: String,
    category: String,
    image1: String,
    image2: String,
    image3: String,
    clue: String,
    responseImage: String,
    listAnswerString: List<String>,
    var answerIsGood: Boolean = false,
    var numberOfEmojiDisplay: Int = 1
) : EmojiStoryObject(name, category, image1, image2, image3, clue , responseImage, listAnswerString){
    constructor(emojiStoryObject: EmojiStoryObject) : this(
        emojiStoryObject.name,
        emojiStoryObject.category,
        emojiStoryObject.emoji1,
        emojiStoryObject.emoji2,
        emojiStoryObject.emoji3,
        emojiStoryObject.clue,
        emojiStoryObject.image,
        emojiStoryObject.listAnswerString
    )
    constructor() : this("", "", "", "", "", "", "", emptyList())

}