package com.example.dbrueckerstattung.entity;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dbrueckerstattung.entity.Daten;

import java.util.List;

//DataAccessObject für die Daten aus der DB für die Rückerstattungen
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

    @Query("DELETE FROM DATEN")
    void clearData();
}