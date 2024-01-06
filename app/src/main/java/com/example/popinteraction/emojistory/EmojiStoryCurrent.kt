package com.example.popinteraction.emojistory

class EmojiStoryCurrent(
    name: String,
    categorie: String,
    image1: String,
    image2: String,
    image3: String,
    responseImage: String,
    listAnswerString: List<String>,
    var answerIsGood: Boolean = false,
    var numberOfEmojiDisplay: Int = 0
) : EmojiStoryObject(name, categorie, image1, image2, image3, responseImage, listAnswerString){
    constructor(emojiStoryObject: EmojiStoryObject) : this(
        emojiStoryObject.name,
        emojiStoryObject.categorie,
        emojiStoryObject.image1,
        emojiStoryObject.image2,
        emojiStoryObject.image3,
        emojiStoryObject.responseImage,
        emojiStoryObject.listAnswerString
    )
    constructor() : this("", "", "", "", "", "", emptyList())

}