package com.example.wallpeparapp.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallpeparapp.Adapters.CategoryRVAdapter;
import com.example.wallpeparapp.Models.CategoryRVModal;
import com.example.wallpeparapp.R;
import com.example.wallpeparapp.Utils.CacheUtils;
import com.example.wallpeparapp.Utils.ImageUrlCategory;
import com.example.wallpeparapp.Utils.LoadingDialog;
import com.example.wallpeparapp.Utils.ThemeUtils;
import com.example.wallpeparapp.Utils.ToastUtil;
import com.example.wallpeparapp.Utils.utils;
import com.example.wallpeparapp.Adapters.WallpaperRVAdapter;
import com.example.wallpeparapp.databinding.ActivityHomeScreenBinding;
import com.example.wallpeparapp.splash.MainActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeScreenActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {


    private static ThemeUtils themeUtils;
    private static int themeValue = 2;
    private ArrayList<CategoryRVModal> categoryRVModals;
    private ArrayList<String> wallpaperArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private WallpaperRVAdapter wallpaperRVAdapter;
    private ActionBarDrawerToggle toggle;
    private Menu MainMenu = null;
    private NavigationView navigationView;
    private LoadingDialog loadingDialog;
    ActivityHomeScreenBinding binding;
    Animation top, bottom, right, left;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils = new ThemeUtils(this);
        themeState(themeUtils.getThemeState("Theme"));
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        right = AnimationUtils.loadAnimation(this, R.anim.right);
        binding.card.setAnimation(right);
        binding.idRVCategories.setAnimation(top);
        binding.idRVWallpapers.setAnimation(bottom);

        loadingDialog = new LoadingDialog(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = findViewById(R.id.drawyerlayout);

        toggle = new ActionBarDrawerToggle(HomeScreenActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.getItemIconTintList();

        navigationView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                ToastUtil.Intent(this, MainActivity.class);
            } else if (itemId == R.id.nav_sharemenu) {
                utils.share(this);
            } else if (itemId == R.id.nav_review) {
                utils.Review(this);
            } else if (itemId == R.id.nav_mode1) {
                mode(this);
            } else if (itemId == R.id.nav_calculator) {
                ToastUtil.Intent(this, FavoriteWallpapersActivity.class);
            } else if (itemId == R.id.About) {
                utils.showbottomdialog(this);
            } else if (itemId == R.id.Contact) {
                ToastUtil.Intent(this, ContactActivity.class);
            } else if (itemId == R.id.cache) {
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.chachedialog);
                Button yes = dialog.findViewById(R.id.yes);
                Button no = dialog.findViewById(R.id.no);
                dialog.show();
                yes.setOnClickListener(v -> {
                    CacheUtils.clearCache(this);
                    dialog.dismiss();
                    loadingDialog.show();
                });
                no.setOnClickListener(v -> {
                    dialog.dismiss();
                });
                loadingDialog.dismiss();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        wallpaperArrayList = new ArrayList<>();
        categoryRVModals = new ArrayList<>();
        //creating a layout manager for recycler view which is our category.
        LinearLayoutManager manager1 = new LinearLayoutManager(HomeScreenActivity.this, RecyclerView.HORIZONTAL, false);
        //initializing our adapter class on below line.
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModals, this, this);
        //setting layout manager to our category recycler view as horizontal.
        binding.idRVCategories.setLayoutManager(manager1);
        binding.idRVCategories.setAdapter(categoryRVAdapter);
        //creating a grid layout manager for our wallpaper recycler view.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //setting layout manager and adapter to our recycler view.
        binding.idRVWallpapers.setLayoutManager(layoutManager);
        binding.idRVWallpapers.setAdapter(wallpaperRVAdapter);

        //on below line we are calling method to get categories to add data in array list.
        getCategories();

        //on below line we are calling get wallpaper method to get data in wallpaper array list.
        getWallpapers();
        //on below line we are adding on click listner for search image view on below line.

        binding.idEdtSearch.setOnClickListener(v ->

        {
            //inside on click method we are getting data from our search edittext and validating if the input field is empty or not.
            String searchStr = binding.idEdtSearch.getText().toString();
            if (searchStr.isEmpty()) {
                ToastUtil.showToast(this, getString(R.string.image_not_loaded));
            } else {
                //on below line we are calling a get wallpaper method to get wallpapers by category.
                getWallpapersByCategory(searchStr);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkthemecolor)); // Replace with your desired color
            }
            navigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.darkthemecolor));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.ColorOrange)); // Replace with your desired color
            }
            navigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    public void mode(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fortheme);

        RadioButton darkMode = dialog.findViewById(R.id.darkmode);
        RadioButton lightMode = dialog.findViewById(R.id.lightmode);
        RadioButton defaultMode = dialog.findViewById(R.id.defaultt);
        Button btnOK = dialog.findViewById(R.id.okselectedtheme);

        dialog.show();

        btnOK.setOnClickListener(v -> {

            themeState(themeValue);
            dialog.dismiss();
            loadingDialog.dismiss();
        });

        int currentThemeState = themeUtils.getThemeState("Theme");

        if (currentThemeState == 0) {
            darkMode.setChecked(true);
            lightMode.setChecked(false);
            defaultMode.setChecked(false);
            themeState(currentThemeState);
        } else if (currentThemeState == 1) {
            lightMode.setChecked(true);
            darkMode.setChecked(false);
            defaultMode.setChecked(false);
            themeState(currentThemeState);
        } else {
            lightMode.setChecked(false);
            darkMode.setChecked(false);
            defaultMode.setChecked(true);
            themeState(currentThemeState);
        }

        darkMode.setOnClickListener(v -> {
            darkMode.setChecked(true);
            lightMode.setChecked(false);
            defaultMode.setChecked(false);
            themeValue = 0;
        });

        lightMode.setOnClickListener(v -> {
            darkMode.setChecked(false);
            lightMode.setChecked(true);
            defaultMode.setChecked(false);
            themeValue = 1;
        });

        defaultMode.setOnClickListener(v -> {
            darkMode.setChecked(false);
            lightMode.setChecked(false);
            defaultMode.setChecked(true);
            themeValue = 2;
        });
    }

    public static void themeState(int themeState) {
        switch (themeState) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                themeUtils.setThemeState("Theme", 0);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                themeUtils.setThemeState("Theme", 1);
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                themeUtils.setThemeState("Theme", 2);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MainMenu = menu;
        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.pro) {
            ToastUtil.showToast(this, "Working on it..");
        }
        if (item.getItemId() == R.id.download_data_show) {
            ToastUtil.Intent(this, DisplayImageActivity.class);
        }
        if (item.getItemId() == R.id.nav_review) {
            utils.Review(this);
        }
        if (item.getItemId() == R.id.nav_mode1) {
            mode(this);
        }
        return super.onOptionsItemSelected(item);
    }


    //on below line we are creating a method to get the wallpaper by category.
    private void getWallpapersByCategory(String category) {
        //on below line we are clearing our array list.
        wallpaperArrayList.clear();
        //on below line we are making visibility of our progress bar as gone.
        loadingDialog.show();
        //on below line we are creating a string variable for our url and adding url to it.
        String url = getString(R.string.https_api_pexels) + category + getString(R.string.per_page);
        //on below line we are creating a new variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(HomeScreenActivity.this);
        //on below line we are making a json object request to get the data from url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                //on below line we are extracting the data from our response and passing it to our array list.
                try {
                    loadingDialog.dismiss();
//                    loadingPB.setVisibility(View.GONE);
                    //on below line we are extracting json data.
                    JSONArray photos = response.getJSONArray(getString(R.string.photos));
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject(getString(R.string.src)).getString(getString(R.string.portrait));
                        //on below line we are passing data to our array list
                        wallpaperArrayList.add(imgUrl);
                    }
                    //here we are notifying adapter that data has changed in our list.
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //handling json exception on below line.
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying a simple toast message on error response.
                ToastUtil.showToast(HomeScreenActivity.this, getString(R.string.data_failed));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //in this method passing headers as key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.authorization), getString(R.string.API_KEYS));
                //at last returning headers.
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

    }

    private void getWallpapers() {
        //on below line we are clearing our array list.
        wallpaperArrayList.clear();
        //changing visiblity of our progress bar to gone.
//        loadingPB.setVisibility(View.VISIBLE);
//        loadingDialog.show();
        //creating a variable for our url.
        String url = getString(R.string.Url);
        //on below line we are creating a new variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(HomeScreenActivity.this);
        //on below line we are making a json object request to get the data from url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                //on below line we are extracting the data from our response and passing it to our array list.
//                loadingPB.setVisibility(View.GONE);
//                loadingDialog.dismiss();
                try {

                    //on below line we are extracting json data.
                    JSONArray photos = response.getJSONArray(getString(R.string.photos));
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject(getString(R.string.src)).getString(getString(R.string.portrait));
                        //on below line we are passing data to our array list
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //handling json exception on below line.
                    e.printStackTrace();
                }
            }
        }, error -> {
            //displaying a toast message on error respone.
            ToastUtil.showToast(HomeScreenActivity.this, getString(R.string.data_failed));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //in this method passing headers as key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.authorization), getString(R.string.API_KEYS));
                //at last returning headers.
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void getCategories() {
        //on below lines we are adding data to our category array list.
        categoryRVModals.add(new CategoryRVModal(getString(R.string.gym), ImageUrlCategory.GymImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.arts), ImageUrlCategory.ArtsImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.music), ImageUrlCategory.MusicImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.abstractt), ImageUrlCategory.AbstractImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.cars), ImageUrlCategory.CarsImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.cartoons), ImageUrlCategory.CartoonsImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.cats), ImageUrlCategory.CatsImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.dogs), ImageUrlCategory.DogsImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.sadness), ImageUrlCategory.SadnessImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.winter_and_snow), ImageUrlCategory.WinterAndSnowImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.technology), ImageUrlCategory.TechnologyImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.programming), ImageUrlCategory.ProgrammingImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.nature), ImageUrlCategory.NatureImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.travel), ImageUrlCategory.TravelImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.architecture), ImageUrlCategory.ArchitectureImageUrl()));
        categoryRVModals.add(new CategoryRVModal(getString(R.string.flowers), ImageUrlCategory.FlowersImageUrl()));
    }

    @Override
    public void onCategoryClick(int position) {
        //on below line we are getting category from our array list and calling a method to get wallpapers by category.
        String category = categoryRVModals.get(position).getCategory();
        getWallpapersByCategory(category);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWallpapers();
    }

    @Override
    public void onBackPressed() {
        utils.BackpressedClosedApplication(this);
    }
}