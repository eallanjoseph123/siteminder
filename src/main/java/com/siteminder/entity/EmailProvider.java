package com.siteminder.entity;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Data
@ToString
@Builder
@Entity
@Table(name = "email_provider")
@AllArgsConstructor
@NoArgsConstructor
public class EmailProvider {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "url")
	private String url;

	@Column(name = "status")
	private String status;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "name")
	private String name;

	@Transient
	private Optional<URI> uri;

	public Optional<URI> getUri() {
		return uri;
	}

	public boolean createUri() {
		boolean result = true;
		URI uri = null;
		try {
			uri = new URI(this.getUrl());
		} catch (URISyntaxException e){
			result = false;
		}
		this.uri = Optional.ofNullable(uri);

		return result;
	}

	/**
	 *  Create base64 and will be used for basic auth.
	 * @return
	 */
	public String getBased64Creds(){

		if(StringUtils.isEmpty(this.getClientId()) && StringUtils.isEmpty(this.getClientSecret())){
			return "";
		}
		String plainCreds = new StringBuilder(this.getClientId()).append(":").append(this.getClientSecret()).toString();
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		return  new String(base64CredsBytes);
	}
}
