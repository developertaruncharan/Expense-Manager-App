package com.example.bestexpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout ;
    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        bottomNavigationView=findViewById(R.id.bottomNavigationBar);
        mAuth=FirebaseAuth.getInstance();
        frameLayout=findViewById(R.id.main_frame);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Navigation_drawer_open,R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        dashboardFragment= new DashboardFragment();
        incomeFragment = new IncomeFragment();
        expenseFragment = new ExpenseFragment();

        setFragment(dashboardFragment);

        NavigationView navigationView = findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.dashboards :
                        bottomNavigationView.setBackgroundResource(R.color.color_dashboard);
                        setFragment(dashboardFragment);
                        Toast.makeText(HomeActivity.this, "dash", Toast.LENGTH_SHORT).show();
                        break;


                    case R.id.incomes :
                        setFragment(incomeFragment);
                        bottomNavigationView.setBackgroundResource(R.color.color_income);
                        Toast.makeText(HomeActivity.this, "income", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.expencess :
                        bottomNavigationView.setBackgroundResource(R.color.color_expense);
                        setFragment(expenseFragment);
                        Toast.makeText(HomeActivity.this, "expense", Toast.LENGTH_SHORT).show();
                        break;


                }
            }
        });
    }
    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }else {
            super.onBackPressed();
        }
    }

    public  void displaySelectedListener(int itemId){
        Fragment fragment=null;

        switch (itemId) {
            case R.id.dashboard:
                fragment=new DashboardFragment();
                break;
            case R.id.income:
                fragment=new IncomeFragment();
                break;
            case R.id.expense:
                fragment=new ExpenseFragment();
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }
        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame,fragment);
            ft.commit();
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        displaySelectedListener(menuItem.getItemId());
        return true;
    }
}