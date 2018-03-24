/**
 * 
 */
package certificadoatributo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Rodrigo
 *
 */
public class Instituicao {
	private String nome;
	private String grauEscolaridade;
	private String nomeCurso;
	private String municipio;
	private String uf;
	
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the grauEscolaridade
	 */
	public String getGrauEscolaridade() {
		return grauEscolaridade;
	}
	/**
	 * @param grauEscolaridade the grauEscolaridade to set
	 */
	public void setGrauEscolaridade(String grauEscolaridade) {
		this.grauEscolaridade = grauEscolaridade;
	}
	/**
	 * @return the nomeCurso
	 */
	public String getNomeCurso() {
		return nomeCurso;
	}
	/**
	 * @param nomeCurso the nomeCurso to set
	 */
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}
	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}
	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	/**
	 * nas primeiras 40 (quarenta) posi��es, o nome da institui��o de ensino; nas 15 (quinze) posi��es subsequentes, o grau de 
	 * escolaridade; nas 30 (trinta) posi��es subsequentes, o nome do curso, nas 20 (vinte) posi��es subsequentes, o munic�pio da 
	 * institui��o e nas 2 (duas) posi��es subsequentes, a UF do munic�pio. 
	 */
	public String formatDadosInstituicao() {
		return  StringUtils.rightPad(this.getNome(), 40, " ") +
				StringUtils.rightPad(this.getGrauEscolaridade(), 15, " ") +
				StringUtils.rightPad(this.getNomeCurso(), 30, " ") +
				StringUtils.rightPad(this.getMunicipio(), 20, " ") +
				StringUtils.rightPad(this.getUf(), 2, " ");
				
	}
}
