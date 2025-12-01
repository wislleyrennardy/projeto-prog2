package main;

import java.util.Scanner;

public class Projeto {
	private Scanner sc;
	
	public Projeto () {
		this.sc = new Scanner(System.in);
	}
	
	public void inicia() {
		System.out.println("Seu nome?");
		String texto = sc.nextLine();
		System.out.printf("Bem vindo %s!", texto);
	}
	
	public static void main(String[] args) {
		Projeto projeto = new Projeto();
		projeto.inicia();

	}

}
