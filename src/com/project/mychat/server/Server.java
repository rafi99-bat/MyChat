package com.project.mychat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
	
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private List<Integer> clientResponce = new ArrayList<Integer>();
	
	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	private Thread run, manage, send, receive;
	
	private final int MAX_ATTEMPTS = 5;
	
	public Server(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();
	}
	
	public void run() {
		running = true;
		System.out.println("Server started on port " + port);
		manageClients();
		receive();
	}
	
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					sendToAll("/i/server");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int i = 0; i < clients.size(); i++) {
						ServerClient c = clients.get(i);
						if (!clientResponce.contains(clients.get(i).getID())) {
							if (c.attempt >= MAX_ATTEMPTS) {
								disconnect(c.getID(), false);
							} else {
								c.attempt++;
							}
						} else {
							Integer ID = Integer.valueOf(c.getID());
							clientResponce.remove(ID);
							c.attempt = 0;
						}
					}
					//System.out.println(clients.size());
				}
			}
		};
		manage.start();
	}
	
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
						packet.getAddress();
						packet.getPort();
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}
	
	private void sendToAll(String message) {
		for(int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
		
	}
	
	private void send(String message, InetAddress address, int port) {
		message += "/e/";
		send(message.getBytes(), address, port);
	}
	
	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		if(string.startsWith("/c/")) {
			// UUID id = UUID.randomUUID();
			// int id = new SecureRandom().nextInt();
			int id = UniqueIdentifier.getIdentifier();
			System.out.println("Identifier : " + id);
			clients.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
			System.out.println(string.substring(3, string.length()));
			String ID = "/c/" + id;
			send(ID, packet.getAddress(), packet.getPort());
		} else if (string.startsWith("/m/")){
			sendToAll(string);
		} else if (string.startsWith("/d/")) {
			String id = string.split("/d/|/e/")[1];
			disconnect( Integer.parseInt(id), true);
		} else if (string.startsWith("/i/")) {
			clientResponce.add(Integer.parseInt(string.split("/i/|/e/")[1]));
		} else {
			System.out.println(string);
		}
	}
	
	private void disconnect(int id, boolean status) {
		ServerClient c = null;
		for (int i = 0; i < clients.size(); i++) {
			if(clients.get(i).getID() == id) {
				c = clients.get(i);
				clients.remove(i);
				break;
			}
		}
		String message = "";
		if (status) {
			message = "Client " + c.name.trim() + " (" + c.getID() + ") @ " + c.address.toString() + " : " + c.port + " disconnected.";
		} else {
			message = "Client " + c.name.trim() + " (" + c.getID() + ") @ " + c.address.toString() + " : " + c.port + " timed out.";
		}
		System.out.println(message);
	}
	
	
}
