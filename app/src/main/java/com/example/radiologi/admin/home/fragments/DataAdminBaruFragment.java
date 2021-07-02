package com.example.radiologi.admin.home.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radiologi.ListitemAdmin;
import com.example.radiologi.R;
import com.example.radiologi.RoomAdmin;
import com.example.radiologi.SharedPreferenceManager;
import com.example.radiologi.TerimaAdmin;
import com.example.radiologi.admin.home.fragments.AdapterAdmin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdminBaruFragment extends Fragment {

    String url_admin = "https://dbradiologi.000webhostapp.com/api/users/admindata";

    String nip;
    ArrayList listRegis;
    private List<ListitemAdmin> adminList;
    private SwipeRefreshLayout SwipeRefreshAdmin;
    AdapterAdmin adapterAdmin;
    TextView kosong;
    ImageButton imageButton;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewDataAdminBaru =  inflater.inflate(R.layout.fragment_data_admin_baru, container, false);

        listRegis = new ArrayList<>();

        SwipeRefreshAdmin = viewDataAdminBaru.findViewById(R.id.swipe_admin_data_baru);
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

        nip = SharedPreferenceManager.getStringPreferences(getContext(), "nip");
        Log.i("regina", nip);
        adapterAdmin = new AdapterAdmin(getContext(), 0);

        kosong = viewDataAdminBaru.findViewById(R.id.teks_kosong);

        recyclerView = (RecyclerView) viewDataAdminBaru.findViewById(R.id.recycler_admin_data_baru);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterAdmin);
        adapterAdmin.setOnClickListener(new AdapterAdmin.OnItemClickListener() {
            @Override
            public void onItemClick(ListitemAdmin listitemAdmin) {
                Intent intent = new Intent(getContext(), TerimaAdmin.class);
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

        FloatingActionButton fab = viewDataAdminBaru.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RoomAdmin.class);
                intent.putExtra("regis", listRegis);
                startActivity(intent);
            }
        });

        adminList = new ArrayList<>();
        dataAdminReq();

        return viewDataAdminBaru;
    }

    public void dataAdminReq() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Mohon Tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        adminList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url_admin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("regina", response);
                        try {
                            JSONObject objectResponse = new JSONObject(response);
                            String objek = objectResponse.getString("status");
                            Log.i("regina", objek);
                            if (objectResponse.getString("status").equals("kosong")) {
                                Log.i("regina", "kosong");
                                kosong.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else if (objectResponse.getString("status").equals("sukses")){
                                Log.i("regina", "tidak kosong");
                                kosong.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONArray array = objectResponse.getJSONArray("data");
                                adapterAdmin.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    ListitemAdmin modelAdmin = new ListitemAdmin();
                                    modelAdmin.setNoRegis(array.getJSONObject(i).optString("noregis"));
                                    modelAdmin.setNoRekam(array.getJSONObject(i).optString("norekam"));
                                    modelAdmin.setNamaLengkap(array.getJSONObject(i).optString("namapasien"));
                                    modelAdmin.setTangLahir(array.getJSONObject(i).optString("tanglahir"));
                                    modelAdmin.setGender(array.getJSONObject(i).optString("gender"));
                                    modelAdmin.setGambar(array.getJSONObject(i).optString("gambar"));
                                    modelAdmin.setStatus(array.getJSONObject(i).optString("status"));
                                    modelAdmin.setDiagnosa(array.getJSONObject(i).optString("diagnosa"));
                                    modelAdmin.setTdt(array.getJSONObject(i).optString("ttd"));
                                    if (modelAdmin.getStatus().equals("0")) {
                                        adminList.add(modelAdmin);
                                    }

//                                    ModelRegis modelRegis = new ModelRegis();
//                                    modelRegis.setNoRegis(array.getJSONObject(i).optString("noregis"));
                                    String regis = array.getJSONObject(i).optString("noregis");
                                    listRegis.add(regis);
                                }
                                adapterAdmin.addAll(adminList);
                                adapterAdmin.notifyDataSetChanged();

                                /*SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(listRegis);
                                editor.putString("list", json);
                                editor.apply();
                                Log.i("regina", listRegis.toString());*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("regina", e.getMessage());
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Regina", String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nip", nip);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}