package com.example.interaktywnefiszki;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcard_sets")
public class FlashcardSet {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public FlashcardSet(String name) {
        this.name = name;
    }
}