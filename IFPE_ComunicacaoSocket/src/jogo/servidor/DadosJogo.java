package jogo.servidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jogo.Conexao;
import jogo.EstadoJogo;
import jogo.Personagem;
import threaded.Config;

public class DadosJogo {
	
	private List<Conexao> clientes;
	private EstadoJogo estado;
	private Map<Conexao,Personagem> mapaPersonagensJogadores;
	private Map<Conexao,Integer> mapaJogadasJogadores;

	public DadosJogo(EstadoJogo estado) {
		super();
		
		this.estado = estado;
		
		this.clientes = new ArrayList<>(Config.QUANTIDADE_JOGADORES);
		
		this.mapaPersonagensJogadores = new HashMap<>();
		this.mapaJogadasJogadores = new HashMap<>();
	}
	
	public List<Conexao> getClientes() {
		return clientes;
	}

	public void setClientes(List<Conexao> clientes) {
		this.clientes = clientes;
	}
	
	public EstadoJogo getEstado() {
		return estado;
	}

	public void setEstado(EstadoJogo estado) {
		this.estado = estado;
	}

	public Map<Conexao, Personagem> getMapaPersonagensJogadores() {
		return mapaPersonagensJogadores;
	}

	public void setMapaPersonagensJogadores(Map<Conexao, Personagem> mapaPersonagensJogadores) {
		this.mapaPersonagensJogadores = mapaPersonagensJogadores;
	}
	
	public Map<Conexao, Integer> getMapaJogadasJogadores() {
		return mapaJogadasJogadores;
	}

	public void setMapaJogadasJogadores(Map<Conexao, Integer> mapaJogadasJogadores) {
		this.mapaJogadasJogadores = mapaJogadasJogadores;
	}
	
	/**
	 * Verifica se todos os clientes já se conectaram
	 * @return
	 */
	public boolean todosClientesConectados() {
		return (this.clientes.size() == Config.QUANTIDADE_JOGADORES);
	}

	/**
	 * Verifica se todos os persongens já foram escolhidos pelos seus jogadores
	 * @return
	 */
	public boolean todosPersonsagensEscolhidos() {
		return (mapaPersonagensJogadores.size() == Config.QUANTIDADE_JOGADORES);
	}

	/**
	 * Verifica se todos os jogadores já fizeram suas jogadas
	 * @return
	 */
	public boolean todasJogadasRealizadas() {
		return (mapaJogadasJogadores.size() == Config.QUANTIDADE_JOGADORES); 
	}


}
