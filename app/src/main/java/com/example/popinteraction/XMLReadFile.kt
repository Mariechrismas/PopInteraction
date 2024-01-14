package com.example.popinteraction

import android.content.Context
import android.content.res.XmlResourceParser
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

    fun readXmlDataObjects(context: Context): MutableList<DataObject> {
        val dataObjects = mutableListOf<DataObject>()
        readXmlDataObjectsApp(context, dataObjects)
        readXmlDataObjectsLocal(context, dataObjects)
        return dataObjects
    }

    private fun createList(stringList: String): List<String> {
        return stringList.split(",").toMutableList()
    }

    fun readXmlDataObjectsApp(context: Context, dataObjects:MutableList<DataObject>): MutableList<DataObject> {
        try {
            val xmlResourceId = context.resources.getIdentifier("data", "xml", context.packageName)
            val xmlParser: XmlResourceParser = context.resources.getXml(xmlResourceId)

            var eventType = xmlParser.eventType
            var currentDataObject: DataObject? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xmlParser.name) {
                            "object" -> {
                                currentDataObject = DataObject(
                                    name = xmlParser.getAttributeValue(null, "name"),
                                    categorie = xmlParser.getAttributeValue(null, "categorie"),
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
                            currentDataObject.categorie in selectedCategories
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
    fun readXmlDataObjectsLocal(context: Context, dataObjects:MutableList<DataObject>): MutableList<DataObject> {

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
                                        categorie = xmlParser.getAttributeValue(null, "categorie"),
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
                                currentDataObject.categorie in selectedCategories
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
}
