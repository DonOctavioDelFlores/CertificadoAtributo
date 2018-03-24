package certificadoatributo;

/**
 * Entrada do cmd do Emissor
 * 
 * o programa deve ser executado passando um xml com as configs de parametro, ex:
 * 
 * java -jar CertAtt.jar -config "c:\\docs\\config.xml" > output.log
 * 
 * o output.log pode ser verificado depois pela aplicacao que chamou para verificar se
 * existram erros ou se tudo deu certo.
 * 
 * Exemplo de Xml para config:
 * 
 * <xml>
 * <config>
 * 	<titular>
 * 		<nome>Nome Aluno</nome>
 * 		<datanasc>2000-02-26</datanasc>
 * 		<cpf>00713879987</cpf>
 * 		<rg>72371909</rg>
 * 		<rgexpuf>ssppr</rgexpuf>
 * 		<matricula>9873129739812</matricula>
 * 		<dtinicialatt>2018-02-26</dtinicialatt>
 * 		<dtfinalatt>2019-02-26</dtfinalatt>
 * 	</titular>
 * 	<instituicao>
 * 		<nome>escola teste</nome>
 * 		<curso>curso teste</curso>
 * 		<escolaridade>ensino medio</escolaridade>
 * 		<municipio>municipio</municipio>
 * 		<uf>pr</uf>
 * 	</instituicao>
 * 	<pathsaida>c:\\karamazov\\docs\\</pathsaida>
 * 	<arquivosaida>certificadoaluno.cer</arquivosaida>
 * 	<certificado>090f8d9405a738a54828ff991b2edda3</certificado>
 * 	<urlcadeiacerts>https://www.upes.org.br/certemissor.cer</urlcadeiacerts>
 * 	<urllcr>https://www.upes.org.br/Rev.crl</urllcr>
 * </ * config>
 * </xml>
 * 
 */
public class Main
{
	/**
	 * @args contem os parametros passados no cmd 
	 */
	public static void main(String args[])    
	{    	    	    	
		try {															
			new CertAtributoBuilder(new Config(args)).build();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);						
		}
	}    	            
}