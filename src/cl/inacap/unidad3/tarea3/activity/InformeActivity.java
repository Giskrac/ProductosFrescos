package cl.inacap.unidad3.tarea3.activity;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import cl.inacap.unidad3.tarea3.clases.Cliente;
import cl.inacap.unidad3.tarea3.clases.Pedido;
import cl.inacap.unidad3.tarea3.clases.Producto;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

public class InformeActivity extends Activity {
	
	//se crean las variables necesarias para obtener tanto los resultados finales como los registros
	private ArrayAdapter<String> adapter;
	private int entregas = 0;
	private int pedidos = 0;
	private int saldo = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informe); 
		
		//obtenemos el simbolo de moneda local
		Currency curr = Currency.getInstance(Locale.getDefault());
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
		nf.setCurrency(curr);
		
		//instanciamos el actionbar para controlar sus eventos y agregar opciones como en un menu
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFF64c8ff)); 
		
		//habilitamos como opcion de menu el titulo del actionbar (home)
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE); 
		int id_usuario = prefs.getInt("id_usuario", 0);
		
		Pedido pedido = new Pedido();		
		ArrayList<Pedido> informeDePedidos = pedido.InformeDePedidos(getApplicationContext(), id_usuario);
		
		//se crean los elementos que contiene el layout para mostrar los resultados del informe
		ListView lv_informe= (ListView)findViewById(R.id.lv_informe);
		TextView txt_saldo= (TextView)findViewById(R.id.lb_saldo_dig);
		TextView txt_total_pedido= (TextView)findViewById(R.id.lb_total_pedido_dig);
		TextView txt_total_entregas= (TextView)findViewById(R.id.lb_total_entregas_dig);
		
		//se crea el arraylist que contendra los registro del informe de pedido
		ArrayList<String> informeArreglo = new ArrayList<String>();
		
		//contamos los elementos del ArrayList PedidoGeneral
		int count = informeDePedidos.size();
		
		//creamos el objeto calendar para crear la fecha
		Calendar date = Calendar.getInstance();
		
		// se recorre los elementos de PedidoGeneral
		for(int i=0; i < count; i++)
		{				
			// se obtiene el subtotal sumando la cantidad y el precio de un pedido en especifico
			int subTotal = informeDePedidos.get(i).cantidad * informeDePedidos.get(i).precio;			
			
			//obtenemos la fecha en un arreglo
			String[] fecha_array = informeDePedidos.get(i).fecha_entrega.split("-");
			
			//asignamos los valores a la fecha			
			date.set(Calendar.YEAR, Integer.parseInt(fecha_array[2]));
			date.set(Calendar.MONTH, Integer.parseInt(fecha_array[1]));
			date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fecha_array[0]));

			//obtenemos el formato de la fecha actual
	        Format format = android.text.format.DateFormat.getDateFormat(this);
	        
	        //aplicamos ese formato a la fecha
	        String fecha = format.format(date.getTime());
	 			
			//se agrega el string para mostrar el registro en el listview
			informeArreglo.add(fecha + " " + informeDePedidos.get(i).nombre_cliente + "\n"
								+ informeDePedidos.get(i).nombre_producto + "\n"
								+ informeDePedidos.get(i).cantidad + "\t X \t" + informeDePedidos.get(i).precio + "\t = \t" + subTotal);			
			
			//se obtiene los datos necesario para los totales finales
			saldo = saldo + subTotal;
			pedidos++;
			entregas = entregas + informeDePedidos.get(i).cantidad;			
			
		}
				
		//se muestran los totales finales
		txt_saldo.setText(nf.format(saldo));//aplicamos el formato de la moneda actual
		txt_total_pedido.setText(String.valueOf(pedidos));
		txt_total_entregas.setText(String.valueOf(entregas));
 
        //se agregan los registros obtenidos en el ciclo for anteros al adapter
		adapter = new ArrayAdapter<String>(this, R.layout.item_informe, informeArreglo);
		
		//se agrega el adapter
        lv_informe.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

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
