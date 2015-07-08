package cl.inacap.unidad3.tarea3.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cl.inacap.unidad3.tarea3.clases.Cliente;
import cl.inacap.unidad3.tarea3.clases.Posicion;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class ClientesActivity extends Activity implements LocationListener {
	
	private String TAG = "ClientesActivity";
	
	private static final long TIEMPO_MIN = 1000 * 60 * 5;
	private static final long DISTANCIA_MIN = 5 ;
	
	private LocationManager manejador;
	private String proveedor;
	
	private ArrayAdapter<Cliente> adapter;
	private Cliente cliente;
	private ListView lv_clientes;
	
	private int id_usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientes);
		
		//instanciamos el actionbar para controlar sus eventos y agregar opciones como en un menu
		ActionBar actionBar = getActionBar();		
		
		//cambiamos el color de fondo del actionbar
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFF64c8ff)); 		
		
		//desabilitamos el boton home del actionbar
		actionBar.setDisplayHomeAsUpEnabled(false);
		
		//optenemos el id del usuario desde las preferencias
		SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE); 
		id_usuario = prefs.getInt("id_usuario", 0);		
			
		lv_clientes = (ListView) findViewById(R.id.lv_clientes);
		
		//Instanciamos la clase usuario para llamar al  
		//metodo listarClientes() para llevarlos al adapter
		cliente = new Cliente();

		//Actualizamos el listview
		updateListView(cliente.MisClientes(this, id_usuario));
		
		ImageButton botonNuevoCliente = (ImageButton) findViewById(R.id.btn_cliente_crear_nuevo);		
		botonNuevoCliente.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditDialog("crear", 0);				
			}
		});
		
		
		// TODO Auto-generated method stub
		manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
	    Criteria criterio = new Criteria();
	    criterio.setCostAllowed(false);
	    criterio.setAltitudeRequired(false);
	    criterio.setAccuracy(Criteria.ACCURACY_FINE);
	    proveedor = manejador.getBestProvider(criterio, true);
	}
	
	private void updateListView(final ArrayList<Cliente> lista)
	{
		
		//limpiamos el adapter
		if(adapter != null){
			adapter.clear();
			lv_clientes.setAdapter(null);
			adapter.notifyDataSetChanged();
		}
			
		//Creamos un adapter con un layout propio para cambiar el color a la letra
		adapter = new ArrayAdapter<Cliente>(this, R.layout.item_cliente, lista);

		//Agregamos el adapter al listView		
		lv_clientes.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		//creamos el Listener para escuchar el click largo para abrir la ventana de modificacion
		lv_clientes.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				//llamamos al metodo que nos desplegara la ventana de modificacion
				EditDialog("editar", lista.get(position).id_cliente);
				return false;
			}
		});
		
		//creamos el listener que quedara escuchando si se selecciona un cliente en la lista
		lv_clientes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Intent intent = new Intent(ClientesActivity.this, PedidoActivity.class);
	    		intent.putExtra("id_cliente", lista.get(position).id_cliente);
	    		intent.putExtra("id_usuario", id_usuario);
	    		intent.putExtra("nombre_cliente", lista.get(position).nombre_cliente);
	    		ClientesActivity.this.startActivity(intent); 
				
			}
			
		});	
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//creamos el listener para la obtencion de la localizacion cada 5 minutos
		manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
		Log.d(TAG, "Localización desconocida");
	}

	
	private void registrarDireccion(Location localizacion) {
        if (localizacion == null)
        {
        	Log.d(TAG, "Localización desconocida");
        }
        else
        {
        	
        	try {
        		//llamamos asincronicamente para llenar los datos de la localizacion
				new WSBackgroundLocation().execute(localizacion).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
        	
	}
	
	public void EditDialog(String accion, final int index){
		
	    final Dialog dialog = new Dialog(this);	    
	    
		dialog.setContentView(R.layout.dialog_modificar_clientes);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		
		lv_clientes.setEnabled(false);
		
		final EditText nombre    = (EditText) dialog.findViewById(R.id.txt_nombre_cliente);
		final EditText direccion = (EditText) dialog.findViewById(R.id.txt_direccion_cliente);
		final EditText telefono  = (EditText) dialog.findViewById(R.id.txt_telefono_cliente);
		
		Button botonEliminar = (Button) dialog.findViewById(R.id.btn_cliente_eliminar);
		Button botonCrear = (Button) dialog.findViewById(R.id.btn_cliente_crear);
		Button botonActualizar = (Button) dialog.findViewById(R.id.btn_cliente_actualizar);
		Button botonCancelar = (Button) dialog.findViewById(R.id.btn_cliente_cancelar);
		
		//si la accion es aditar debemos llenar los campos con los datos actuales
		if(accion.equals("editar"))
		{
			Cliente clienteActual = cliente.MostrarCliente(index);
			dialog.setTitle(R.string.dialog_titulo_cliente_editar);
			
			nombre.setText(clienteActual.nombre_cliente);
			direccion.setText(clienteActual.direccion_cliente);
			telefono.setText(clienteActual.telefono_cliente);
			
			botonCrear.setVisibility(View.GONE);			
			
		}
		else
		{
			dialog.setTitle(R.string.dialog_titulo_cliente_crear);
			
			botonActualizar.setVisibility(View.GONE);
			botonEliminar.setVisibility(View.GONE);
		}
		
		// si el boton e cliqueado eliminaremos al ocultaremos al cliente
		botonEliminar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Borramos el cliente seleccionado
				updateListView(cliente.borrarCliente(index));
				lv_clientes.setEnabled(true);
				dialog.dismiss();
			}
		});			
		
		// si el boton e cliqueado crearemos un nuevo cliente
		botonCrear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Cliente nuevoCliente = new Cliente();
				nuevoCliente.nombre_cliente 	= nombre.getText().toString();
				nuevoCliente.direccion_cliente 	= direccion.getText().toString();
				nuevoCliente.telefono_cliente 	= telefono.getText().toString();
				nuevoCliente.id_usuario 		= id_usuario;

				updateListView(cliente.agregarCliente(nuevoCliente));
				lv_clientes.setEnabled(true);
				dialog.dismiss();
			}
		});		
		
		// si el boton e cliqueado actualizaremos al cliente actual
		botonActualizar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Cliente nuevoCliente = new Cliente();
				nuevoCliente.nombre_cliente 	= nombre.getText().toString();
				nuevoCliente.direccion_cliente 	= direccion.getText().toString();
				nuevoCliente.telefono_cliente 	= telefono.getText().toString();
				
				updateListView(cliente.actualizarCliente(nuevoCliente, index));
				lv_clientes.setEnabled(true);
				dialog.dismiss();
			}
		});
		
		// si el boton e cliqueado actualizaremos al cliente actual
		botonCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lv_clientes.setEnabled(true);
				dialog.dismiss();
			}
		});
		
		//se despliega el popup con el formulario
		dialog.show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_menu, menu);
	    
	    //creamos los componentes para crear el buscador
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        
        //seleccionamos el icono u opcion del menu asignado tambien al actionbar
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        
        // creamos el listener para recibir los datos a buscar
        search.setOnQueryTextListener(new OnQueryTextListener() { 

        	// creamos el codigo para hacer la busqueda al momento de escribir en el cuadro de busqueda
            @Override 
            public boolean onQueryTextChange(String query) {
				
				Log.d("SEARCH", query);
				
				ArrayList<Cliente> tempArrayList = new ArrayList<Cliente>();
				ArrayList<Cliente> requestArrayList = new ArrayList<Cliente>();
				tempArrayList = cliente.MisClientes(getApplicationContext(), id_usuario);					
					
				for(Cliente c: tempArrayList){
					
					Log.d("SEARCH", "BUSCANDO: " + c.toString());
					
					if (c.toString().toLowerCase().contains(query.toString().toLowerCase())) {
						requestArrayList.add(c);
					}
					
				}						
				
				updateListView(requestArrayList);

				return true; 
            }

			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;					
			} 

        });

	    
	    return true;
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

	@Override
	public void onLocationChanged(Location location) {
		registrarDireccion(location);
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	public class WSBackgroundLocation extends AsyncTask<Location, String, String> {
		
		// obtenemos los datos de localizacion necesarios
		@Override
		protected String doInBackground(Location... params) {
			
			Location loc = params[0];
			
			try {
				Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
				List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
				
				if (!list.isEmpty()) {
					Address address = list.get(0);		
					Log.d(TAG, address.getAddressLine(0));
					
					Posicion posicion = new Posicion();
		        	
					posicion.id_usuario = id_usuario;
					posicion.longitud = loc.getLongitude(); 
					posicion.latitud = loc.getLatitude(); 
					posicion.direccion = address.getAddressLine(0);
					
					posicion.addPosition(getApplicationContext(), posicion);

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
	        
			return null;
		}
		
		
	}
	

}
