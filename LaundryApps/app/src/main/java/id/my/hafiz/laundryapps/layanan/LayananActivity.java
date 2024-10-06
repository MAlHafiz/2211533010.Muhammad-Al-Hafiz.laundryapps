package id.my.hafiz.laundryapps.layanan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.my.hafiz.laundryapps.R;
import id.my.hafiz.laundryapps.adapter.AdapterLayanan;
import id.my.hafiz.laundryapps.database.SQLiteHelper2;
import id.my.hafiz.laundryapps.model.ModelLayanan;
import id.my.hafiz.laundryapps.pelanggan.PelangganAddActivity;

public class LayananActivity extends AppCompatActivity {
    SQLiteHelper2 db;
    TextView btnLayAdd;
    RecyclerView rvLayanan;
    AdapterLayanan adapterLayanan;
    ArrayList<ModelLayanan> list;
    ProgressDialog progressDialog;
    AlphaAnimation btnAnimasi = new AlphaAnimation(1F,0.5F);
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_layanan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                            systemBars.bottom);
                    return insets;
                });
        setView();
        eventHandling();
        getData();

    }
    private View.OnClickListener onClickListener = new
            View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(btnAnimasi);
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)
                            v.getTag();
                    int position = viewHolder.getAdapterPosition();
                    ModelLayanan mp = list.get(position);
                    Toast.makeText(LayananActivity.this, "" +mp.getTipe(),
                            Toast.LENGTH_SHORT).show();
                }
            };

    private void getData() {
        list.clear();
        showMsg();
        progressDialog.dismiss();
        try {
            List<ModelLayanan> l = db.getLayanan();
            if (l.size() > 0){
                for (ModelLayanan lay : l){
                    ModelLayanan ml = new ModelLayanan();
                    ml.setId(lay.getId());
                    ml.setTipe(lay.getTipe());
                    ml.setHarga(lay.getHarga());
                    list.add(ml);
                }
                adapterLayanan = new AdapterLayanan(this, list);
                adapterLayanan.notifyDataSetChanged();
                rvLayanan.setAdapter(adapterLayanan);
                adapterLayanan.setOnItemClickListener(onClickListener);
            }else{
                Toast.makeText(this, "Data tidak ditemukan",
                        Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }

    private void eventHandling() {
        btnLayAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LayananActivity.this,
                        LayananAddActivity.class));
            }
        });
    }

    private void setView() {
        db = new SQLiteHelper2(this);
        progressDialog = new ProgressDialog(this);
        btnLayAdd = (TextView) findViewById(R.id.btnLayAdd);
        rvLayanan = (RecyclerView) findViewById(R.id.rvLayanan);
        list = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvLayanan.setHasFixedSize(true);
        rvLayanan.setLayoutManager(llm);
    }
    private void showMsg() {
        progressDialog.setTitle("Informasi");
        progressDialog.setMessage("Loading Data..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}