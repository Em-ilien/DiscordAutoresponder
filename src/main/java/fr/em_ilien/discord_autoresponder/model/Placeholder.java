package fr.em_ilien.discord_autoresponder.model;

import fr.em_ilien.discord_autoresponder.exceptions.PlaceholderKeyContainsForbiddenCharactersException;

public class Placeholder {
	private static final String AUTHORISED_CHARS_IN_REPLACED_VALUES = "AZERTYUIOPQSDFGHJKLMWXCVBN_{}%";
	private String genericReplacedValue;
	private String customReplacerValue;

	public Placeholder(String key, String value) {
		try {
			setGenericReplacedValue(key);
		} catch (PlaceholderKeyContainsForbiddenCharactersException e) {
			e.printStackTrace();
		} finally {
			setCustomReplacerValue(value);
		}
	}

	private void setGenericReplacedValue(String genericReplacedValue)
			throws PlaceholderKeyContainsForbiddenCharactersException {

		for (char c : genericReplacedValue.toCharArray())
			if (!AUTHORISED_CHARS_IN_REPLACED_VALUES.contains(String.valueOf(c)))
				throw new PlaceholderKeyContainsForbiddenCharactersException();

		this.genericReplacedValue = genericReplacedValue;
	}

	private void setCustomReplacerValue(String customReplacerValue) {
		this.customReplacerValue = customReplacerValue;
	}

	public String getGenericReplacedValue() {
		return genericReplacedValue;
	}

	public String getCustomReplacerValue() {
		return customReplacerValue;
	}
}