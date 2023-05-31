package com.example.dbrueckerstattung.database

import androidx.room.*
import com.example.dbrueckerstattung.entity.Customer

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer_table")
    fun getAll(): List<Customer>

    /* @Query("SELECT * FROM kunde_table WHERE uid IN (:userIds)")
     fun loadAllByIds(userIds: IntArray): List<Kunde>*/

    @Query("SELECT * FROM customer_table WHERE roll_no LIKE :roll LIMIT 1")
    suspend fun findByRoll(roll: Int): Customer

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("DELETE FROM customer_table")
    suspend fun deleteAll()
}