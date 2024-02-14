package com.campusland.views;

import java.time.LocalDateTime;
import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.Cliente;
import com.campusland.respository.models.Factura;
import com.campusland.respository.models.ItemFactura;
import com.campusland.respository.models.Producto;

public class ViewFactura extends ViewMain {

    public static void startMenu() throws FacturaExceptionInsertDataBase {

        int op = 0;

        do {

            op = mostrarMenu();
            switch (op) {
                case 1:
                    crearFactura();
                    break;
                case 2:
                    listarFactura();
                    break;
                case 3:
                    generarDIAN();
                    break;
                case 4:
                    informeTotal();
                    break;
                case 5:
                    clientesCompras();
                    break;
                case 6:
                    productoMasVendido();
                    break;
                default:
                    System.out.println("Salida");
                    break;
            }

        } while (op >= 1 && op < 7);

    }

    public static void generarDIAN(){
        System.out.println("\nGENERACIÓN ARCHIVO DIAN\n");
        double sum_com = 0, sum_imp = 0;
        for (Factura factura : serviceFactura.listar()) {
            if (factura.getImpuestoFactura() != 0) {
                System.out.println("Nombre: "+ factura.getCliente().getApellido() + "\tN° Documento: "+ factura.getCliente().getNombre() +"\tN° Telefono: "+ factura.getCliente().getDocumento() +"\tN° Factura: "+ factura.getNumeroFactura() + "\tFecha: " + factura.getFecha() + " \tTotal compra: $" + factura.getTotalFactura() + " \tImpuesto: $" + factura.getImpuestoFactura());
            }
            sum_com = sum_com + factura.getTotalFactura();
            sum_imp = sum_imp + factura.getImpuestoFactura();
        }
        System.out.print("\n");
        System.out.println("Total compras: $" + sum_com);
        System.out.println("Total impuestos: $" + sum_imp);
        System.out.print("\nIngrese cualquier tecla para continuar --> ");
        leer.next();
        System.out.println("\n");
    }

    public static void informeTotal(){
        System.out.println("\nGENERACIÓN INFORME TOTAL\n");
        double sum_com = 0, sum_imp = 0, sum_des = 0;
        for (Factura factura : serviceFactura.listar()) {
            sum_com = sum_com + factura.getTotalFactura();
            sum_imp = sum_imp + factura.getImpuestoFactura();
            sum_des = sum_des + factura.getDescuentos(factura.getTotalFactura());
        }
        System.out.println("Total compras:      $" + sum_com);
        System.out.println("Total impuestos:    $" + sum_imp);
        System.out.println("Total descuentos:   $" + sum_des);
        System.out.print("\nIngrese cualquier tecla para continuar --> ");
        leer.next();
        System.out.println("\n");
    }

    public static void clientesCompras(){
        System.out.println("\nLISTADO DESCENDENTE CLIENTES POR COMPRAS\n");

        for (Cliente cliente : serviceCliente.listar()){
            System.out.println("Cliente: " + cliente.getFullName());
            int cont_compras = 0;
            for (Factura factura : serviceFactura.listar()){
                if (cliente.getId() == factura.getCliente().getId()) {
                    cont_compras++;
                }
            }
            System.out.println("Numero de compras: "+ cont_compras + "\n");
        }
        System.out.print("\nIngrese cualquier tecla para continuar --> ");
        leer.next();
        System.out.println("\n");
    }

    public static void productoMasVendido(){
        System.out.println("\nLISTADO DESCENDENTE PRODUCTO MAS VENDIDO\n");

        for (Producto producto : serviceProducto.listar()){
            System.out.println("Producto: " + producto.getNombre());
            int cont_compras = 0;
            for (Factura factura : serviceFactura.listar()){
                for (ItemFactura itemFactura : factura.getItems()) {
                    if (producto.getCodigo() == itemFactura.getProducto().getCodigo()) {
                        cont_compras = itemFactura.getCantidad();
                    }
                }

            }
            System.out.println("Numero de compras: "+ cont_compras + "\n");
        }
        System.out.print("\nIngrese cualquier tecla para continuar --> ");
        leer.next();
        System.out.println("\n");
    }

    public static int mostrarMenu() {
        System.out.println("----Menu--Factura----");
        System.out.println("1. Crear factura.");
        System.out.println("2. Listar factura.");
        System.out.println("3. Generar archivo DIAN por año");
        System.out.println("4. Informe total de ventas, descuento e impuestos");
        System.out.println("5. Listado descendiente clientes por compras");
        System.out.println("6. Listado descendiente producto mas vendido");
        System.out.println("7. Salir ");
        return leer.nextInt();
    }

    public static void listarFactura() {
        System.out.println("Lista de Facturas");
        for (Factura factura : serviceFactura.listar()) {
            factura.display();
            System.out.println();
        }
    }

    public static void crearFactura() {
        System.out.println("-- Creación de Factura ---");

        Cliente cliente;
        do {
            cliente = ViewCliente.buscarGetCliente();
        } while (cliente == null);

        Factura factura = new Factura(LocalDateTime.now(), cliente);
        System.out.println("-- Se creó la factura -----------------");
        System.out.println("-- Seleccione los productos a comprar por código");
     

        do {
            Producto producto = ViewProducto.buscarGetProducto();

            if (producto != null) {
                System.out.print("Cantidad: ");
                int cantidad = leer.nextInt();
                ItemFactura item = new ItemFactura(cantidad, producto);
                factura.agregarItem(item);

                System.out.println("Agregar otro producto: si o no");
                String otroItem = leer.next();
                if (!otroItem.equalsIgnoreCase("si")) {
                    break;
                }
            }

        } while (true);
        

        try {
            serviceFactura.crear(factura);
            System.out.println("Se creó la factura");
            factura.display();
        } catch (FacturaExceptionInsertDataBase e) {
            System.out.println(e.getMessage());
        }
    }

}
