package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import android.util.Log;
import cl.inacap.unidad3.tarea3.activity.ClientesActivity;


public class Pedido {

	public int id_pedido;
	public int id_cliente;
	public String nombre_cliente_pedido;
	public int id_producto_pedido;
	public String producto_pedido;
	public String fecha_entrega_pedido;
	public int cantidad_pedido;	
	public int precio_pedido;	
	public boolean visible;
		
	//Entrega los Pedidos asignados a un cliente
	public ArrayList<Pedido> misPedidos(ArrayList<Pedido> lista, int id_cliente)
	{
		//se crea variable para recojer mis Pedidos
		ArrayList<Pedido> misPedidos = new ArrayList<Pedido>();
		
		//se calcula la cantidad de objetos en la lista para no hacerlo en cada ciclo del FOR
		int totalPedidos = lista.size();
		
		//se Busca los Pedidos asignados al cliente actual
		for(int i = 0; i < totalPedidos ; i++)
		{
			//si el pedido corresponde al cliente se agrega a la lista misPedidos
			if(lista.get(i).id_cliente == id_cliente && lista.get(i).visible == true){
				
				Pedido nuevoPedido = new Pedido();
				
				nuevoPedido.id_pedido = i;
				nuevoPedido.id_cliente = lista.get(i).id_cliente;
				nuevoPedido.nombre_cliente_pedido = lista.get(i).nombre_cliente_pedido;
				nuevoPedido.id_producto_pedido = lista.get(i).id_producto_pedido;
				nuevoPedido.producto_pedido = lista.get(i).producto_pedido;
				nuevoPedido.fecha_entrega_pedido = lista.get(i).fecha_entrega_pedido;
				nuevoPedido.cantidad_pedido = lista.get(i).cantidad_pedido;	
				nuevoPedido.precio_pedido = lista.get(i).precio_pedido;
				nuevoPedido.visible = lista.get(i).visible;
				
				misPedidos.add(lista.get(i));
			}
		}
		
		return misPedidos;
		
	}
	
	//entrega todos los pedidos
	public ArrayList<Pedido> TodosPedidos()	{	
		return ClientesActivity.PedidosGeneral;		
	}

	//forma String de la clase para general el listview
	public String toString()
	{
		return this.producto_pedido + "\n" + this.cantidad_pedido + " x " + this.precio_pedido + "      " + (this.precio_pedido * this.cantidad_pedido); 
	}
	
	//muestra el pedido actual seleccionado
	public Pedido MostrarPedido(ArrayList<Pedido> lista, int index)
	{
		return lista.get(index);
	}
	
	//agrega un pedido al pedido general y al actual
	public ArrayList<Pedido> agregarPedido(ArrayList<Pedido> lista, Pedido cliente, int id_cliente)
	{
		ClientesActivity.PedidosGeneral.add(cliente);
		
		int id_pedido = ClientesActivity.PedidosGeneral.size() - 1;
		
		Log.d("agregarPedido", "ID_PEDIDO:" + id_pedido);
		
		cliente.id_pedido = id_pedido;
		
		lista.add(cliente);
		
		return lista;
	}
	
	// oculta el pedido en la lista general y lo elimina de la actual
	public ArrayList<Pedido> borrarPedido(ArrayList<Pedido> lista, int id_pedido, int index)
	{
		
		ClientesActivity.PedidosGeneral.get(id_pedido).visible = false;
		lista.remove(index);
		
		return lista;
	}	
	
	// actualiza el pedido del pedido general y del actual
	public ArrayList<Pedido> actualizarPedido(ArrayList<Pedido> lista, Pedido cliente, int id_pedido, int index)
	{
		
		ClientesActivity.PedidosGeneral.set(id_pedido, cliente);
		lista.set(index, cliente);
		
		return lista;
	}

	
}
