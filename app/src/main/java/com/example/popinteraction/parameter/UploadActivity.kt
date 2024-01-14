package com.example.popinteraction.parameter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.popinteraction.R
import com.example.popinteraction.XMLReadFile
import com.example.popinteraction.XMLWriter
import com.example.popinteraction.DataObject
import ImagesUpload

class UploadActivity : AppCompatActivity() {
    private lateinit var uploadsList: RecyclerView
    private lateinit var dataObjects: MutableList<DataObject>
    private lateinit var namesList: MutableList<String>
    private lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadsList = findViewById(R.id.UploadsList)
        uploadsList.layoutManager = LinearLayoutManager(this)

        dataObjects = mutableListOf()
        XMLReadFile.readXmlDataObjectsLocal(this, dataObjects)

        namesList = mutableListOf()
        for (dataObject in dataObjects) {
            val name = dataObject.name
            namesList.add(name)
        }

        adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_upload, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val element = namesList[position]
                val nameTextView = holder.itemView.findViewById<TextView>(R.id.name)
                val delete = holder.itemView.findViewById<ImageView>(R.id.remove)

                nameTextView.setOnClickListener {
                    showData(holder.itemView.context, dataObjects[position])
                }

                delete.setOnClickListener {
                    deleteElement(dataObjects[position].name, dataObjects[position].image)
                }
                nameTextView.text = element
            }

            override fun getItemCount(): Int {
                return namesList.size
            }
        }

        uploadsList.adapter = adapter
    }

    fun deleteElement(elementName: String, elementImage:String){
        val xmlWriter = XMLWriter(this)
        xmlWriter.deleteUpload(elementName)
        val imagesUpload = ImagesUpload(this, contentResolver)
        imagesUpload.deleteImage(elementImage)

        // Supprimer l'élément de la liste
        val index = dataObjects.indexOfFirst { it.name == elementName }
        if (index != -1) {
            dataObjects.removeAt(index)
            namesList.removeAt(index)
            adapter.notifyDataSetChanged()
        }
    }

    fun showData(context: Context, dataObject: DataObject) {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.CustomDialog)
        alertDialogBuilder.setTitle(dataObject.name)

        val message = String.format(
            "Emoji 1: %s\nEmoji 2: %s\nEmoji 3: %s\nCategory: %s\nClue: %s\nAnswers: %s",
            dataObject.emoji1,
            dataObject.emoji2,
            dataObject.emoji3,
            dataObject.categorie,
            dataObject.clue,
            dataObject.listAnswerString.joinToString(", ")
        )

        alertDialogBuilder.setMessage(message)

        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val imageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val imagePath = dataObject.image
        imageView.setImageURI(Uri.parse(imagePath))

        val textView = TextView(context)
        textView.text = message
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayout.addView(imageView)

        alertDialogBuilder.setView(linearLayout)

        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun navigateToParameter(view: View) {
        val intent = Intent(this, ParameterActivity::class.java)
        startActivity(intent)
    }

    fun popupUpload(view: View) {
        val intent = Intent(this, UploadPopup::class.java)
        startActivity(intent)
    }
}
