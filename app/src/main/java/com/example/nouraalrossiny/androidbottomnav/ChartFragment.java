package com.example.nouraalrossiny.androidbottomnav;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nouraalrossiny.androidbottomnav.Database.Database;
import com.example.nouraalrossiny.androidbottomnav.model.Order;
import com.example.nouraalrossiny.androidbottomnav.model.Request;
import com.example.nouraalrossiny.androidbottomnav.viewHolder.CartAdapter;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database ;
    DatabaseReference  requests;
    View view;
    TextView txtTotalPrice;
    Button btnplace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView =inflater.inflate(R.layout.fragment_chart, container, false);
        // Inflate the layout for this fragment

        //Firebase
         database = FirebaseDatabase.getInstance();
         requests =database.getReference("Requests");
        //requests = database.getReference("Requsts");

        //Init

        recyclerView =(RecyclerView)RootView.findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
       // layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)RootView.findViewById(R.id.total);
        btnplace = (Button)RootView.findViewById(R.id.btnPlaceOrder);

        btnplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create new Request

                showAlertDialog();

            }
        });
        loadListFood();



        return RootView;


    }

    private void showAlertDialog() {
        AlertDialog.Builder al = new AlertDialog.Builder(getActivity());

      //  AlertDialog.Builder al = new AlertDialog.Builder(ChartFragment.this);
        al.setTitle("one more step");
        al.setMessage("Enter your address");

       // final EditText add= new EditText(ChartFragment.this);
        final EditText add= new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        add.setLayoutParams(lp);
        al.setView(add);
        al.setIcon(R.drawable.ic_arrow_left_black_48dp);

        al.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ممكن هذي اضيفها بال payment activity
                Request request = new Request(
                        //Common.currentUser.getphone(),
                        //Common.currentUser.getName(),
                        //addres,
                        "7777","Amal",
                        add.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                // Submite To firebase
                //We will using System.CurrentMilli to key
                 requests.child(String.valueOf(System.currentTimeMillis()))
                .setValue(request);
//Delete Cart:

                // new Database(getBaseContext()).cleanCart();
              new Database(getActivity()).CleanCart();
                Toast.makeText(getActivity(), "Thank you", Toast.LENGTH_SHORT).show();
                // finish();
            }
        });

        al.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        al.show();
    }

    private void loadListFood() {
       //cart = new Database(this).getCarts();
        cart = new Database(getActivity()).getCarts();

        //cart = new Database(this).getCarts();
       // adapter = new CartAdapter(cart,this);
        //adapter = new CartAdapter(cart,this);
        adapter = new CartAdapter(cart,getActivity());
        recyclerView.setAdapter(adapter);

        //Calculate total price
        int total = 0;
        for (Order order:cart) {
//Minute 36
           // int Item_order = Integer.parseInt(order.getPrice());
            int Item_order = Integer.parseInt("3");
            int Item_Q = (Integer.parseInt(order.getQuantity()));

            total +=Item_order * Item_Q;
        }
        Locale locale = new Locale("ar","KSA");
        // Locale locale = new Locale("ar","KSA");

        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));





    }

}
