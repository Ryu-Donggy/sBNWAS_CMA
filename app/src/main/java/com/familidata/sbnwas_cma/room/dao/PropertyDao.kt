package com.familidata.sbnwas_cma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.familidata.sbnwas_cma.room.entity.Property


@Dao
interface PropertyDao {

    @Query("SELECT property_value FROM property where property_key =  :property_key")
    fun getProperty(property_key: String?): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setProperty(property: Property)

//    @get:Query("SELECT * FROM property")
//    val all: List<Property>

//    @Query("SELECT COUNT(*) from property")
//    fun count(): Int

//    @Insert
//    fun insertAll(vararg properties: Property)

    @Query("DELETE FROM property")
    fun delete()
}
