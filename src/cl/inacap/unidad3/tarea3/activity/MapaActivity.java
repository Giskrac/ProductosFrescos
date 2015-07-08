package cl.inacap.unidad3.tarea3.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import cl.inacap.unidad3.tarea3.clases.Cliente;
import cl.inacap.unidad3.tarea3.clases.Pedido;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {
	
	private int id_usuario;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        
        //instanciamos el actionbar para controlar sus eventos y agregar opciones como en un menu
  		ActionBar actionBar = getActionBar();
  		actionBar.setBackgroundDrawable(new ColorDrawable(0xFF64c8ff)); 
  		
  		//habilitamos como opcion de menu el titulo del actionbar (home)
  		actionBar.setDisplayHomeAsUpEnabled(true);
  		actionBar.setHomeButtonEnabled(true);
  		
  		SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE); 
		id_usuario = prefs.getInt("id_usuario", 0);	
        
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.rutas); 
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
    	
    	Cliente cliente = new Cliente();
    	ArrayList<Cliente> clientes = cliente.MisClientes(this, id_usuario);
    	
    	Geocoder geocoder = new Geocoder(this);  
    	List<Address> addresses;
    	double latitude = 0;
    	double longitude = 0;
    	try {
    		
    		int cant = clientes.size();
    		
    		for(int i=0; i < cant; i++) 
    		{

    			addresses = geocoder.getFromLocationName(clientes.get(i).direccion_cliente, 1);
    			if(addresses.size() > 0) {
    	    	    latitude = addresses.get(0).getLatitude();
    	    	    longitude = addresses.get(0).getLongitude();
    	    	    LatLng posicion = new LatLng(latitude, longitude); 	
    	    	    map.addMarker(new MarkerOptions().position(posicion).title(clientes.get(i).nombre_cliente));
    	    	    
    	    	    if(i == 0)
    	    	    	map.moveCamera(CameraUpdateFactory.newLatLng(posicion));
    	            
    	    	}

    		}
    		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        map.moveCamera(CameraUpdateFactory.zoomTo(10));
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_informe, menu);
	    return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // recuperamos el menu seleccionado desde el actionbar
	    switch (item.getItemId()) {
		    case android.R.id.home: //opcion home
	    		onBackPressed(); //volvemos
	    		return true;
	        case R.id.action_search: //opcion buscar
	            //openSearch(); 
	            return true;
	        case R.id.action_pedido: //opcion pedidos
	        	
	        	Intent intent = new Intent(this, InformeActivity.class);
				startActivity(intent);
				
	            return true;
	        case R.id.rutas: //opcion rutas
	        	
	        	Intent intentRutas = new Intent(this, MapaActivity.class);
				startActivity(intentRutas);
				
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}