package com.example.interaktywnefiszki;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SolveFlashcardsActivity extends AppCompatActivity {

    private TextView tvTitle, tvProgress, tvFrontText, tvBackText;
    private Button btnFlip, btnKnow, btnDontKnow;
    private CardView cardFront, cardBack;
    private LinearLayout layoutAnswerButtons;

    private FlashcardRepository repository;

    private List<Flashcard> currentFlashcards;
    private int currentIndex = 0;
    private boolean isFrontVisible = true;

    private AnimatorSet frontAnim, backAnim;

    private int correctAnswers = 0;
    private int incorrectAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_flashcards);

        initializeViews();
        setupAnimations();

        repository = new FlashcardRepository(getApplication());

        int setId = getIntent().getIntExtra("setId", -1);
        String setName = getIntent().getStringExtra("setName");

        if (setId != -1) {
            if (setName != null) {
                tvTitle.setText("Nauka: " + setName);
            }
            loadFlashcards(setId);
        } else {
            Toast.makeText(this, "Błąd: Nie można załadować zestawu (brak ID).", Toast.LENGTH_LONG).show();
            finish();
        }

        btnFlip.setOnClickListener(v -> flipCard());
        btnKnow.setOnClickListener(v -> handleAnswer(true));
        btnDontKnow.setOnClickListener(v -> handleAnswer(false));
    }

    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvProgress = findViewById(R.id.tvProgress);
        tvFrontText = findViewById(R.id.tvFlashcardFrontText);
        tvBackText = findViewById(R.id.tvFlashcardBackText);
        btnFlip = findViewById(R.id.btnFlip);
        btnKnow = findViewById(R.id.btnKnow);
        btnDontKnow = findViewById(R.id.btnDontKnow);
        cardFront = findViewById(R.id.card_front);
        cardBack = findViewById(R.id.card_back);
        layoutAnswerButtons = findViewById(R.id.layoutAnswerButtons);
    }

    private void setupAnimations() {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        cardFront.setCameraDistance(8000 * scale);
        cardBack.setCameraDistance(8000 * scale);
        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.front_animator);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.back_animator);
    }

    private void loadFlashcards(int setId) {
        repository.getFlashcardsForSet(setId, result -> {
            runOnUiThread(() -> {
                currentFlashcards = result;

                if (currentFlashcards == null || currentFlashcards.isEmpty()) {
                    showEndSessionDialog();
                    return;
                }

                correctAnswers = 0;
                incorrectAnswers = 0;

                Collections.shuffle(currentFlashcards);
                currentIndex = 0;
                displayCurrentCard();
            });
        });
    }

    private void displayCurrentCard() {
        if (currentFlashcards == null || currentIndex >= currentFlashcards.size()) {
            showEndSessionDialog();
            return;
        }

        updateButtonVisibility(false);

        Flashcard currentCard = currentFlashcards.get(currentIndex);
        tvFrontText.setText(currentCard.front);
        tvBackText.setText(currentCard.back);
        tvProgress.setText(String.format(Locale.getDefault(), "Postęp: %d / %d", currentIndex + 1, currentFlashcards.size()));
    }

    private void flipCard() {
        if (isFrontVisible) {
            frontAnim.setTarget(cardFront);
            backAnim.setTarget(cardBack);

            backAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    updateButtonVisibility(true);
                    backAnim.removeListener(this);
                }
            });

            frontAnim.start();
            backAnim.start();
            isFrontVisible = false;
        } else {
            frontAnim.setTarget(cardBack);
            backAnim.setTarget(cardFront);

            backAnim.start();
            frontAnim.start();
            isFrontVisible = true;
        }
    }

    private void handleAnswer(boolean known) {
        if (known) {
            correctAnswers++;
        } else {
            incorrectAnswers++;
        }
        moveToNextCard();
    }

    private void moveToNextCard() {
        currentIndex++;

        backAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                displayCurrentCard();
                backAnim.removeListener(this);
            }
        });

        flipCard();
    }

    private void updateButtonVisibility(boolean showAnswerButtons) {
        if (showAnswerButtons) {
            btnFlip.setVisibility(View.GONE);
            layoutAnswerButtons.setVisibility(View.VISIBLE);
        } else {
            btnFlip.setVisibility(View.VISIBLE);
            layoutAnswerButtons.setVisibility(View.GONE);
        }
    }

    private void showEndSessionDialog() {
        btnFlip.setEnabled(false);
        layoutAnswerButtons.setVisibility(View.GONE);

        String message;
        if (currentFlashcards == null || currentFlashcards.isEmpty()) {
            message = "Ten zestaw jest pusty. Dodaj do niego fiszki, aby rozpocząć naukę.";
        } else {
            int total = correctAnswers + incorrectAnswers;
            double percentage = (total > 0) ? ((double) correctAnswers / total) * 100 : 0;

            message = String.format(Locale.getDefault(),
                    "Sesja zakończona!\n\nPoprawne odpowiedzi: %d\nBłędne odpowiedzi: %d\n\nSkuteczność: %.1f%%",
                    correctAnswers, incorrectAnswers, percentage);
        }

        new AlertDialog.Builder(this)
                .setTitle("Wyniki sesji")
                .setMessage(message)
                .setPositiveButton("Powrót", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}