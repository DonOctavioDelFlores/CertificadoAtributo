/**
 * 
 */
package certificadoatributo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Rodrigo
 *
 */
public class Titular {
	private String nome;
	private Date dataNasc;
	private String cpf;
	private String matricula;
	private String rg;
	private String rgExpUf;
	private Date dtInicialAtt;
	private Date dtFinalAtt;
	
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
	 * @return the dataNasc
	 */
	public Date getDataNasc() {
		return dataNasc;
	}
	/**
	 * @param dataNasc the dataNasc to set
	 */
	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}
	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}
	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}
	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	/**
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}
	/**
	 * @param rg the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}
	/**
	 * @return the rgExpUf
	 */
	public String getRgExpUf() {
		return rgExpUf;
	}
	/**
	 * @param rgExpUf the rgExpUf to set
	 */
	public void setRgExpUf(String rgExpUf) {
		this.rgExpUf = rgExpUf;
	}
	/**
	 * @return the dtInicialAtt
	 */
	public Date getDtInicialAtt() {
		return dtInicialAtt;
	}
	/**
	 * @param dtInicialAtt the dtInicialAtt to set
	 */
	public void setDtInicialAtt(Date dtInicialAtt) {
		this.dtInicialAtt = dtInicialAtt;
	}
	/**
	 * @return the dtFinalAtt
	 */
	public Date getDtFinalAtt() {
		return dtFinalAtt;
	}
	/**
	 * @param dtFinalAtt the dtFinalAtt to set
	 */
	public void setDtFinalAtt(Date dtFinalAtt) {
		this.dtFinalAtt = dtFinalAtt;
	}
	
	/**
	 *   nas primeiras 8 (oito) posições, a data de nascimento do titular, no formato ddmmaaaa; nas 11 (onze) posições subsequentes, 
	 *   o Cadastro de Pessoa Física (CPF) do titular; nas 15 (quinze) posições subsequentes, o número do Registro Geral - RG do titular
	 *   do atributo; nas 10 (de z) posições subsequentes, as siglas do órgão expedidor do RG e respectiva UF, nas 15 (quinze) posições 
	 *   subsequentes, o número da matrícula do estudante.

	 */
	public String formatDadosTitular() {
		
		return new SimpleDateFormat("ddmmyyyy").format(this.getDataNasc()) +		
				StringUtils.leftPad(this.getCpf(), 11, "0") +
				StringUtils.leftPad(this.getRg(), 15, "0") +
				StringUtils.rightPad(this.getRgExpUf(), 10, " ") +
				StringUtils.leftPad(this.getMatricula(), 15, " ");		
	}

}
