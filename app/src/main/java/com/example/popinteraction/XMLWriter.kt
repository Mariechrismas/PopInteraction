package com.example.popinteraction

import android.content.Context
import android.util.Log
import org.w3c.dom.Document
import java.io.File
import java.io.FileOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class XMLWriter(context: Context) {

    private val fileName = "data.xml"
    private val file: File
    private val document: Document

    init {
        file = File(context.filesDir, fileName)

        document = if (file.exists()) {
            loadXmlDocument(file)
        } else {
            createNewXmlDocument()
        }

    }

    private val rootElement = document.documentElement ?: createRootElement()

    fun addUpload(
        name: String, category: String, emoji1: String, emoji2: String,
        emoji3: String, clue: String, image: String, listAnswerString: String
    ) {
        addObjectElement(name, category, emoji1, emoji2, emoji3, clue, image, listAnswerString)
        saveChanges()
    }

    private fun addObjectElement(
        name: String, category: String, emoji1: String, emoji2: String,
        emoji3: String, clue: String, image: String, listAnswerString: String
    ) {
        val objectElement = document.createElement("object")

        objectElement.setAttribute("name", name)
        objectElement.setAttribute("category", category)
        objectElement.setAttribute("emoji1", emoji1)
        objectElement.setAttribute("emoji2", emoji2)
        objectElement.setAttribute("emoji3", emoji3)
        objectElement.setAttribute("clue", clue)
        objectElement.setAttribute("image", image)
        objectElement.setAttribute("listAnswerString", listAnswerString)

        rootElement.appendChild(objectElement)
    }

    private fun saveChanges() {
        saveXmlDocument(document, file)
    }

    private fun loadXmlDocument(file: File): Document =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

    private fun createNewXmlDocument(): Document {
        val newDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
        val rootElement = newDocument.createElement("resources")
        newDocument.appendChild(rootElement)
        saveXmlDocument(newDocument, file)
        return newDocument
    }

    private fun saveXmlDocument(document: Document, file: File) {
        try {
            val transformer = TransformerFactory.newInstance().newTransformer()
            val source = DOMSource(document)
            val result = StreamResult(FileOutputStream(file))
            transformer.transform(source, result)
        } catch (e: Exception) {
            Log.e("XMLWriter", "Error saving XML document", e)
        }
    }

    private fun createRootElement(): org.w3c.dom.Element {
        val rootElement = document.createElement("resources")
        document.appendChild(rootElement)
        return rootElement
    }

    fun deleteUpload(name: String) {
        deleteObjectElement(name)
        saveChanges()
    }

    private fun deleteObjectElement(name: String) {
        val nodeList = rootElement.getElementsByTagName("object")
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node is org.w3c.dom.Element && node.getAttribute("name") == name) {
                rootElement.removeChild(node)
                break
            }
        }
    }

}