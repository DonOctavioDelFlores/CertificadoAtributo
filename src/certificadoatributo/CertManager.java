package certificadoatributo;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class CertManager {
	PublicKey pubKey;
	PrivateKey privKey;

	public CertManager() {		
		Security.addProvider(new BouncyCastleProvider());		
	}

	public X509Certificate buildCertificadoEmissor() throws Exception {
		try {
			this.setUpKeys();

			X500NameBuilder holder = new X500NameBuilder(X500Name.getDefaultStyle());

			holder.addRDN(BCStyle.CN, "Karamazov Attribute Authority");
			holder.addRDN(BCStyle.O, "Karamazov Technologies");
			holder.addRDN(BCStyle.OU, "Karamazov Primary Certificate");
			holder.addRDN(BCStyle.C, "BR");

			//
			// create the certificate - version 1
			//
			X509v1CertificateBuilder v1Bldr = new JcaX509v1CertificateBuilder(holder.build(), BigInteger.probablePrime(120, new SecureRandom()),
					new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30), new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)),
					holder.build(), pubKey);

			X509CertificateHolder certHldr = v1Bldr.build(new JcaContentSignerBuilder("SHA1WithRSA").setProvider("BC").build(privKey));

			X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHldr);

			cert.checkValidity(new Date());              

			cert.verify(pubKey);

			return cert;
			
		} catch (Exception e) {
			throw e;
		}
	}

	public void setUpKeys() throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(
				new BigInteger("b4a7e46170574f16a97082b22be58b6a2a629798419be12872a4bdba626cfae9900f76abfb12139dce5de56564fab2b6543165a040c606887420e33d91ed7ed7", 16),
				new BigInteger("11", 16));

		RSAPrivateCrtKeySpec privKeySpec = new RSAPrivateCrtKeySpec(
				new BigInteger("b4a7e46170574f16a97082b22be58b6a2a629798419be12872a4bdba626cfae9900f76abfb12139dce5de56564fab2b6543165a040c606887420e33d91ed7ed7", 16),
				new BigInteger("11", 16),
				new BigInteger("9f66f6b05410cd503b2709e88115d55daced94d1a34d4e32bf824d0dde6028ae79c5f07b580f5dce240d7111f7ddb130a7945cd7d957d1920994da389f490c89", 16),
				new BigInteger("c0a0758cdf14256f78d4708c86becdead1b50ad4ad6c5c703e2168fbf37884cb", 16),
				new BigInteger("f01734d7960ea60070f1b06f2bb81bfac48ff192ae18451d5e56c734a5aab8a5", 16),
				new BigInteger("b54bb9edff22051d9ee60f9351a48591b6500a319429c069a3e335a1d6171391", 16),
				new BigInteger("d3d83daf2a0cecd3367ae6f8ae1aeb82e9ac2f816c6fc483533d8297dd7884cd", 16),
				new BigInteger("b8f52fc6f38593dabb661d3f50f8897f8106eee68b1bce78a95b132b4e5b5d19", 16));

		KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
		pubKey = fact.generatePublic(pubKeySpec);
		privKey = fact.generatePrivate(privKeySpec);		
	}	

	public X509Certificate selfSign(KeyPair keyPair, String subjectDN) throws OperatorCreationException, CertificateException, IOException
	{
		long now = System.currentTimeMillis();
		Date startDate = new Date(now);

		X500Name dnName = new X500Name(subjectDN);
		BigInteger certSerialNumber = new BigInteger(Long.toString(now)); // <-- Using the current timestamp as the certificate serial number

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.YEAR, 1); // <-- 1 Yr validity

		Date endDate = calendar.getTime();

		String signatureAlgorithm = "SHA256WithRSA"; // <-- Use appropriate signature algorithm based on your keyPair algorithm.

		ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());

		JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, certSerialNumber, startDate, endDate, dnName, keyPair.getPublic());

		// Extensions --------------------------

		// Basic Constraints
		BasicConstraints basicConstraints = new BasicConstraints(true); // <-- true for CA, false for EndEntity

		certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints); // Basic Constraints is usually marked as critical.

		// -------------------------------------

		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(contentSigner));
	}


}
