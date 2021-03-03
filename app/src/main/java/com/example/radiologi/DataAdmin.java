 package com.example.radiologi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdmin extends AppCompatActivity {

    String url_cek = "https://dbradiologi.000webhostapp.com/api/users/cektoken";

    AdapterAdmin adapterAdmin;
    TextView kosong;
    RecyclerView recyclerView;
    ImageButton imageButton;
    String msg, tokenLama;

    private SwipeRefreshLayout SwipeRefreshAdmin;

    private List<ListitemAdmin> adminList;
    String nip, token;

    ArrayList listRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_admin);

        tokenLama = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "token");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("regina", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                token = task.getResult();
                msg = getString(R.string.msg_token_fmt, token);
                Log.d("regina", token);

                cekToken();
                /*if (tokenLama.equals(token)) {
                    Log.d("regina", "testestes" + tokenLama);
                    Toast.makeText(getApplicationContext(), tokenLama, Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("regina", "token dijalankan");
                }*/
            }
        });

        final TabLayout tabLayout = findViewById(R.id.tab);
        final ViewPager viewPager = findViewById(R.id.admin_pager);

        DataAdminPagerAdapter pagerAdapter = new DataAdminPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imageButton = findViewById(R.id.option);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*RelativeLayout relativeLayout = view.findViewById(R.id.relative_layout);
                relativeLayout.setVisibility(View.VISIBLE);
                FragmentAdmin fragmentAdmin = new FragmentAdmin();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.relative_layout, fragmentAdmin, FragmentAdmin.class.getSimpleName()).addToBackStack(null).commit();*/
                showDialog();
            }
        });


        /*listRegis = new ArrayList<>();

        SwipeRefreshAdmin = findViewById(R.id.swipe_admin);
        SwipeRefreshAdmin.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefreshAdmin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefreshAdmin.setRefreshing(false);

                        dataAdminReq();
                    }
                },4000);
            }
        });

        nip = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "nip");
        Log.i("regina", nip);
        adapterAdmin = new AdapterAdmin(getApplicationContext());

        kosong = findViewById(R.id.teks_kosong);
        imageButton = findViewById(R.id.option);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*RelativeLayout relativeLayout = findViewById(R.id.relative_layout);
                relativeLayout.setVisibility(View.VISIBLE);
                FragmentAdmin fragmentAdmin = new FragmentAdmin();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.relative_layout, fragmentAdmin, FragmentAdmin.class.getSimpleName()).addToBackStack(null).commit();*//*
                showDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterAdmin);
        adapterAdmin.setOnClickListener(new AdapterAdmin.OnItemClickListener() {
            @Override
            public void onItemClick(ListitemAdmin listitemAdmin) {
                Intent intent = new Intent(DataAdmin.this, TerimaAdmin.class);
                intent.putExtra("noregis", listitemAdmin.getNoRegis());
                intent.putExtra("norekam", listitemAdmin.getNoRekam());
                intent.putExtra("namalengkap", listitemAdmin.getNamaLengkap());
                intent.putExtra("tanggalahir", listitemAdmin.getTangLahir());
                intent.putExtra("gender", listitemAdmin.getGender());
                intent.putExtra("gambar", listitemAdmin.getGambar());
                intent.putExtra("untuk", "admin");
                intent.putExtra("diagnosa", listitemAdmin.getDiagnosa());
                intent.putExtra("tdt", listitemAdmin.getTdt());
                intent.putExtra("status", listitemAdmin.getStatus());
                startActivity(intent);
            }
        });

        adminList = new ArrayList<>();
        dataAdminReq();*/

    }


    private void cekToken() {
        StringRequest request = new StringRequest(Request.Method.POST, url_cek, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object= new JSONObject(response);
                    if (object.getString("text").equals("sukses")) {
                        SharedPreferenceManager.savesStringPreferences(getApplicationContext(), "token", msg);

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("regina", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nip", nip);
                param.put("token", token);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "islogin", false );
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}