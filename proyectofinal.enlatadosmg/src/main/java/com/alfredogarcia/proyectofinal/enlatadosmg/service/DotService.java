package com.alfredogarcia.proyectofinal.enlatadosmg.service;

import com.alfredogarcia.proyectofinal.enlatadosmg.entity.*;
import com.alfredogarcia.proyectofinal.enlatadosmg.structures.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DotService {

    public String generarDotUsuarios(List<Usuario> lista) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=record, color=\"#2196F3\", style=filled, fillcolor=\"#E3F2FD\", fontname=\"Helvetica\"];\n");
        dot.append("  label=\"Lista Enlazada de Usuarios\";\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontsize=20;\n\n");

        if (lista == null || lista.isEmpty()) {
            dot.append("  vacio [label=\"Lista Vacía\", shape=none, fillcolor=white];\n");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Usuario u = lista.get(i);
                dot.append("  u").append(u.getId()).append(" [label=\"{ID: ").append(u.getId())
                        .append(" | Nombre: ").append(u.getNombre()).append(" ").append(u.getApellidos())
                        .append("}\"];\n");
            }
            for (int i = 0; i < lista.size() - 1; i++) {
                dot.append("  u").append(lista.get(i).getId()).append(" -> u").append(lista.get(i + 1).getId()).append(";\n");
            }
        }
        dot.append("}\n");
        return dot.toString();
    }

    public String generarDotAlmacen(List<Caja> lista) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("  node [shape=box, color=\"#FF5722\", style=filled, fillcolor=\"#FBE9E7\", fontname=\"Helvetica\"];\n");
        dot.append("  label=\"Pila del Almacén (LIFO) - Cima en la parte superior\";\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontsize=20;\n\n");

        if (lista == null || lista.isEmpty()) {
            dot.append("  vacio [label=\"Almacén Vacío\", shape=none, fillcolor=white];\n");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Caja c = lista.get(i);
                dot.append("  c").append(c.getCorrelativo()).append(" [label=\"Caja #").append(c.getCorrelativo())
                        .append("\\nIngreso: ").append(c.getFechaIngreso()).append("\"];\n");
            }
            for (int i = 0; i < lista.size() - 1; i++) {
                dot.append("  c").append(lista.get(i).getCorrelativo()).append(" -> c").append(lista.get(i + 1).getCorrelativo())
                        .append(" [label=\"abajo\"];\n");
            }
        }
        dot.append("}\n");
        return dot.toString();
    }

    public String generarDotClientes(ArbolAVLClientes arbol) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("  node [shape=circle, color=\"#9C27B0\", style=filled, fillcolor=\"#F3E5F5\", fontname=\"Helvetica\"];\n");
        dot.append("  label=\"Árbol AVL de Clientes\";\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontsize=20;\n\n");

        if (arbol == null || arbol.estaVacio()) {
            dot.append("  vacio [label=\"Árbol Vacío\", shape=none, fillcolor=white];\n");
        } else {
            generarDotAVLRecursivo(arbol.obtenerRaiz(), dot);
        }
        dot.append("}\n");
        return dot.toString();
    }

    private void generarDotAVLRecursivo(NodoArbolAVL nodo, StringBuilder dot) {
        if (nodo == null) return;
        Cliente c = nodo.obtenerCliente();
        dot.append("  c").append(c.getDpi()).append(" [label=\"").append(c.getNombre()).append(" ")
                .append(c.getApellidos()).append("\\nDPI: ").append(c.getDpi()).append("\\nAlt: ").append(nodo.obtenerAltura())
                .append("\"];\n");

        if (nodo.obtenerIzquierdo() != null) {
            dot.append("  c").append(c.getDpi()).append(" -> c").append(nodo.obtenerIzquierdo().obtenerCliente().getDpi())
                    .append(" [label=\"izq\"];\n");
            generarDotAVLRecursivo(nodo.obtenerIzquierdo(), dot);
        } else {
            // Nodo nulo para balance visual (opcional, pero ayuda)
            dot.append("  izq_null_").append(c.getDpi()).append(" [shape=point, style=invis];\n");
            dot.append("  c").append(c.getDpi()).append(" -> izq_null_").append(c.getDpi()).append(" [style=invis];\n");
        }

        if (nodo.obtenerDerecho() != null) {
            dot.append("  c").append(c.getDpi()).append(" -> c").append(nodo.obtenerDerecho().obtenerCliente().getDpi())
                    .append(" [label=\"der\"];\n");
            generarDotAVLRecursivo(nodo.obtenerDerecho(), dot);
        } else {
            dot.append("  der_null_").append(c.getDpi()).append(" [shape=point, style=invis];\n");
            dot.append("  c").append(c.getDpi()).append(" -> der_null_").append(c.getDpi()).append(" [style=invis];\n");
        }
    }

    public String generarDotRepartidores(List<Repartidor> lista) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=record, color=\"#2196F3\", style=filled, fillcolor=\"#E3F2FD\", fontname=\"Helvetica\"];\n");
        dot.append("  label=\"Cola de Repartidores (FIFO) - Frente a la izquierda\";\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontsize=20;\n\n");

        if (lista == null || lista.isEmpty()) {
            dot.append("  vacio [label=\"Cola Vacía\", shape=none, fillcolor=white];\n");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Repartidor r = lista.get(i);
                dot.append("  r").append(r.getDpi()).append(" [label=\"{DPI: ").append(r.getDpi())
                        .append(" | Nombre: ").append(r.getNombre()).append(" ").append(r.getApellidos())
                        .append(" | Lic: ").append(r.getLicencia()).append("}\"];\n");
            }
            for (int i = 0; i < lista.size() - 1; i++) {
                dot.append("  r").append(lista.get(i).getDpi()).append(" -> r").append(lista.get(i + 1).getDpi()).append(";\n");
            }
        }
        dot.append("}\n");
        return dot.toString();
    }

    public String generarDotVehiculos(List<Vehiculo> lista) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=record, color=\"#00BCD4\", style=filled, fillcolor=\"#E0F7FA\", fontname=\"Helvetica\"];\n");
        dot.append("  label=\"Cola de Vehículos (FIFO) - Frente a la izquierda\";\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontsize=20;\n\n");

        if (lista == null || lista.isEmpty()) {
            dot.append("  vacio [label=\"Cola Vacía\", shape=none, fillcolor=white];\n");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Vehiculo v = lista.get(i);
                dot.append("  v").append(v.getPlaca()).append(" [label=\"{Placa: ").append(v.getPlaca())
                        .append(" | Marca: ").append(v.getMarca()).append(" ").append(v.getModelo())
                        .append(" | Trans: ").append(v.getTipoTransmision()).append("}\"];\n");
            }
            for (int i = 0; i < lista.size() - 1; i++) {
                dot.append("  v").append(lista.get(i).getPlaca()).append(" -> v").append(lista.get(i + 1).getPlaca()).append(";\n");
            }
        }
        dot.append("}\n");
        return dot.toString();
    }

    public String generarDotPedidos(List<Pedido> lista) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=record, color=\"#4CAF50\", style=filled, fillcolor=\"#E8F5E9\", fontname=\"Helvetica\"];\n");
        dot.append("  label=\"Lista Enlazada de Pedidos\";\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontsize=20;\n\n");

        if (lista == null || lista.isEmpty()) {
            dot.append("  vacio [label=\"Lista de Pedidos Vacía\", shape=none, fillcolor=white];\n");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Pedido p = lista.get(i);
                String colorEstado = p.getEstado() == EstadoPedido.PENDIENTE ? "#FFF9C4" : "#C8E6C9"; // Amarillo vs Verde claro
                dot.append("  p").append(p.getNumeroPedido()).append(" [fillcolor=\"").append(colorEstado)
                        .append("\", label=\"{Pedido #").append(p.getNumeroPedido())
                        .append(" | Cliente: ").append(p.getCliente().getNombre()).append(" ")
                        .append(p.getCliente().getApellidos())
                        .append(" | Origen: ").append(p.getDepartamentoOrigen())
                        .append(" | Destino: ").append(p.getDepartamentoDestino())
                        .append(" | Repartidor: ").append(p.getRepartidor().getNombre())
                        .append(" | Vehículo: ").append(p.getVehiculo().getPlaca())
                        .append(" | Cajas: ").append(p.getNumeroCajas())
                        .append(" | Estado: ").append(p.getEstado().name())
                        .append("}\"];\n");
            }
            for (int i = 0; i < lista.size() - 1; i++) {
                dot.append("  p").append(lista.get(i).getNumeroPedido()).append(" -> p").append(lista.get(i + 1).getNumeroPedido()).append(";\n");
            }
        }
        dot.append("}\n");
        return dot.toString();
    }

    public String generarDotCajasPedido(List<Caja> lista, int numeroPedido) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=record, color=\"#795548\", style=filled, fillcolor=\"#EFEBE9\", fontname=\"Helvetica\"];\n");
        dot.append("  label=\"Cajas asignadas al Pedido #").append(numeroPedido).append("\";\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontsize=20;\n\n");

        if (lista == null || lista.isEmpty()) {
            dot.append("  vacio [label=\"Sin cajas\", shape=none, fillcolor=white];\n");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                Caja c = lista.get(i);
                dot.append("  c").append(c.getCorrelativo()).append(" [label=\"{Correlativo: ").append(c.getCorrelativo())
                        .append(" | Ingreso: ").append(c.getFechaIngreso()).append("}\"];\n");
            }
            for (int i = 0; i < lista.size() - 1; i++) {
                dot.append("  c").append(lista.get(i).getCorrelativo()).append(" -> c").append(lista.get(i + 1).getCorrelativo()).append(";\n");
            }
        }
        dot.append("}\n");
        return dot.toString();
    }
}
