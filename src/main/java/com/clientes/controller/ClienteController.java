package com.clientes.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clientes.model.Cliente;
import com.clientes.model.ClienteFB;
import com.clientes.model.Kpideclientes;
import com.clientes.service.ClienteService;

@CrossOrigin
@RestController
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@GetMapping("/listclientes")
	public ArrayList<Cliente> getCliente() throws InterruptedException, ExecutionException {
		return clienteService.getClientes();
	}
	
	@GetMapping("/kpideclientes")
	public Kpideclientes kpideclientes() throws InterruptedException, ExecutionException {
		return clienteService.getKpideclientes();
	}

	@PostMapping("/creacliente")
	public String createCliente(@RequestBody ClienteFB cliente) throws InterruptedException, ExecutionException {
		return clienteService.saveClienteDetails(cliente);
	}
}
