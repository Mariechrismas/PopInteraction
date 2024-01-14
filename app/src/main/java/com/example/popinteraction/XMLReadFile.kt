package com.example.popinteraction

import android.content.Context
import android.content.res.XmlResourceParser
import com.example.popinteraction.depixelimage.DepixelObject
import com.example.popinteraction.emojistory.EmojiStoryObject
import com.example.popinteraction.guessvoice.GuessVoiceObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object XMLReadFile {
    private var selectedCategories =
        listOf("Film", "Series", "Game", "Animal", "Actor", "Anime", "Cartoon", "Singer")

    fun initSelectedCategories(categories: List<String>) {
        selectedCategories = categories
    }


    private fun createList(stringList: String): List<String> {
        return stringList.split(",").toMutableList()
    }
    fun readXmlDepixelObject(context: Context): MutableList<DepixelObject> {
        val dataObjects = mutableListOf<DepixelObject>()
        readXmlDepixelObjectsApp(context, dataObjects)
        readXmlDepixelObjectsLocal(context, dataObjects)
        return dataObjects
    }
    fun readXmlDepixelObjectsApp(context: Context, dataObjects:MutableList<DepixelObject>): MutableList<DepixelObject> {
        try {
            val xmlResourceId = context.resources.getIdentifier("data", "xml", context.packageName)
            val xmlParser: XmlResourceParser = context.resources.getXml(xmlResourceId)

            var eventType = xmlParser.eventType
            var currentDataObject: DepixelObject? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xmlParser.name) {
                            "object" -> {
                                currentDataObject = DepixelObject(
                                    name = xmlParser.getAttributeValue(null, "name"),
                                    category = xmlParser.getAttributeValue(null, "category"),
                                    image = xmlParser.getAttributeValue(null, "image"),
                                    clue=xmlParser.getAttributeValue(null, "clue"),
                                    listAnswerString = createList(
                                        xmlParser.getAttributeValue(
                                            null,
                                            "listAnswerString"
                                        )
                                    )
                                )
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (xmlParser.name == "object" && currentDataObject != null &&
                            currentDataObject.category in selectedCategories
                        ) {
                            dataObjects.add(currentDataObject)
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

        return dataObjects
    }
    fun readXmlDepixelObjectsLocal(context: Context, dataObjects:MutableList<DepixelObject>): MutableList<DepixelObject> {

        try {
            val file = File(context.filesDir, "data.xml")

            if (file.exists()) {
                val fileInputStream = FileInputStream(file)
                val xmlParser: XmlPullParser = android.util.Xml.newPullParser()

                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                xmlParser.setInput(fileInputStream, null)

                var eventType = xmlParser.eventType
                var currentDataObject: DepixelObject? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (xmlParser.name) {
                                "object" -> {
                                    currentDataObject = DepixelObject(
                                        name = xmlParser.getAttributeValue(null, "name"),
                                        category = xmlParser.getAttributeValue(null, "category"),
                                        image = xmlParser.getAttributeValue(null, "image"),
                                        clue=xmlParser.getAttributeValue(null, "clue"),
                                        listAnswerString = createList(
                                            xmlParser.getAttributeValue(
                                                null,
                                                "listAnswerString"
                                            )
                                        )
                                    )
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> {
                            if (xmlParser.name == "object" && currentDataObject != null &&
                                currentDataObject.category in selectedCategories
                            ) {
                                dataObjects.add(currentDataObject)
                            }
                        }
                    }

                    eventType = xmlParser.next()
                }

                fileInputStream.close()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return dataObjects
    }

    fun readXmlEmojieObjects(context: Context): MutableList<EmojiStoryObject> {
        val dataObjects = mutableListOf<EmojiStoryObject>()
        readXmlEmojiObjectsApp(context, dataObjects)
        readXmlEmojiObjectsLocal(context, dataObjects)
        return dataObjects
    }
    fun readXmlEmojiObjectsApp(context: Context, emojiObjects:MutableList<EmojiStoryObject>): MutableList<EmojiStoryObject> {

        try {
            val xmlResourceId = context.resources.getIdentifier("data", "xml", context.packageName)
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
                                    category = xmlParser.getAttributeValue(null,"category"),
                                    emoji1 = xmlParser.getAttributeValue(null, "emoji1"),
                                    emoji2 = xmlParser.getAttributeValue(null, "emoji2"),
                                    emoji3 = xmlParser.getAttributeValue(null, "emoji3"),
                                    clue = xmlParser.getAttributeValue(null,"clue"),
                                    image = xmlParser.getAttributeValue(null, "image"),
                                    listAnswerString = createList(xmlParser.getAttributeValue(null,"listAnswerString"))
                                )
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (xmlParser.name == "object" && currentEmojiObject != null &&
                            currentEmojiObject.category in selectedCategories
                        ) {
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

    fun readXmlEmojiObjectsLocal(context: Context, emojiObjects:MutableList<EmojiStoryObject>): MutableList<EmojiStoryObject> {

        try {
            val file = File(context.filesDir, "data.xml")

            if (file.exists()) {
                val fileInputStream = FileInputStream(file)
                val xmlParser: XmlPullParser = android.util.Xml.newPullParser()

                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                xmlParser.setInput(fileInputStream, null)

                var eventType = xmlParser.eventType
                var currentDataObject: EmojiStoryObject? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (xmlParser.name) {
                                "object" -> {
                                    currentDataObject = EmojiStoryObject(
                                        name = xmlParser.getAttributeValue(null, "name"),
                                        category = xmlParser.getAttributeValue(null, "category"),
                                        emoji1 = xmlParser.getAttributeValue(null, "emoji1"),
                                        emoji2 = xmlParser.getAttributeValue(null, "emoji2"),
                                        emoji3 = xmlParser.getAttributeValue(null, "emoji3"),
                                        image = xmlParser.getAttributeValue(null, "image"),
                                        clue=xmlParser.getAttributeValue(null, "clue"),
                                        listAnswerString = createList(
                                            xmlParser.getAttributeValue(
                                                null,
                                                "listAnswerString"
                                            )
                                        )
                                    )
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> {
                            if (xmlParser.name == "object" && currentDataObject != null &&
                                currentDataObject.category in selectedCategories
                            ) {
                                emojiObjects.add(currentDataObject)
                            }
                        }
                    }

                    eventType = xmlParser.next()
                }

                fileInputStream.close()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emojiObjects
    }

    fun readXmlGuessVoiceObjects(context: Context): MutableList<GuessVoiceObject> {
        val dataObjects = mutableListOf<GuessVoiceObject>()
        readXmlGuessVoiceObjectsApp(context, dataObjects)
        readXmlGuessVoiceObjectsLocal(context, dataObjects)
        return dataObjects
    }
    fun readXmlGuessVoiceObjectsApp(context: Context, guessVoiceObject: MutableList<GuessVoiceObject>): MutableList<GuessVoiceObject> {

        try {
            val xmlResourceId = context.resources.getIdentifier("data", "xml", context.packageName)
            val xmlParser: XmlResourceParser = context.resources.getXml(xmlResourceId)

            var eventType = xmlParser.eventType
            var currentGuessVoiceObject: GuessVoiceObject? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xmlParser.name) {
                            "object" -> {
                                currentGuessVoiceObject = GuessVoiceObject(
                                    name = xmlParser.getAttributeValue(null, "name"),
                                    category = xmlParser.getAttributeValue(null,"category"),
                                    image = xmlParser.getAttributeValue(null, "image"),
                                    music = xmlParser.getAttributeValue(null,"music"),
                                    listAnswerString = createList(xmlParser.getAttributeValue(null,"listAnswerString"))
                                )
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (xmlParser.name == "object" && currentGuessVoiceObject != null &&
                            currentGuessVoiceObject.category in selectedCategories
                        ) {
                            guessVoiceObject.add(currentGuessVoiceObject)
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

        return guessVoiceObject
    }

    fun readXmlGuessVoiceObjectsLocal(context: Context, guessVoiceObjects:MutableList<GuessVoiceObject>): MutableList<GuessVoiceObject> {

        try {
            val file = File(context.filesDir, "data.xml")

            if (file.exists()) {
                val fileInputStream = FileInputStream(file)
                val xmlParser: XmlPullParser = android.util.Xml.newPullParser()

                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                xmlParser.setInput(fileInputStream, null)

                var eventType = xmlParser.eventType
                var currentDataObject: GuessVoiceObject? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (xmlParser.name) {
                                "object" -> {
                                    currentDataObject = GuessVoiceObject(
                                        name = xmlParser.getAttributeValue(null, "name"),
                                        category = xmlParser.getAttributeValue(null, "category"),
                                        image = xmlParser.getAttributeValue(null, "image"),
                                        music = xmlParser.getAttributeValue(null,"music"),
                                        listAnswerString = createList(xmlParser.getAttributeValue(null, "listAnswerString")
                                        )
                                    )
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> {
                            if (xmlParser.name == "object" && currentDataObject != null &&
                                currentDataObject.category in selectedCategories
                            ) {
                                guessVoiceObjects.add(currentDataObject)
                            }
                        }
                    }

                    eventType = xmlParser.next()
                }

                fileInputStream.close()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return guessVoiceObjects
    }



    fun readXmlDataObjectsLocal(context: Context, emojiObjects:MutableList<DataObject>): MutableList<DataObject> {

        try {
            val file = File(context.filesDir, "data.xml")

            if (file.exists()) {
                val fileInputStream = FileInputStream(file)
                val xmlParser: XmlPullParser = android.util.Xml.newPullParser()

                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                xmlParser.setInput(fileInputStream, null)

                var eventType = xmlParser.eventType
                var currentDataObject: DataObject? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (xmlParser.name) {
                                "object" -> {
                                    currentDataObject = DataObject(
                                        name = xmlParser.getAttributeValue(null, "name"),
                                        category = xmlParser.getAttributeValue(null, "category"),
                                        emoji1 = xmlParser.getAttributeValue(null, "emoji1"),
                                        emoji2 = xmlParser.getAttributeValue(null, "emoji2"),
                                        emoji3 = xmlParser.getAttributeValue(null, "emoji3"),
                                        image = xmlParser.getAttributeValue(null, "image"),
                                        clue= xmlParser.getAttributeValue(null, "clue"),
                                        music= xmlParser.getAttributeValue(null,"music"),
                                        listAnswerString = createList(xmlParser.getAttributeValue(null, "listAnswerString"))
                                    )
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> {
                            if (xmlParser.name == "object" && currentDataObject != null
                            ) {
                                emojiObjects.add(currentDataObject)
                            }
                        }
                    }

                    eventType = xmlParser.next()
                }

                fileInputStream.close()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emojiObjects
    }
}
