package com.example.interaktywnefiszki;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcards",
        foreignKeys = @ForeignKey(entity = FlashcardSet.class,
                parentColumns = "id",
                childColumns = "setId",
                onDelete = ForeignKey.CASCADE))
public class Flashcard {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int setId;

    public String front;
    public String back;

    public Flashcard(int setId, String front, String back) {
        this.setId = setId;
        this.front = front;
        this.back = back;
    }

    public Flashcard() {}
}