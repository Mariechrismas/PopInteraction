package com.example.popinteraction.emojistory

class EmojiStoryCurrent(
    name: String,
    categorie: String,
    image1: String,
    image2: String,
    image3: String,
    indice: String,
    responseImage: String,
    listAnswerString: List<String>,
    var answerIsGood: Boolean = false,
    var numberOfEmojiDisplay: Int = 1
) : EmojiStoryObject(name, categorie, image1, image2, image3, indice , responseImage, listAnswerString){
    constructor(emojiStoryObject: EmojiStoryObject) : this(
        emojiStoryObject.name,
        emojiStoryObject.categorie,
        emojiStoryObject.image1,
        emojiStoryObject.image2,
        emojiStoryObject.image3,
        emojiStoryObject.indice,
        emojiStoryObject.responseImage,
        emojiStoryObject.listAnswerString
    )
    constructor() : this("", "", "", "", "", "", "", emptyList())

}