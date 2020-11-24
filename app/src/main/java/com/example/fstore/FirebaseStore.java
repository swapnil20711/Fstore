package com.example.fstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.core.OrderBy;

import java.util.HashMap;

public class FirebaseStore extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_store);
//        setAccountData();
//        setDataOnFireStore();
//        deleteDataFromFstore();
//        batchWrite();
//        txn();
//        orderAccountsByBal();
        listenToRealtimeUpdates();
    }

    private void listenToRealtimeUpdates() {
        db.collection("users")
                .document("swapnil")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e!=null){
                            Toast.makeText(FirebaseStore.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        if (snapshot!=null&&snapshot.exists()){
                            Toast.makeText(FirebaseStore.this, ""+snapshot.getId()+" "+snapshot.get("name"), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(FirebaseStore.this, "Details does not exist!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        ;
    }

    private void orderAccountsByBal() {
        Query query = db.collection("accounts").orderBy("bal", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        Toast.makeText(FirebaseStore.this, "" + snapshot.getData(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void txn() {
        final int amt = 20;
        //To run a transaction
        db.runTransaction(new Transaction.Function<Integer>() {
            @Nullable
            @Override
            public Integer apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                //Get account a
                Account a = transaction.get(db.collection("accounts").document("a"))
                        .toObject(Account.class);
                if (a.bal < amt) {
                    return -1;
                }
                //Update account a by decrementing balance
                transaction.update(db.collection("accounts").document("a"), "bal", FieldValue.increment(-amt));
                //Update account b by increamenting balance
                transaction.update(db.collection("accounts").document("b"), "bal", FieldValue.increment(amt));
                return a.bal - amt;
            }
        }).addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer bal) {
                if (bal == -1) {
                    new AlertDialog.Builder(FirebaseStore.this)
                            .setTitle("Insufficient balance")
                            .setMessage("Insufficient balance in the account")
                            .show();
                } else {
                    new AlertDialog.Builder(FirebaseStore.this)
                            .setMessage("Your balance is " + bal)
                            .setTitle("Transaction completed")
                            .show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FirebaseStore.this, "Failure!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAccountData() {
        WriteBatch batch = db.batch();

        batch.set(db.collection("accounts").document("a"), new Account(100, "a"));
        batch.set(db.collection("accounts").document("b"), new Account(50, "b"));

        batch.commit();
    }

    private void batchWrite() {
        WriteBatch batch = db.batch();
        DocumentReference user1 = db.collection("users")
                .document("emiway");
        DocumentReference user2 = db.collection("users")
                .document("krishna");
        DocumentReference user3 = db.collection("users")
                .document("raftaar");

        batch.update(user1, "college", "oldschool");
        batch.update(user2, "college", "newschool");
        batch.update(user3, "name", "raa");


        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FirebaseStore.this, "Batch writed succesfully!!", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FirebaseStore.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        })
        ;


    }

    private void deleteDataFromFstore() {
        db.collection("users")
                .document("swapnil").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FirebaseStore.this, "User deleted from db", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FirebaseStore.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataOnFireStore() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "raftaar");
        user.put("email", "raftaar.raa@gmail.com");
        user.put("college", "india");
        user.put("locality", "delhi");

        db.collection("users")
                .document("raftaar")
                .set(user);

    }

}