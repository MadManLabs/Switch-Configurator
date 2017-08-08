package data;

public class ResultData {
	private boolean isSuccessfull = true;
	private String ErrorMessage = "";
	private ConfigurationData Config;
	private LoginData LoginData;

	public boolean getSuccessfull() {
		return isSuccessfull;
	}
	public void setSuccessfull(boolean isSuccessfull) {
		this.isSuccessfull = isSuccessfull;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	public ConfigurationData getConfig() {
		return Config;
	}
	public void setConfig(ConfigurationData config) {
		Config = config;
	}

	public LoginData getLoginData() {
		return LoginData;
	}
	public void setLoginData(LoginData loginData) {
		LoginData = loginData;
	}
}