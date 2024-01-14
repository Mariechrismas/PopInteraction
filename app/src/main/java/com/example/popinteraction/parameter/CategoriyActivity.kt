package com.example.popinteraction.parameter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.popinteraction.R
import com.example.popinteraction.XMLReadFile

class CategoriyActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToggleButtonAdapter

    lateinit var initialListCategory: List<String>
    lateinit var selectedCategories: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        initialListCategory = listOf("Film", "Series", "Game", "Animal", "Actor", "Anime", "Cartoon", "Singer")

        selectedCategories = getSavedSelectedCategories()

        recyclerView = findViewById(R.id.listCategory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val categoryList = createCategoryList()
        adapter = ToggleButtonAdapter(categoryList)
        recyclerView.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        saveCategoryStates()
    }

    fun navigateToParameter(view: View) {
        XMLReadFile.initSelectedCategories(selectedCategories)
        val intent = Intent(this, ParameterActivity::class.java)
        intent.putStringArrayListExtra("selectedCategories", ArrayList(selectedCategories))
        startActivity(intent)
    }

    private fun createCategoryList(): List<CategoryObject> {
        val categoryNames = listOf("Film", "Series", "Game", "Animal", "Actor", "Anime", "Cartoon", "Singer")
        return categoryNames.mapIndexed { index, name ->
            val categoryId = "category_$index"
            val isChecked = selectedCategories.contains(name) // Mettez à jour ici
            CategoryObject(categoryId, name, isChecked)
        }
    }

    private fun saveCategoryStates() {
        for (category in adapter.categoryList) {
            saveCategoryState(category.id, category.isChecked)
            if (category.isChecked == true) {
                selectedCategories.add(category.name)
            } else {
                selectedCategories.remove(category.name)
            }
        }
        // Sauvegarder les catégories dans les préférences partagées
        saveSelectedCategories(selectedCategories)
    }

    private fun saveCategoryState(categoryId: String, isChecked: Boolean) {
        val sharedPrefs = getSharedPreferences("CategoryPrefs", MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(categoryId, isChecked).apply()
    }

    private fun getSavedCategoryState(categoryId: String, defaultValue: Boolean): Boolean {
        val sharedPrefs = getSharedPreferences("CategoryPrefs", MODE_PRIVATE)
        return sharedPrefs.getBoolean(categoryId, defaultValue)
    }

    private fun saveSelectedCategories(categories: List<String>) {
        val sharedPrefs = getSharedPreferences("CategoryPrefs", MODE_PRIVATE)
        sharedPrefs.edit().putStringSet("selectedCategories", categories.toSet()).apply()
    }

    private fun getSavedSelectedCategories(): MutableList<String> {
        val sharedPrefs = getSharedPreferences("CategoryPrefs", MODE_PRIVATE)
        // Obtenez les catégories depuis les préférences partagées et retournez-les sous forme de MutableList
        return sharedPrefs.getStringSet("selectedCategories", mutableSetOf())?.toMutableList() ?: mutableListOf()
    }
}
