package com.example.popinteraction.emojistory

import android.content.Context
import android.content.res.XmlResourceParser
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class XMLReadFile {

    //Methode pour récupérer la liste des object emoji story dans un fochier xml
    fun readXmlEmojiObjects(context: Context): MutableList<EmojiStoryObject> {
        val emojiObjects = mutableListOf<EmojiStoryObject>()

        try {
            val xmlResourceId = context.resources.getIdentifier("data_emoji_story", "xml", context.packageName)
            val xmlParser: XmlResourceParser = context.resources.getXml(xmlResourceId)

            var eventType = xmlParser.eventType
            var currentEmojiObject: EmojiStoryObject? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xmlParser.name) {
                            "object" -> {
                                currentEmojiObject = EmojiStoryObject(
                                    name = xmlParser.getAttributeValue(null, "name"),
                                    categorie = xmlParser.getAttributeValue(null,"categorie"),
                                    image1 = xmlParser.getAttributeValue(null, "emoji1"),
                                    image2 = xmlParser.getAttributeValue(null, "emoji2"),
                                    image3 = xmlParser.getAttributeValue(null, "emoji3"),
                                    indice = xmlParser.getAttributeValue(null,"indice"),
                                    responseImage = xmlParser.getAttributeValue(null, "responseImage"),
                                    listAnswerString = creatList(xmlParser.getAttributeValue(null,"listAnswerString"))
                                )
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (xmlParser.name == "object" && currentEmojiObject != null) {
                            emojiObjects.add(currentEmojiObject)
                        }
                    }
                }

                eventType = xmlParser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return emojiObjects
    }

    private fun creatList(stringList: String): List<String>{
        val listOfAnswer = stringList.split(",").toMutableList()
        return listOfAnswer
    }
}