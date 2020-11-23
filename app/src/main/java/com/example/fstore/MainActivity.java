package com.example.fstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setDataOnFireStore();
//        addDataOnFirestore();
//        setDataAndMerge();
//        updateData();
        setCustomObject();
//        getDataFromFstore();
//        getAllDocs();

    }

    private void getAllDocs() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot q:task.getResult()){
                                Toast.makeText(MainActivity.this, q.getId()+" "+q.getData(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setCustomObject() {
        Student student = new Student("Swapnil", "F", 19, 9.2f);
        db.collection("users")
                .document("students")
                .set(student);

    }

    private void updateData() {
        db.collection("users")
                .document("swapnil")
                .update("locality", "Mansarovar")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataAndMerge() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("number", "8949828208");
        db.collection("users")
                .document("swapnil")
                .set(user, SetOptions.merge());

    }

    private void getDataFromFstore() {
        DocumentReference docRef = db.collection("users").document("swapnil");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        Toast.makeText(MainActivity.this, "" + snapshot.getData().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "No such doc exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setDataOnFireStore() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "Swapnil Bhojwani");
        user.put("email", "swapnil.bhojwani@gmail.com");
        user.put("college", "ju");
        user.put("locality", "Agarwal farm");

        db.collection("users")
                .document("swapnil")
                .set(user);

    }

    private void addDataOnFirestore() {

        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "Swapnil Bhojwani");
        user.put("email", "swapnil.bhojwani@gmail.com");
        user.put("college", "ju");
        user.put("locality", "Agarwal farm");
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        })
        ;
    }
}