package com.example.interaktywnefiszki;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlashcardsListActivity extends AppCompatActivity {

    private ListView lvFlashcards;
    private Button btnBackToMenu;
    private FloatingActionButton fabAddSet;

    private FlashcardRepository repository;

    private List<FlashcardSet> flashcardSetObjects;
    private List<String> flashcardSetNames;
    private ArrayAdapter<String> adapter;

    private ActivityResultLauncher<Intent> createSetLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_list);

        lvFlashcards = findViewById(R.id.lvFlashcards);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);
        fabAddSet = findViewById(R.id.fabAddSet);

        repository = new FlashcardRepository(getApplication());

        flashcardSetObjects = new ArrayList<>();
        flashcardSetNames = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flashcardSetNames);
        lvFlashcards.setAdapter(adapter);


        createSetLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    loadSetsFromDatabase();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(FlashcardsListActivity.this, "Dodano nowy zestaw!", Toast.LENGTH_SHORT).show();
                    }
                });

        lvFlashcards.setOnItemClickListener((parent, view, position, id) -> {
            FlashcardSet selectedSet = flashcardSetObjects.get(position);
            Intent intent = new Intent(FlashcardsListActivity.this, FlashcardDetailsActivity.class);
            intent.putExtra("setId", selectedSet.id);
            intent.putExtra("setName", selectedSet.name);
            startActivity(intent);
        });

        lvFlashcards.setOnItemLongClickListener((parent, view, position, id) -> {
            showEditDeleteDialog(position);
            return true;
        });

        fabAddSet.setOnClickListener(v -> {
            Intent intent = new Intent(FlashcardsListActivity.this, CreateFlashcardsActivity.class);
            createSetLauncher.launch(intent);
        });

        btnBackToMenu.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSetsFromDatabase();
    }

    private void loadSetsFromDatabase() {
        repository.getAllSets(result -> {
            runOnUiThread(() -> {
                flashcardSetObjects.clear();
                flashcardSetObjects.addAll(result);

                flashcardSetNames.clear();
                for (FlashcardSet set : flashcardSetObjects) {
                    flashcardSetNames.add(set.name);
                }

                adapter.notifyDataSetChanged();
            });
        });
    }

    private void showEditDeleteDialog(final int position) {
        final String[] options = {"Edytuj nazwę", "Usuń zestaw"};
        final FlashcardSet selectedSet = flashcardSetObjects.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz akcję dla: " + selectedSet.name);
        builder.setItems(options, (dialog, which) -> {
            if (options[which].equals("Edytuj nazwę")) {
                showEditSetDialog(selectedSet);
            } else if (options[which].equals("Usuń zestaw")) {
                showDeleteConfirmationDialog(selectedSet);
            }
        });
        builder.show();
    }

    private void showEditSetDialog(final FlashcardSet setToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edytuj nazwę zestawu");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(setToEdit.name);
        builder.setView(input);

        builder.setPositiveButton("Zapisz", (dialog, which) -> {
            String newSetName = input.getText().toString().trim();
            if (!newSetName.isEmpty()) {
                setToEdit.name = newSetName;
                repository.updateSet(setToEdit);
                loadSetsFromDatabase();
                Toast.makeText(FlashcardsListActivity.this, "Nazwa została zmieniona", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FlashcardsListActivity.this, "Nazwa nie może być pusta!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmationDialog(final FlashcardSet setToDelete) {
        new AlertDialog.Builder(this)
                .setTitle("Usuwanie zestawu")
                .setMessage("Czy na pewno chcesz usunąć zestaw \"" + setToDelete.name + "\"? Ta operacja usunie również wszystkie fiszki w tym zestawie i jest nieodwracalna.")
                .setPositiveButton("Usuń", (dialog, which) -> {
                    repository.deleteSet(setToDelete);
                    loadSetsFromDatabase();
                    Toast.makeText(FlashcardsListActivity.this, "Zestaw usunięty", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Anuluj", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}