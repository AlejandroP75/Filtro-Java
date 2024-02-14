package com.campusland.respository.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.campusland.utils.Formato;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.campusland.respository.impl.implfactura.RepositoryFacturaJsonImpl;
import com.campusland.respository.impl.implfactura.RepositoryFacturaMysqlImpl;
import com.campusland.respository.models.Factura;
import com.campusland.services.ServiceFactura;
import com.campusland.services.impl.ServiceFacturaImpl;

import lombok.Data;

@Data
public class Factura {

    public static final ServiceFactura serviceFactura = new ServiceFacturaImpl(new RepositoryFacturaMysqlImpl(),new RepositoryFacturaJsonImpl());
    private int numeroFactura;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")    
    private LocalDateTime fecha;
    private Cliente cliente;
    private List<ItemFactura> items;
    private static int nextNumeroFactura;

    public Factura(){

    }

    public Factura(int numeroFactura, LocalDateTime fecha, Cliente cliente) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
    }

    public Factura(LocalDateTime fecha, Cliente cliente) {
        this.numeroFactura = ++nextNumeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
    }

    public double getTotalFactura() {
        double totalFactura = 0;
        for (ItemFactura item : items) {
            totalFactura += item.getImporte();
        }
        return totalFactura;
    }

    public double getImpuestoFactura(){
        double impuesto = 0.19;
        double totalFactura = getTotalFactura();
        double impuesto_pagar = totalFactura * impuesto;

        return impuesto_pagar;
    }

    public void agregarItem(ItemFactura item){
        this.items.add(item);
    }

    public double getDescuento1(double total_factura){
        double des = 0;
        if (total_factura > 999) {
            des = total_factura * 0.1;
        }

        return des;
    }

    public double getDescuento2(){
        double des = 0;
        for (ItemFactura item : this.items) {
            if(item.getProducto().getCodigo() == 5){
                if (item.getCantidad() >= 5) {
                    des = 5;
                }
            }

        }
        return des;
    }

    public double getDescuento3(Cliente c, double total_factura){
        double des = 0;
        int cont_facturas = 0;
        for (Factura factura : serviceFactura.listar()) {
            if (factura.getCliente().getId() == c.getId()) {
                cont_facturas++;
            }
        }

        if (cont_facturas > 10) {
            des = total_factura * 0.15;
        }

        return des;
    }

    public double getDescuento4(LocalDateTime fecha){
        double des = 0;
        LocalDateTime viernes = LocalDateTime.parse("2024-02-16T08:22");

        if (fecha.getDayOfWeek() == viernes.getDayOfWeek()) {
            des = 3;
        }
        return des;
    }

    public double getDescuento5(LocalDateTime fecha, double total_factura){
        double des = 0;
        
        LocalDateTime temporada = LocalDateTime.parse("2024-12-01T08:22");

        if (fecha.getMonth() == temporada.getMonth()) {
            des = total_factura * 0.05;
        }

        return des;
    }

    public double getDescuentos(double total_factura){
        double sum_des = 0;

        sum_des = this.getDescuento1(this.getTotalFactura()) + getDescuento2() + getDescuento3(this.cliente, this.getTotalFactura()) + getDescuento4(this.getFecha()) + getDescuento5(this.getFecha(), this.getTotalFactura());

        if (sum_des > total_factura) {
            sum_des = total_factura;
        }

        return sum_des;
    }

    public void display() {
        System.out.println();
        System.out.println("Factura: " + this.numeroFactura);
        System.out.println("Cliente: " + this.cliente.getFullName());
        System.out.println("Fecha: " + Formato.formatoFechaHora(this.getFecha()));
        System.out.println("-----------Detalle Factura----------------------");
        for (ItemFactura item : this.items) {
            System.out.println(item.getProducto().getCodigoNombre() + " " + item.getCantidad() + " " + Formato.formatoMonedaPesos(item.getImporte()));

        }
        System.out.println();
        System.out.println("Total productos                     " + Formato.formatoMonedaPesos(this.getTotalFactura()));
        System.out.println("IVA                                 " + Formato.formatoMonedaPesos(this.getImpuestoFactura()));
        System.out.println("Descuento monto minimo             -" + Formato.formatoMonedaPesos(this.getDescuento1(getTotalFactura())));
        System.out.println("Descuento al menos 5\n" + 
                           "unidades del producto x            -" + Formato.formatoMonedaPesos(this.getDescuento2()));
        System.out.println("Descuento cliente gold             -" + Formato.formatoMonedaPesos(this.getDescuento3(this.cliente, this.getTotalFactura())));
        System.out.println("Descuento viernes                  -" + Formato.formatoMonedaPesos(this.getDescuento4(this.getFecha())));
        System.out.println("Descuento temporada navideña       -" + Formato.formatoMonedaPesos(this.getDescuento5(this.getFecha(), this.getTotalFactura())));
        System.out.println("Total a pagar                       " + Formato.formatoMonedaPesos(this.getTotalFactura() - this.getDescuentos(this.getTotalFactura()) + this.getImpuestoFactura()));
        System.out.println();
    }

}
