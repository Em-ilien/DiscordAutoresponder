package fr.em_ilien.discord_autoresponder.model;

import fr.em_ilien.discord_autoresponder.exceptions.PlaceholderKeyContainsForbiddenCharactersException;

public class Placeholder {
	private static final String AUTHORISED_CHARS = "AZERTYUIOPQSDFGHJKLMWXCVBN_{}%";
	private String key;
	private String value;

	public Placeholder(String key, String value) {
		try {
			setKey(key);
		} catch (PlaceholderKeyContainsForbiddenCharactersException e) {
			e.printStackTrace();
		}
		setValue(value);
	}

	private void setKey(String key) throws PlaceholderKeyContainsForbiddenCharactersException {
		key = key.toUpperCase();

		String keyPrim = "";
		for (char c : key.toCharArray()) {
			if (!AUTHORISED_CHARS.contains(String.valueOf(c)))
				throw new PlaceholderKeyContainsForbiddenCharactersException();

			keyPrim += c;
		}

		this.key = keyPrim;
	}

	private void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}