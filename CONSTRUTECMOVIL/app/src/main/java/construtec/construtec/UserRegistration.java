package construtec.construtec;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class UserRegistration extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView Thint;
    TextView TGre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent example = getIntent();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_registration);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_re) {
            Toast.makeText(getApplicationContext(),"Create a new profile as an enginner",Toast.LENGTH_LONG);
            TGre = (TextView) findViewById(R.id.greetings);
            Thint = (TextView) findViewById(R.id.hint);
            TGre.setVisibility(View.INVISIBLE);
            Thint.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.registration_frame,new RegistrateEngineer())
                    .commit();
            // Handle the camera action
        } else if (id == R.id.nav_rc) {
            Toast.makeText(getApplicationContext(),"Create a new profile as a customer",Toast.LENGTH_LONG);
            TGre = (TextView) findViewById(R.id.greetings);
            Thint = (TextView) findViewById(R.id.hint);
            TGre.setVisibility(View.INVISIBLE);
            Thint.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.registration_frame, new RegistrateCustomer())
                    .commit();
        } else if (id == R.id.nav_ru) {
            Toast.makeText(getApplicationContext(),"Create a new profile as customer service",Toast.LENGTH_LONG);
            TGre = (TextView) findViewById(R.id.greetings);
            Thint = (TextView) findViewById(R.id.hint);
            TGre.setVisibility(View.INVISIBLE);
            Thint.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.registration_frame, new RegistrateNormalUser())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
