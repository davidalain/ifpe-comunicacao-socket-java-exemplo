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
	private Map<Conexao,Personagem> mapaPersonagens;
	private Map<Conexao,Integer> mapaJogadas;

	public DadosJogo(EstadoJogo estado) {
		super();
		
		this.estado = estado;
		
		this.clientes = new ArrayList<>(Config.QUANTIDADE_JOGADORES);
		
		this.mapaPersonagens = new HashMap<>();
		this.mapaJogadas = new HashMap<>();
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

	public Map<Conexao, Personagem> getMapaPersonagens() {
		return mapaPersonagens;
	}

	public void setMapaPersonagens(Map<Conexao, Personagem> mapaPersonagensJogadores) {
		this.mapaPersonagens = mapaPersonagensJogadores;
	}
	
	public Map<Conexao, Integer> getMapaJogadas() {
		return mapaJogadas;
	}

	public void setMapaJogadas(Map<Conexao, Integer> mapaJogadasJogadores) {
		this.mapaJogadas = mapaJogadasJogadores;
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
		return (mapaPersonagens.size() == Config.QUANTIDADE_JOGADORES);
	}

	/**
	 * Verifica se todos os jogadores já fizeram suas jogadas
	 * @return
	 */
	public boolean todasJogadasRealizadas() {
		return (mapaJogadas.size() == Config.QUANTIDADE_JOGADORES); 
	}


}
