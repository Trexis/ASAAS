package net.trexis.asaas.web.service.component;

import net.trexis.asaas.web.service.model.RepositorySession;

public interface SessionKeyGenerator {
	public String generate(RepositorySession session) throws Exception;
	public boolean validateKey(String key, RepositorySession session) throws Exception;
}
