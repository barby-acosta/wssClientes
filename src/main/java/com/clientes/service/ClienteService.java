package com.clientes.service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.clientes.model.Cliente;
import com.clientes.model.ClienteFB;
import com.clientes.model.Kpideclientes;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class ClienteService {
	public static final String COL_NAME = "clientes";
	private final int expectativaVida = 75;

	public String saveClienteDetails(ClienteFB cliente) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document().set(cliente);
		return collectionsApiFuture.get().getUpdateTime().toString();
	}

	public ArrayList<Cliente> getClientes() throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		Cliente cliente = new Cliente();

		for (DocumentReference element : documentReference) {
			ApiFuture<DocumentSnapshot> future = element.get();
			DocumentSnapshot document = future.get();

			if (document.exists()) {
				cliente = document.toObject(Cliente.class);
				String[] parts = cliente.getFechaNacimiento().split("-");

				String anio = String.valueOf(Integer.valueOf(parts[0]) + this.expectativaVida);
				cliente.setFechaMuerte(anio + "-" + parts[1] + "-" + parts[2]);
				clientes.add(cliente);
			}
		}

		return clientes;
	}

	public Kpideclientes getKpideclientes() throws InterruptedException, ExecutionException {
		Kpideclientes kpi = new Kpideclientes();
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		clientes = this.getClientes();
		int cant = clientes.size();

		double sum = 0.0, variacion = 0.0, prom = 0.0;

		for (Cliente cliente : clientes) {
			sum = sum + cliente.getEdad();
		}
		prom = sum / cant;

		for (Cliente cliente : clientes) {
			variacion += Math.pow(cliente.getEdad() - prom, 2);
		}

		kpi.setDesviacion(Math.sqrt(variacion / cant));
		kpi.setPromedio(prom);
		return kpi;
	}

}
