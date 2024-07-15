package com.test.mylocations

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Gestor de permisos
 * @param permissions arreglo de permisos a solicitar
 * @param checkPermission funcion para revisar los permisos usa una cadena regresa un bool
 * @param shouldShowRequestPermissionRationale funcion para revisar los permisos que necesitan ser explicados
 * al usuario mediante una UI, usa una cadena regresa un boo
 * @param showUI funcion para mnostrar la UI de explicación de permisos
 */
class PermisionManager(
    private var permissions : Array<String>
    , val checkPermission:(String)->Boolean
    , val shouldShowRequestPermissionRationale : (String)->Boolean
    , val showUI:  (Array<String>, (Array<String>)->Unit) -> Unit
)
{

    var proceed : ()-> Unit = {}
    private  lateinit var  request: ActivityResultLauncher<Array<String>>
    fun registerActivityForResult(act : ComponentActivity)
    {
        request = act.registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions() ){map->
            val partition = map.entries.partition {
                it.value
            }
            if(partition.second.isEmpty())
            {
                proceed()
            }
            else
            {
                retry( partition.second.map { it.key }.toTypedArray() )
            }
        }
    }

    private fun retry(newPermission:Array<String>)
    {
        request.launch(newPermission)
    }

    fun request()
    {
        val partition = permissions.partition{checkPermission(it)}
        if(partition.second.isEmpty())
        {
            proceed()
        }
        else
        {
            val rationale = partition.second.partition{shouldShowRequestPermissionRationale(it)}
            if(rationale.second.isEmpty())
            {
                request.launch(partition.second.toTypedArray())
            }
            else
            {
                showUI(rationale.second.toTypedArray()){retry(it)}
            }
        }
    }
}