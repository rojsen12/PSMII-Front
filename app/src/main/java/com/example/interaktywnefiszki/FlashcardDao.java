package com.example.interaktywnefiszki;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface FlashcardDao {
    @Insert
    long insertSet(FlashcardSet set);

    @Insert
    void insertFlashcard(Flashcard flashcard);

    @Query("SELECT * FROM flashcard_sets ORDER BY name ASC")
    List<FlashcardSet> getAllSets();

    @Query("SELECT * FROM flashcards WHERE setId = :setId")
    List<Flashcard> getFlashcardsForSet(int setId);

    @Query("SELECT * FROM flashcard_sets WHERE name = :name LIMIT 1")
    FlashcardSet getSetByName(String name);

    @Update
    void updateSet(FlashcardSet set);

    @Delete
    void deleteSet(FlashcardSet set);
}