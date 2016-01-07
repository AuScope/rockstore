package org.csiro.rockstore.http;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.csiro.rockstore.utilities.Config;
import org.springframework.stereotype.Service;


@Service
public class HttpServiceProvider {

	public HttpClient createHttpClient_AcceptsUntrustedCerts() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, FileNotFoundException {
	    HttpClientBuilder builder = HttpClientBuilder.create();
	 
	    // setup a Trust Strategy that allows all certificates.
	    //
	    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
	        public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	            return true;
	        }
	    }).build();
	    builder.setSslcontext( sslContext);
	 
	    // don't check Hostnames, either.
	    //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
	    X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
	 
	    // here's the special part:
	    //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
	    //      -- and create a Registry, to register it.
	    //
	    SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();
	 
	    // now, we create connection-manager using our Registry.
	    //      -- allows multi-threaded use
	    PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
	    builder.setConnectionManager( connMgr);
	    
	    HttpClient client = builder.build();
	
	   
	    
	    
	    return client;
	}
	
	public HttpResponse invokeTheMethod(HttpRequestBase method) throws Exception {
       
        HttpClient httpClient = createHttpClient_AcceptsUntrustedCerts();

	    CredentialsProvider provider = new BasicCredentialsProvider();
	    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(Config.getIGSNUser(),Config.getIGSNPassword());
	    provider.setCredentials(AuthScope.ANY, credentials);
	    

	    	 
        
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(provider);
        
//        Lookup<AuthSchemeProvider> authProviders=RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.BASIC,new BasicSchemeFactory()).build();
//		context.setAuthSchemeRegistry(authProviders);
        
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		HttpHost targetHost = new HttpHost(method.getURI().getHost(), method.getURI().getPort(), method.getURI().getScheme());
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);
        context.setAuthCache(authCache);
        
        // make the call
        HttpResponse response = httpClient.execute(method,context);

        //int statusCode = response.getStatusLine().getStatusCode();

        return response;
    }
	
	

}
