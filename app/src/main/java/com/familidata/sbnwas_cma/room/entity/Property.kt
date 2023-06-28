package com.familidata.sbnwas_cma.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "property")
data class Property(

    @PrimaryKey @ColumnInfo(name = "property_key") val property_key: String,
    @ColumnInfo(name = "property_value") val property_value: String?

)
