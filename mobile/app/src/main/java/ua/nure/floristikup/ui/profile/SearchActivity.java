//package ua.nure.floristikup.ui.profile;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageButton;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import ua.nure.floristikup.R;
//import ua.nure.floristikup.data.FloristShop;
//import ua.nure.floristikup.network.JSONPlaceHolderApi;
//import ua.nure.floristikup.network.NetworkService;
//import ua.nure.floristikup.ui.rva.SearchRVA;
//
//public class SearchActivity extends AppCompatActivity {
//
//    private static final String TAG = "SearchActivity";
//
//    private List<FloristShop> mCompanies;
//    private RecyclerView mRecyclerView;
//    private JSONPlaceHolderApi mApi;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.search_rv);
//
//        ImageButton mBackButton = findViewById(R.id.back_btn);
//
//        mApi = NetworkService.getInstance().getApiService();
//
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//
//        mRecyclerView.setLayoutManager(llm);
//        mRecyclerView.setHasFixedSize(true);
//
//        mBackButton.setOnClickListener((v) -> {
//            navigateToScreen(MenuActivity.class);
//            finish();
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mCompanies = new ArrayList<>();
//        initializeData();
//    }
//
//    private void initializeData() {
//        String token = "Bearer " + FloristShop.getInstance().getToken();
//        mApi.getFloristShops(token).enqueue(searchCallback);
//    }
//
//
//    Callback<ArrayList<FloristShop>> searchCallback = new Callback<ArrayList<FloristShop>>() {
//        @Override
//        public void onResponse(Call<ArrayList<FloristShop>> call, Response<ArrayList<FloristShop>> response) {
//            if (!response.isSuccessful()) {
//                System.out.println(response.code());
//                return;
//            }
//            ArrayList<FloristShop> companies = response.body();
//            for (FloristShop floristShop : companies) {
//                mCompanies.add(new FloristShop(floristShop.getId(), floristShop.getName(), floristShop
//                        .getEmail(), floristShop.getPhoneNumber()));
//            }
//            initializeAdapter();
//        }
//
//        @Override
//        public void onFailure(Call<ArrayList<FloristShop>> call, Throwable t) {
//            System.out.println(t);
//            Log.i(TAG, t.getMessage());
//        }
//    };
//
//    private void initializeAdapter() {
//        SearchRVA adapter = new SearchRVA(this, mCompanies);
//        mRecyclerView.setAdapter(adapter);
//    }
//
//    private void navigateToScreen(Class cls) {
//        Intent intent = new Intent(SearchActivity.this,
//                cls);
//        startActivity(intent);
//    }
//}