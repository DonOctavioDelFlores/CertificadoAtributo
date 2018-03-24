/**
 * 
 */
package certificadoatributo;

import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * @author Rodrigo
 *
 */
public class Emissor {
	private X509Certificate certificado;
	private PrivateKey privKey;

	public Emissor(String aliasCertificado) throws Exception {

		try {
			KeyStore ks = KeyStore.getInstance("Windows-MY");

			ks.load(null);

			Certificate cert = ks.getCertificate(aliasCertificado);
			Key key = ks.getKey(aliasCertificado, null);

			certificado = (X509Certificate) cert;
			privKey = (PrivateKey) key;

			if (certificado == null) {
				throw new Exception("Certificado não encontrado");
			}

			if (privKey == null) {
				throw new Exception("Chave privada não encontrada");
			}
			
		} catch (Exception e) {
			throw e;
		}
	}

	public X509Certificate getCertificado() {
		return certificado;
	}

	public PrivateKey getPrivateKey() {
		return privKey;
	}
}
