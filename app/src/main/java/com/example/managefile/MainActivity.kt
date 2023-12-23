package com.example.managefile

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ItemAdapter
    private var filesData: ArrayList<ItemModel> = ArrayList()
    private lateinit var currentLocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        toolbar.title = "Manage SD File"
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        val sdCardPath = Environment.getExternalStorageDirectory().path
        currentLocation = sdCardPath

        val sdCardDir = File(sdCardPath)
        if (sdCardDir.exists() && sdCardDir.isDirectory) {
            val files = sdCardDir.listFiles()

            if (files != null) {
                for (file in files) {
                    if (file.isDirectory) {
                        filesData.add(ItemModel(file))
                    } else {
                        filesData.add(ItemModel(file, false))
                    }
                }
            }

            adapter = ItemAdapter(filesData)

            listView = findViewById(R.id.listView)
            listView.adapter = adapter

            listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val fileSelected = filesData[position]

                if (fileSelected.isFolder) {
                    val filesChild = fileSelected.file.listFiles()
                    if (filesChild != null) {
                        filesData.clear()
                        for (file in filesChild) {
                           if (file.isDirectory) {
                               filesData.add(ItemModel(file))
                            } else {
                                filesData.add(ItemModel(file, false))
                           }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            registerForContextMenu(listView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialogBuilder = AlertDialog.Builder(this)
        val editText = EditText(this)
        dialogBuilder.setView(editText)
        dialogBuilder.setPositiveButton("Cancel") { dialogInterface, _ ->
        }


        return when (item.itemId) {
            R.id.new_file -> {
                dialogBuilder.setTitle("File name")
                dialogBuilder.setNegativeButton("Create") { dialogInterface, _ ->
                    val userInput = editText.text.toString()

                    val location = "$currentLocation/$userInput.txt"
                    val file = File(location)
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                }
                val dialog = dialogBuilder.create()
                dialog.show()
                return true
            }

            R.id.new_folder -> {
                dialogBuilder.setTitle("Folder name")
                dialogBuilder.setNegativeButton("Create") { dialogInterface, _ ->
                    val userInput = editText.text.toString()

                    val location = "$currentLocation/$userInput"
                    val folder = File(location)
                    if (!folder.exists()) {
                        folder.mkdirs()
                    }

                }
                val dialog = dialogBuilder.create()
                dialog.show()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.item_menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        if (!filesData[position].isFolder) {
            menu?.add(Menu.NONE, R.id.delete, Menu.NONE, "Duplicate")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val dialog = AlertDialog.Builder(this);
        val textView = TextView(this);
        textView.text = "Warning";
        textView.setTextColor(Color.RED)
        textView.setPadding(72, 32, 4 ,4);
        textView.textSize = 16F;
        dialog.setCustomTitle(textView);
        dialog.setNegativeButton("Yes") {
                dialogInterface, which -> {}
        }
        dialog.setPositiveButton("No") {
                dialogInterface, which -> {}
        }

        return when (item.itemId) {
            R.id.rename -> {
                dialog.setMessage("Do you want to rename this File/Folder");
                dialog.show();
                return true
            }

            R.id.delete -> {
                dialog.setMessage("Do you want to delete this File/Folder");
                dialog.show();
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }
}