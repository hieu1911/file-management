package com.example.managefile

import java.io.File

data class ItemModel (val file: File, val isFolder: Boolean = true) {
}