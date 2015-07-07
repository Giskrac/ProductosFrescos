package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import cl.inacap.unidad3.tarea3.activity.R;

public class Cliente {
	public int id_cliente;
	public String nombre_cliente;
	public String direccion_cliente;
	public String telefono_cliente;
	public int id_usuario;
	
	//Se genera y obtiene la lista de clientes como base de datos de todos los clientes
	private ArrayList<Cliente> ListarClientes(Context mContext)	{	
		
		ArrayList<Cliente> lista = new ArrayList<Cliente>();
		
		String clientes[] = mContext.getResources().getStringArray(R.array.clientes_array);
		
		int cantidad = clientes.length;
		
		for(int i =0; i < cantidad; i++)
		{
			
			try {
				
				JSONObject jsonClientes = new JSONObject(clientes[i]);
				
				Cliente cliente = new Cliente();
				cliente.id_cliente = jsonClientes.getInt("id_cliente");
				cliente.nombre_cliente = jsonClientes.getString("nombre_cliente");
				cliente.direccion_cliente = jsonClientes.getString("direccion_cliente");
				cliente.telefono_cliente = jsonClientes.getString("telefono_cliente");
				cliente.id_usuario = jsonClientes.getInt("id_usuario");
				
				lista.add(cliente);
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			
			
		}
		
		return lista;

	}
	
	
	//Entrega los clientes asignados a un usuario y visibles o activos
	public ArrayList<Cliente> ListarMisClientes(Context mContext, int id_usuario)
	{
		ArrayList<Cliente> lista = ListarClientes(mContext);
		
		//se crea variable para recojer mis clientes
		ArrayList<Cliente> misClientes = new ArrayList<Cliente>();
		
		//se calcula la cantidad de objetos en la lista para no hacerlo en cada ciclo del FOR
		int totalClientes = lista.size();
		
		//se Busca a los clientes asignados al usuario actual
		for(int i = 0; i < totalClientes ; i++)
		{
			//si el cliente es el asignado al usuario se agrega a la lista misClientes
			if(lista.get(i).id_usuario == id_usuario){
				misClientes.add(lista.get(i));
			}
		}
		
		return misClientes;
		
	}

	//forma String de la clase para general el listview
	public String toString()
	{
		return this.nombre_cliente + "\n" + this.direccion_cliente + "\n" + this.telefono_cliente; 
	}
	
	public Cliente MostrarCliente(ArrayList<Cliente> lista, int index)
	{
		return lista.get(index);
	}
	
	public ArrayList<Cliente> agregarCliente(ArrayList<Cliente> lista, Cliente cliente)
	{
		lista.add(cliente);
		
		return lista;
	}
	
	public ArrayList<Cliente> borrarCliente(ArrayList<Cliente> lista, int index)
	{
		lista.remove(index);
		
		return lista;
	}	
	
	public ArrayList<Cliente> actualizarCliente(ArrayList<Cliente> lista, Cliente cliente, int index)
	{
		lista.set(index, cliente);
		
		return lista;
	}

	
}
