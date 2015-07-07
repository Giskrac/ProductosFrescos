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
	
	private ArrayAdapter<Pedido> pedidoAdapter;
	private Pedido pedido;
	private ListView lv_pedidos;
	
	private int id_cliente;
	private String nombre_cliente;
	
	private ArrayAdapter<Producto> adapterSpinner;
	private ArrayList<Producto> arrayProductos;		
	
	public ArrayList<Pedido> misPedidos;

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
		
		Bundle bundle = getIntent().getExtras();
		
		id_cliente = bundle.getInt("id_cliente");
		nombre_cliente = bundle.getString("nombre_cliente");
		
		Producto producto = new Producto();
		arrayProductos = producto.listaProductos(this);
		
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
		
		pedido = new Pedido();		
		misPedidos = pedido.misPedidos(ClientesActivity.PedidosGeneral, id_cliente);	

		//Creamos un adapter con un layout propio para personalizar el color a la letra
		pedidoAdapter = new ArrayAdapter<Pedido>(this, R.layout.item_pedido, misPedidos);
		
		//Agregamos el adapter al listView
		lv_pedidos.setAdapter(pedidoAdapter);
		
		//creamos el listener que quedara escuchando si se selecciona un pedido en la lista
		lv_pedidos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				EditDialog("editar", position);				
				
			}
			
		});
		
		pedidoAdapter.notifyDataSetChanged();
		
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
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
			
		});
		
		//si la accion es aditar debemos llenar los campos con los datos actuales
		if(accion.equals("editar"))
		{
			dialog.setTitle(R.string.dialog_titulo_pedido_editar);
			
			productos.setSelection(misPedidos.get(index).id_producto_pedido);			
			cantidad.setText("" + misPedidos.get(index).cantidad_pedido);
			precio.setText("" + misPedidos.get(index).precio_pedido);
			
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
				misPedidos = pedido.borrarPedido(misPedidos, misPedidos.get(index).id_pedido, index);
				
				pedidoAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});			
		
		// si el boton e cliqueado crearemos un nuevo pedido
		botonCrear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//obtenemos la fecha actual
				Date currentLocalTime = Calendar.getInstance().getTime();
				SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm");   
				String localTime = date.format(currentLocalTime);
				
				//se crea un nuevo objeto pedido para agregarlo al registro
				Pedido nuevoPedido = new Pedido();
				
				nuevoPedido.id_pedido = 0;
				nuevoPedido.id_cliente = id_cliente;
				nuevoPedido.nombre_cliente_pedido = nombre_cliente;
				nuevoPedido.id_producto_pedido = productos.getSelectedItemPosition();
				nuevoPedido.producto_pedido = productos.getSelectedItem().toString();
				nuevoPedido.fecha_entrega_pedido = localTime;
				nuevoPedido.cantidad_pedido = Integer.parseInt(cantidad.getText().toString());	
				nuevoPedido.precio_pedido = Integer.parseInt(precio.getText().toString());
				nuevoPedido.visible = true;
				
				//se agrega el nuevo pedido
				misPedidos = pedido.agregarPedido(misPedidos, nuevoPedido, id_cliente);	

				pedidoAdapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});		
		
		// si el boton e cliqueado actualizaremos al pedido actual
		botonActualizar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//se obtienes la fecha actual
				Date currentLocalTime = Calendar.getInstance().getTime();
				SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm");   
				String localTime = date.format(currentLocalTime);
				
				//se crea un nuevo objeto pedido para remplazar al registro existente
				Pedido nuevoPedido = new Pedido();
				
				nuevoPedido.id_pedido = misPedidos.get(index).id_pedido;
				nuevoPedido.id_cliente = id_cliente;
				nuevoPedido.nombre_cliente_pedido = nombre_cliente;
				nuevoPedido.id_producto_pedido = productos.getSelectedItemPosition();
				nuevoPedido.producto_pedido = productos.getSelectedItem().toString();
				nuevoPedido.fecha_entrega_pedido = localTime;
				nuevoPedido.cantidad_pedido = Integer.parseInt(cantidad.getText().toString());	
				nuevoPedido.precio_pedido = Integer.parseInt(precio.getText().toString());
				nuevoPedido.visible = true;
				
				//se actualiza el pedido actual
				misPedidos = pedido.actualizarPedido(misPedidos, nuevoPedido, misPedidos.get(index).id_pedido, index);
				
				pedidoAdapter.notifyDataSetChanged();
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
				tempArrayList =  pedido.misPedidos(ClientesActivity.PedidosGeneral, id_cliente);				
				misPedidos.clear();
					
				for(Pedido c: tempArrayList){
					
					Log.d("SEARCH", "BUSCANDO: " + c.toString());
					
					if (c.toString().toLowerCase().contains(query.toString().toLowerCase())) {
						misPedidos.add(c);
					}
					
				}						
				
				pedidoAdapter.notifyDataSetChanged();

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
