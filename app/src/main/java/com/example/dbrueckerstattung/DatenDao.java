package com.example.dbrueckerstattung;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DatenDao {

    @Query("SELECT * FROM DATEN ORDER BY ID")
    List<Daten> loadAllPersons();

    @Insert
    void insertDaten(Daten daten);

    @Update
    void updatePerson(Daten daten);

    @Delete
    void delete(Daten daten);

    @Query("SELECT * FROM DATEN WHERE id = :id")
    Daten loadPersonById(int id);
}