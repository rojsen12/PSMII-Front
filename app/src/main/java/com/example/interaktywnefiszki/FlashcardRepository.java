package com.example.interaktywnefiszki;

import android.app.Application;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlashcardRepository {

    private final FlashcardDao flashcardDao;
    private final ExecutorService executorService;

    public FlashcardRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        flashcardDao = db.flashcardDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getAllSets(final RepositoryCallback<List<FlashcardSet>> callback) {
        executorService.execute(() -> {
            List<FlashcardSet> result = flashcardDao.getAllSets();
            if (callback != null) {
                callback.onComplete(result);
            }
        });
    }

    public void getFlashcardsForSet(final int setId, final RepositoryCallback<List<Flashcard>> callback) {
        executorService.execute(() -> {
            List<Flashcard> result = flashcardDao.getFlashcardsForSet(setId);
            if (callback != null) {
                callback.onComplete(result);
            }
        });
    }

    public void addSet(String setName, final RepositoryCallback<Long> callback) {
        executorService.execute(() -> {
            FlashcardSet existingSet = flashcardDao.getSetByName(setName);
            if (existingSet == null) {
                long newId = flashcardDao.insertSet(new FlashcardSet(setName));
                if (callback != null) {
                    callback.onComplete(newId);
                }
            } else {
                if (callback != null) {
                    callback.onComplete(null);
                }
            }
        });
    }

    public void addFlashcardToSet(String setName, String front, String back) {
        executorService.execute(() -> {
            FlashcardSet set = flashcardDao.getSetByName(setName);
            if (set != null) {
                flashcardDao.insertFlashcard(new Flashcard(set.id, front, back));
            } else {
                long setId = flashcardDao.insertSet(new FlashcardSet(setName));
                flashcardDao.insertFlashcard(new Flashcard((int) setId, front, back));
            }
        });
    }

    public void updateSet(FlashcardSet set) {
        executorService.execute(() -> flashcardDao.updateSet(set));
    }

    public void deleteSet(FlashcardSet set) {
        executorService.execute(() -> flashcardDao.deleteSet(set));
    }

    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }
}