package cl.inacap.unidad3.tarea3.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cl.inacap.unidad3.tarea3.clases.Cliente;
import cl.inacap.unidad3.tarea3.clases.Pedido;
import cl.inacap.unidad3.tarea3.clases.Producto;
import android.R.menu;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

public class PedidoActivity extends Activity {	
	
	private String TAG = "PedidoActivity";
	
	private ArrayAdapter<Pedido> pedidosAdapter;
	private Pedido pedido;
	private ListView lv_pedidos;
	
	private int id_cliente;
	private int id_usuario;
	private String nombre_cliente;
	
	private ArrayAdapter<Producto> adapterSpinner;
	private ArrayList<Producto> arrayProductos;		
	
	public ArrayList<Pedido> misPedidos;
	private Producto mProducto;
	
	private int idProductoSeleccionado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedido);
		
		//instanciamos el actionbar para controlar sus eventos y agregar opciones como en un menu
		ActionBar actionBar = getActionBar();	
		
		//cambiamos el color de fondo del actionbar
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFF64c8ff)); 
		
		//habilitamos como opcion de menu el titulo del actionbar (home)
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		//optenemos el id del usuario desde las preferencias
		SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE); 
		id_usuario = prefs.getInt("id_usuario", 0);	
		
		Bundle bundle = getIntent().getExtras();
		
		id_cliente = bundle.getInt("id_cliente");
		nombre_cliente = bundle.getString("nombre_cliente");
		
		actionBar.setTitle("Pedido: " + nombre_cliente);
		
		mProducto = new Producto();
		arrayProductos = mProducto.listaProductos(this);
		
		// Reservamos el adapter para la lista de productos
		adapterSpinner = new ArrayAdapter<Producto>(this, R.layout.item_producto, arrayProductos);
		
		ImageButton botonNuevoPedido = (ImageButton) findViewById(R.id.btn_pedido_crear_nuevo);		
		botonNuevoPedido.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditDialog("crear", 0);				
			}
		});
		
		lv_pedidos = (ListView) findViewById(R.id.lv_pedidos);
		
		pedido = new Pedido(getApplicationContext(), id_cliente, id_usuario);		
		misPedidos = pedido.ListarPedidos();	
		
		updateListView(misPedidos);
			
	}
	
	private void updateListView(final ArrayList<Pedido> lista)
	{
		
		misPedidos = lista;
		
		//limpiamos el adapter
		if(pedidosAdapter != null){
			pedidosAdapter.clear();
			lv_pedidos.setAdapter(null);
			pedidosAdapter.notifyDataSetChanged();
		}
		
		pedidosAdapter = new ArrayAdapter<Pedido>(this, R.layout.item_pedido, misPedidos);
		
		//Agregamos el adapter al listView
		lv_pedidos.setAdapter(pedidosAdapter);
		pedidosAdapter.notifyDataSetChanged();
		
		//creamos el listener que quedara escuchando si se selecciona un pedido en la lista
		lv_pedidos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
				EditDialog("editar", position);								
			}
			
		});
		
	}
	
	public void EditDialog(String accion, final int index){
		
	    final Dialog dialog = new Dialog(this);
	    	    
		dialog.setContentView(R.layout.dialog_modificar_pedido);
		
		final Spinner productos    = (Spinner) dialog.findViewById(R.id.sp_lista_productos);
		final EditText cantidad = (EditText) dialog.findViewById(R.id.txt_pedido_cantidad);
		final EditText precio  = (EditText) dialog.findViewById(R.id.txt_pedido_precio);
		
		Button botonEliminar = (Button) dialog.findViewById(R.id.btn_pedido_eliminar);
		Button botonCrear = (Button) dialog.findViewById(R.id.btn_pedido_crear);
		Button botonActualizar = (Button) dialog.findViewById(R.id.btn_pedido_actualizar);
		Button botonCancelar = (Button) dialog.findViewById(R.id.btn_pedido_cancelar);
		
		productos.setAdapter(adapterSpinner);
		
		// se selecciona el producto del spinner
		productos.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v, int position, long arg3) {
				//al seleccionar un producto del spinner mostramos su precio
				precio.setText("" + arrayProductos.get(position).precio_producto);				
				idProductoSeleccionado = arrayProductos.get(position).id_producto;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
			
		});
		
				
		//si la accion es aditar debemos llenar los campos con los datos actuales
		if(accion.equals("editar"))
		{
			
			dialog.setTitle(R.string.dialog_titulo_pedido_editar);
			
			String nombre = misPedidos.get(index).nombre_producto;				

			int cant = productos.getCount();
			
			for(int i=0; i < cant; i++)
			{	
				
				if(productos.getItemAtPosition(i).toString().equals(nombre.toString()))
				{
					productos.setSelection(i);
					break;
				}
			}
		
		
			cantidad.setText(String.valueOf(misPedidos.get(index).cantidad));
			precio.setText(String.valueOf(misPedidos.get(index).precio));
			
			botonCrear.setVisibility(View.GONE);			
			
		}
		else
		{
			dialog.setTitle(R.string.dialog_titulo_pedido_crear);
			
			botonActualizar.setVisibility(View.GONE);
			botonEliminar.setVisibility(View.GONE);
		}
		
		// si el boton e cliqueado eliminaremos al ocultaremos el pedido
		botonEliminar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				//Borramos el pedido seleccionado
				updateListView(pedido.borrarPedido(misPedidos.get(index).id_pedido));
				dialog.dismiss();
			}
		});			
		
		// si el boton e cliqueado crearemos un nuevo pedido
		botonCrear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//obtenemos la fecha actual
				Date currentLocalTime = Calendar.getInstance().getTime();
				SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyy");   
				String localTime = date.format(currentLocalTime);
				
				//se crea un nuevo objeto pedido para agregarlo al registro
				Pedido mPedido = new Pedido();
				
				mPedido.id_cliente = id_cliente;
				mPedido.id_usuario = id_usuario;
				mPedido.id_producto = idProductoSeleccionado;
				mPedido.fecha_entrega = localTime;
				mPedido.cantidad = Integer.parseInt(cantidad.getText().toString());
				
				//se agrega el nuevo pedido
				updateListView(pedido.agregarPedido(mPedido));	
				dialog.dismiss();
			}
		});		
		
		// si el boton e cliqueado actualizaremos al pedido actual
		botonActualizar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//se obtienes la fecha actual
				Date currentLocalTime = Calendar.getInstance().getTime();
				SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyy");   
				String localTime = date.format(currentLocalTime);
				
				//se crea un nuevo objeto pedido para remplazar al registro existente
				Pedido mPedido = new Pedido();
				
				mPedido.id_producto = idProductoSeleccionado;
				mPedido.fecha_entrega = localTime;
				mPedido.cantidad = Integer.parseInt(cantidad.getText().toString());	
				
				//se actualiza el pedido actual
				updateListView(pedido.actualizarPedido(mPedido, misPedidos.get(index).id_pedido));
				dialog.dismiss();
			}
		});
		
		// si el boton e cliqueado actualizaremos al cliente actual
		botonCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
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
				
				ArrayList<Pedido> tempArrayList = new ArrayList<Pedido>();
				ArrayList<Pedido> requestArrayList = new ArrayList<Pedido>();
				tempArrayList =  pedido.ListarPedidos();				
					
				for(Pedido c: tempArrayList){
					
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
