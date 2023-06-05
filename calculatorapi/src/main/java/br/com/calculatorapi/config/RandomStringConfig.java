package br.com.calculatorapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rs")
public class RandomStringConfig {
	private String environment;
	private int num;
	private int len;
	private String digits;
	private String upperalpha;
	private String loweralpha;
	private String unique;
	private String format;
	private String rnd;
	private int timeout;

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getDigits() {
		return digits;
	}

	public void setDigits(String digits) {
		this.digits = digits;
	}

	public String getUpperalpha() {
		return upperalpha;
	}

	public void setUpperalpha(String upperalpha) {
		this.upperalpha = upperalpha;
	}

	public String getLoweralpha() {
		return loweralpha;
	}

	public void setLoweralpha(String loweralpha) {
		this.loweralpha = loweralpha;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRnd() {
		return rnd;
	}

	public void setRnd(String rnd) {
		this.rnd = rnd;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
