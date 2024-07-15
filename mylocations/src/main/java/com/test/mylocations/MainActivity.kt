package com.test.mylocations

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.mylocations.databinding.ActivityMainMylocationsBinding

/**
 * Contenedor principal de la UI
 * En esta actividad se muestran los fragmentos
 * [TodoFragment],[DoingFragment],[DoneFragment]
 */
class MainActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityMainMylocationsBinding
    private lateinit var navController: NavController
    private lateinit var vm: MainViewModel
    private val pm  =
        PermisionManager(
            arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION )
            ,{
                ActivityCompat.checkSelfPermission( this,  it ) == PackageManager.PERMISSION_GRANTED
            }
            ,{
                shouldShowRequestPermissionRationale(it)
            }
            ,{ per,retry->
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.permissions_title)
                    .setMessage(R.string.location_permissions_request)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok ) { _, _ ->
                      retry(per)
                    }.show()
            }
        ).apply {
            registerActivityForResult(this@MainActivity)
        }

    init
    {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[MainViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        pm.proceed = { vm.getCurrentLocation(fusedLocationClient) }
        pm.request()
        binding = ActivityMainMylocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listFragment, R.id.mapsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        vm.msgs.observe(this, Observer { errorMessage ->
            // Handle error message display or logging
            errorMessage ?:return@Observer
            MaterialAlertDialogBuilder(this)
                .setMessage(getString(R.string.error,errorMessage))
                .setTitle(R.string.alert)
                .show()
            vm.clearError()
            Log.e("Pokemon", "Error: $errorMessage")
        })
    }

    override fun onSupportNavigateUp(): Boolean
    {
        return if (::navController.isInitialized)
        {
            navController.navigateUp()
        } else
        {
            false
        }
    }
}