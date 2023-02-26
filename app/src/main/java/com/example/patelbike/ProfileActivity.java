package com.example.patelbike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.patelbike.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private OrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        binding.email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        ordersAdapter=new OrdersAdapter(this);
        binding.ordersRecycler.setAdapter(ordersAdapter);
        binding.ordersRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getOrders();
    }

    private void getOrders(){
        FirebaseFirestore.getInstance()
                .collection("orders")
                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            OrderModel orderModel = ds.toObject(OrderModel.class);
                            ordersAdapter.addProduct(orderModel);
                        }
                    }
                });

    }
}