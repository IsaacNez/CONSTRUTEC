package construtec.construtec;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
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

import org.json.JSONArray;
import org.json.JSONException;

public class UserInterface extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView username;
    TextView emailuser;
    JSONArray arrayrole;
    String user="";
    Integer userID = 0;
    Integer userCode = 0;
    String jsonArray = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent example = getIntent();
        jsonArray = example.getStringExtra("jsonArray");
        System.out.println(jsonArray);
        user = example.getStringExtra("Name");
        userID = example.getIntExtra("UID", 1);
        userCode = example.getIntExtra("UCode",0);
        try {
             arrayrole = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        getMenuInflater().inflate(R.menu.user_interface, menu);
        username = (TextView) findViewById(R.id.Name_User);
        emailuser = (TextView) findViewById(R.id.textView);
        username.setText(user);
        emailuser.setText("Your ID: "+userID);
        NavigationView nav1 = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_menu = nav1.getMenu();
        nav_menu.findItem(R.id.nav_first_layout).setVisible(false);
        nav_menu.findItem(R.id.nav_second_layout).setVisible(false);
        nav_menu.findItem(R.id.nav_third_layout).setVisible(false);
        nav_menu.findItem(R.id.nav_assign_stage).setVisible(false);
        nav_menu.findItem(R.id.nav_assign_products).setVisible(false);
        nav_menu.findItem(R.id.nav_gen_budget).setVisible(false);
        nav_menu.findItem(R.id.nav_send_order).setVisible(false);
        nav_menu.findItem(R.id.nav_search_product).setVisible(false);
        nav_menu.findItem(R.id.nav_search_date).setVisible(false);
        for(int i = 0; i < arrayrole.length();i++){
            try {
                if(arrayrole.getInt(i)==1){
                    nav_menu.findItem(R.id.nav_first_layout).setVisible(true);
                    nav_menu.findItem(R.id.nav_second_layout).setVisible(true);
                }
                if(arrayrole.getInt(i)==2){
                    nav_menu.findItem(R.id.nav_second_layout).setVisible(true);
                    nav_menu.findItem(R.id.nav_third_layout).setVisible(true);
                    nav_menu.findItem(R.id.nav_assign_stage).setVisible(true);
                    nav_menu.findItem(R.id.nav_assign_products).setVisible(true);
                    nav_menu.findItem(R.id.nav_gen_budget).setVisible(true);
                    nav_menu.findItem(R.id.nav_send_order).setVisible(true);
                }
                if(arrayrole.getInt(i)==3){
                    nav_menu.findItem(R.id.nav_assign_products).setVisible(true);
                    nav_menu.findItem(R.id.nav_send_order).setVisible(true);
                }
                if(arrayrole.getInt(i)==4){
                    nav_menu.findItem(R.id.nav_search_product).setVisible(true);
                    nav_menu.findItem(R.id.nav_search_date).setVisible(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
        Bundle extradata = new Bundle();
        extradata.putString("role",arrayrole.toString());
        extradata.putInt("u_id", userID);
        extradata.putInt("u_code", userCode);
        extradata.putString("u_name", user);
        extradata.putString("jsonArray",jsonArray);
        FragmentManager fragmgr = getFragmentManager();

        if (id == R.id.nav_first_layout) {
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, new FirstFragment())
                    .commit();
            // Handle the camera action
        } else if (id == R.id.nav_second_layout) {
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, new SecondFragment())
                    .commit();
        } else if (id == R.id.nav_third_layout) {
            Fragment todo = new ThirdFragment();
            todo.setArguments(extradata);
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, todo)
                    .commit();
        } else if (id == R.id.nav_assign_stage){
            Fragment _assignstage = new AssignStage();
            _assignstage.setArguments(extradata);
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, _assignstage)
                    .commit();
        }else if (id == R.id.nav_assign_products){
            Fragment todo = new AssignProduct();
            todo.setArguments(extradata);
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame,todo)
                    .commit();

        }else if (id == R.id.nav_gen_budget){
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, new GenBudget())
                    .commit();
        }else if (id == R.id.nav_send_order){
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, new SendOrder())
                    .commit();
        }else if (id == R.id.nav_search_date){
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, new SearchByDate())
                    .commit();
        }else if (id == R.id.nav_search_product){
            fragmgr.beginTransaction()
                    .replace(R.id.content_frame, new SearchByProduct())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
