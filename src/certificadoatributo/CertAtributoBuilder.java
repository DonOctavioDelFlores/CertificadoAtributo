package certificadoatributo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateEncodingException;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.AttributeCertificateHolder;
import org.bouncycastle.cert.AttributeCertificateIssuer;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v2AttributeCertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;

/**
 * Classe principal que cria o certificado
 *
 */
public class CertAtributoBuilder {

	private String urlCadeiaCerts;
	private String urlLcr;
	private String pathSaida;
	private String arquivoSaida;
	private Emissor emissor;
	private Titular titular;
	private Instituicao instituicao;
	private X509v2AttributeCertificateBuilder certAttBuilder;

	public CertAtributoBuilder(Config config) throws Exception {

		Security.addProvider(new sun.security.mscapi.SunMSCAPI());

		try {
			this.emissor = config.getEmissor();		
			this.titular = config.getTitular();
			this.instituicao = config.getInstituicao();		
			this.setPathSaida(config.getPathSaida());

			this.setArquivoSaida(config.getArquivoSaida());
			this.urlCadeiaCerts = config.getUrlCadeiaCerts();
			this.urlLcr = config.getUrlLcr();

		} catch (Exception e) {			
			throw e;
		}
	}

	public void build() throws Exception {	

		this.montaCertificadoAtt();						
		this.addAtributos();		
		this.addExtensoes();		
		this.finalizaCertificadoAtt();
	}

	private void montaCertificadoAtt() throws CertificateEncodingException {

		RDN cnEmissor = new JcaX509CertificateHolder(emissor.getCertificado()).getSubject().getRDNs(BCStyle.CN)[0];		 

		X500NameBuilder holder = new X500NameBuilder(X500Name.getDefaultStyle());

		holder.addRDN(BCStyle.CN, titular.getNome());
		holder.addRDN(BCStyle.O, "ICP-Brasil");
		holder.addRDN(BCStyle.OU, cnEmissor.getFirst().getValue().toString());
		holder.addRDN(BCStyle.C, "BR");				

		X500NameBuilder issuer = new X500NameBuilder(X500Name.getDefaultStyle());

		issuer.addRDN(BCStyle.O, instituicao.getNome());
		issuer.addRDN(BCStyle.CN, instituicao.getNome());	

		// Instantiate a new AC generator
		certAttBuilder = new X509v2AttributeCertificateBuilder(
				new AttributeCertificateHolder(holder.build()),
				new AttributeCertificateIssuer(issuer.build()),
				BigInteger.probablePrime(120, new SecureRandom()),
				titular.getDtInicialAtt(), 
				titular.getDtFinalAtt()
				);  
	}

	private void finalizaCertificadoAtt() throws OperatorCreationException, FileNotFoundException, IOException {

		X509AttributeCertificateHolder att = certAttBuilder.build(new JcaContentSignerBuilder("SHA1WithRSA")
				.setProvider("SunMSCAPI")
				.build(emissor.getPrivateKey()));

		try (OutputStream out = new FileOutputStream(Paths.get(getPathSaida(),getArquivoSaida()).toString()) ) {
			Base64.encode(att.getEncoded(), out);
			out.close();			
		}

	}

	private void addExtensoes() throws CertificateEncodingException, CertIOException, OperatorCreationException, IOException {

		certAttBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.35"), 
				false, 
				new X509ExtensionUtils(new BcDigestCalculatorProvider()
						.get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1)))
				.createAuthorityKeyIdentifier(new X509CertificateHolder(emissor.getCertificado().getEncoded()))						
				);

		if (!this.urlCadeiaCerts.isEmpty() && this.urlCadeiaCerts != null) {
			certAttBuilder.addExtension(new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1"), 
					false, 
					urlCadeiaCerts.getBytes());
		}

		if (!this.urlLcr.isEmpty() && this.urlLcr != null) {
			certAttBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.31"), 
					false, 
					urlLcr.getBytes());			
		}
	}

	private void addAtributos() {

		certAttBuilder.addAttribute(new ASN1ObjectIdentifier("2.16.76.1.10.1"),
				new DERPrintableString(titular.formatDadosTitular()));

		certAttBuilder.addAttribute(new ASN1ObjectIdentifier("2.16.76.1.10.2"),
				new DERPrintableString(instituicao.formatDadosInstituicao()));
	}

	/**
	 * @return the pathSaida
	 */
	private String getPathSaida() {
		return pathSaida;
	}

	/**
	 * @param pathSaida the pathSaida to set
	 * @throws IOException 
	 */
	private void setPathSaida(String pathSaida) throws IOException {		
		Files.createDirectories(Paths.get(pathSaida));

		this.pathSaida = pathSaida;
	}

	/**
	 * @return the arquivoSaida
	 */
	private String getArquivoSaida() {
		return arquivoSaida;
	}

	/**
	 * @param arquivoSaida the arquivoSaida to set
	 */
	private void setArquivoSaida(String arquivoSaida) {				
		this.arquivoSaida = arquivoSaida;
	}			
}
