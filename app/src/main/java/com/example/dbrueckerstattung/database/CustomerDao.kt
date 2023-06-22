package com.example.dbrueckerstattung.database

import android.content.Context
import androidx.room.*
import com.example.dbrueckerstattung.entity.Customer

// Room für Compile-time verification der SQL queries
// Dao Data Access Object um Entities über DB abzubilden
@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer_table")
    fun getAll(): List<Customer>

    /* @Query("SELECT * FROM kunde_table WHERE uid IN (:userIds)")
     fun loadAllByIds(userIds: IntArray): List<Kunde>*/

    // Room uebernimmt die Querys - dynamische Abfrage
    @Query("SELECT * FROM customer_table WHERE id LIKE :roll LIMIT 1")
    suspend fun findByRoll(roll: Int): Customer

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("DELETE FROM customer_table")
    suspend fun deleteAll()

}