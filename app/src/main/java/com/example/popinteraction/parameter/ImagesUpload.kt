import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class ImagesUpload(private val context: Context, private val contentResolver: ContentResolver) {

    fun saveImageToInternalStorage(imageUri: Uri, fileName: String): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val internalFilesDir = File(context.filesDir, "images")
        internalFilesDir.mkdirs()
        val filePath = File(internalFilesDir, fileName)

        inputStream?.use { input ->
            val outputStream = FileOutputStream(filePath)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
            }
        }

        return filePath.absolutePath
    }

    fun deleteImage(filePath: String) {
        val fileToDelete = File(filePath)
        if (fileToDelete.exists()){
            fileToDelete.delete()
        }
    }
}
