package br.com.siecola.helloworldturbo;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.siecola.helloworldturbo.fragments.Fragment_login;
import br.com.siecola.helloworldturbo.fragments.GCMFragment;
import br.com.siecola.helloworldturbo.fragments.OrdersFragment;
import br.com.siecola.helloworldturbo.fragments.ProductsFragment;
import br.com.siecola.helloworldturbo.fragments.SettingsFragment;
import br.com.siecola.helloworldturbo.fragments.Tela1Fragment;
import br.com.siecola.helloworldturbo.models.ProductInfo;
import br.com.siecola.helloworldturbo.fragments.ListaPedidosFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private static final int PERMISSION_REQUEST_INTERNET = 10;
//    private static final int PERMISSION_REQUEST_NETWORK_STATE = 11;

    private ProductInfo productInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = this.getIntent();
        if (intent.hasExtra("productInfo")) {
            productInfo = (ProductInfo) intent.
                    getSerializableExtra("productInfo");
            if (productInfo != null) {
                displayFragment(R.id.nav_gcm);
            }
        } else if (savedInstanceState == null) {
            displayFragment(R.id.nav_tela1);
        }

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.INTERNET)
//                != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.INTERNET},
//                        PERMISSION_REQUEST_INTERNET);
//        }
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_NETWORK_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
//                    PERMISSION_REQUEST_NETWORK_STATE);
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra("productInfo")) {
            productInfo = (ProductInfo) intent.
                    getSerializableExtra("productInfo");
            if (productInfo != null) {
                displayFragment(R.id.nav_gcm);
            }
        }
        super.onNewIntent(intent);
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

    //Trata o clique no botão do menu, retornando o ID que será usado dentro do case do método
    // displayFragment
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayFragment(item.getItemId());
        return true;
    }

    //O método display fragment recebe o ID tratado do botão da barra de menus e chama o fragment
    //corrrespondente para ser inflado
    private void displayFragment (int fragmentId) {
        Class fragmentClass;
        Fragment fragment = null;

        int backStackEntryCount;
        backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        for (int j = 0; j < backStackEntryCount; j++) {
            getFragmentManager().popBackStack();
        }

        try {
            switch (fragmentId) {
                case R.id.nav_tela1:
                    //fragmentClass = Tela1Fragment.class;
                    fragmentClass = Fragment_login.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
                case R.id.nav_lista_pedidos:
                    fragmentClass = OrdersFragment.class;
                    //fragmentClass = ListaPedidosFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
                case R.id.nav_gcm:
                    fragmentClass = GCMFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    if (productInfo != null) {
                        Bundle args = new Bundle();
                        args.putSerializable("productInfo", productInfo);
                        fragment.setArguments(args);
                        productInfo = null;
                    }
                    break;
                case R.id.nav_settings:
                    //fragmentClass = SettingsFragment.class;
                    fragmentClass = ProductsFragment.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
                default:
                    //fragmentClass = Tela1Fragment.class;
                    fragmentClass = Fragment_login.class;
                    fragment = (Fragment) fragmentClass.newInstance();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container,
                    fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
