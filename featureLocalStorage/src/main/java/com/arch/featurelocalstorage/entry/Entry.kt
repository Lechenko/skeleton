package com.arch.featurelocalstorage.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arch.featurelocalstorage.BuildConfig


@Entity(tableName = "entry")
class Entry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

) {

    override fun toString(): String {
        return if (BuildConfig.DEBUG)  "DataNews(name = $id"  else ""
    }
}