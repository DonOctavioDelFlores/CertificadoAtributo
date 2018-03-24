/**
 * 
 */
package certificadoatributo;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Carrega todas as variáves do Xml e distribui por gets
 */
public class Config {
	private String args[];
	private Document xml;

	public Config(String args[]) throws SAXException, IOException, ParserConfigurationException {
		this.args = args;

		xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(this.getParamByName("-config")));				
	}

	/**
	 * a busca é pelo nome, mas sempre retorna i + 1 para trazer o valor
	 */
	public String getParamByName(String value) {

		String result = "";
		for (int i = 0; i <= (args.length - 1); i++) {

			if (value.toLowerCase().equals(args[i].toLowerCase())) {
				if ((args.length -1) >= (i + 1)) {
					result = args[i + 1];
					break;
				}				
			}
		}

		return result;
	}

	public Titular getTitular() {

		Titular titular = new Titular();

		Element elTitular = (Element)xml.getElementsByTagName("titular").item(0);

		titular.setNome(elTitular.getElementsByTagName("nome").item(0).getTextContent());
		titular.setDataNasc(Date.valueOf(elTitular.getElementsByTagName("datanasc").item(0).getTextContent()));
		titular.setCpf(elTitular.getElementsByTagName("cpf").item(0).getTextContent());
		titular.setRg(elTitular.getElementsByTagName("rg").item(0).getTextContent());
		titular.setRgExpUf(elTitular.getElementsByTagName("rgexpuf").item(0).getTextContent());
		titular.setMatricula(elTitular.getElementsByTagName("matricula").item(0).getTextContent());
		titular.setDtInicialAtt(Date.valueOf(elTitular.getElementsByTagName("dtinicialatt").item(0).getTextContent()));
		titular.setDtFinalAtt(Date.valueOf(elTitular.getElementsByTagName("dtfinalatt").item(0).getTextContent()));

		return titular;
	}

	public Instituicao getInstituicao() {

		Instituicao instituicao = new Instituicao();

		Element elInstituicao = (Element)xml.getElementsByTagName("instituicao").item(0);

		instituicao.setNome(elInstituicao.getElementsByTagName("nome").item(0).getTextContent());
		instituicao.setNomeCurso(elInstituicao.getElementsByTagName("curso").item(0).getTextContent());
		instituicao.setGrauEscolaridade(elInstituicao.getElementsByTagName("escolaridade").item(0).getTextContent());
		instituicao.setMunicipio(elInstituicao.getElementsByTagName("municipio").item(0).getTextContent());
		instituicao.setUf(elInstituicao.getElementsByTagName("uf").item(0).getTextContent());

		return instituicao;
	}

	public Emissor getEmissor() throws Exception {		
		try {			 			
			return new Emissor(xml.getElementsByTagName("certificado").item(0).getTextContent());
			
		} catch (Exception e) {
			throw e; 			
		}
	}

	public String getPathSaida() { 
		return xml.getElementsByTagName("pathsaida").item(0).getTextContent();
	}

	public String getArquivoSaida() {
		return xml.getElementsByTagName("arquivosaida").item(0).getTextContent();
	}

	public String getUrlCadeiaCerts() {
		return xml.getElementsByTagName("urlcadeiacerts").item(0).getTextContent();
	}

	public String getUrlLcr() {
		return xml.getElementsByTagName("urllcr").item(0).getTextContent();
	}	
}
