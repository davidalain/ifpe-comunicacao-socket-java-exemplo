package jogo.mensagem;

import java.io.Serializable;

import jogo.Personagem;

public class Mensagem implements Serializable{

	/**
	 *	@serial 
	 */
	private static final long serialVersionUID = -3056642974461498964L;

	public static final int MSG_SIZE = 3;

	/**
	 * Campos do pacote
	 */
	private int[] campos;

	/**
	 * Único construtor privado para não poder ser instanciado fora desta classe.
	 * 
	 * Utilizando o padrão Fabric que constrói os objetos através dos métodos estáticos criaMensagem...
	 */
	//	private Mensagem() {
	//		this.campos = int byte[MSG_SIZE];
	//	}

	public Mensagem(int[] campos) {
		this.campos = campos;
	}

	/**
	 * get retornando array com propriedade final para que não seja modificado
	 * 
	 * @return
	 */
	public int[] getCampos() {
		return this.campos;
	}

	/**
	 * Intencionalmente não tem o método set pq depois que uma mensagem é criada ela não pode ser modificada.
	 * @param campos
	 */
	public void setCampos(int[] campos) {
		this.campos = campos;
	}

	public String toString() {

		TipoMensagem tipo = TipoMensagem.values()[this.campos[0]];

		String str = "[" + tipo + ", ";

		switch (tipo) {
		case MSG_JOGADA:
		case MSG_TABULEIRO:
		default:
			str  += this.campos[1];
			break;
		case MSG_ESCOLHA_PERSONAGEM:
		case MSG_VENCEDOR:
			str  += Personagem.values()[this.campos[1]];
			break;
		case MSG_ERRO:
			str  += TipoErro.values()[this.campos[1]];
			break;
		}

		str += "]";

		return str;
	}

	public boolean isMensagemJogada() {
		return this.campos[0] == TipoMensagem.MSG_JOGADA.ordinal();
	}

	public boolean isMensagemEscolhaPersonagem() {
		return this.campos[0] == TipoMensagem.MSG_ESCOLHA_PERSONAGEM.ordinal();
	}

	public boolean isMensagemTabuleiro() {
		return this.campos[0] == TipoMensagem.MSG_TABULEIRO.ordinal();
	}

	public boolean isMensagemVencedor() {
		return this.campos[0] == TipoMensagem.MSG_VENCEDOR.ordinal();
	}

	public boolean isMensagemErro() {
		return this.campos[0] == TipoMensagem.MSG_ERRO.ordinal();
	}

	public Personagem getCampoPersonagem() {
		if(!isMensagemEscolhaPersonagem() && !isMensagemVencedor() && !isMensagemJogada()) 
			return null;

		return Personagem.values()[this.campos[1]];
	}

	public TipoErro getCampoTipoErro() {
		if(!isMensagemErro()) 
			return null;

		return TipoErro.values()[this.campos[1]];
	}

	public int getCampoJogada() {
		return this.campos[2];
	}

	public int getCampoTabuleiro() {
		return this.campos[1];
	}


}
