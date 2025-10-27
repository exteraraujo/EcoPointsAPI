package br.com.ecopoints.api.model;

public enum TipoAcao {
	RECICLAR_LIXO(10),
	USAR_TRANSPORTE_PUBLICOS(5),
	USAR_BICICLETA(15),
	ECONOMIZAR_AGUA(8),
	PLANTAR_ARVORE(25);
	
	private final int pontos;
	
	TipoAcao(int pontos) {
		this.pontos = pontos;
	}
    
	public int getPontos() {
		return pontos;
	}
}
