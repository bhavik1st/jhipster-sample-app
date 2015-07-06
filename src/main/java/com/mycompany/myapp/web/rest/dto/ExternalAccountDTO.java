package com.mycompany.myapp.web.rest.dto;

import com.mycompany.myapp.security.social.ExternalAccountProvider;

public class ExternalAccountDTO {
	
	private ExternalAccountProvider externalProvider;
	private String externalId;

	public ExternalAccountDTO() {
	}

	public ExternalAccountDTO(ExternalAccountProvider externalProvider,
			String externalId) {
		super();
		this.externalProvider = externalProvider;
		this.externalId = externalId;
	}

	public ExternalAccountProvider getExternalProvider() {
		return externalProvider;
	}

	public String getExternalId() {
		return externalId;
	}

    @Override
    public String toString() {
        return "ExternalAccountDTO{" +
        "externalProvider='" + externalProvider + '\'' +
        ", externalId=" + externalId +
        '}';
    }

}
