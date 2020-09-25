package com.epam.project.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.epam.project.constants.Constants;


public class VerificationToken implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5715244159873330908L;
	private int id;
    private String token;
    private int user_id;
     
    private LocalDate expiryDate;
    public VerificationToken() {}

	public VerificationToken(String token, User user) {
		this.token = token;
		this.user_id = user.getId();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.HOUR_OF_DAY, 10);
		this.expiryDate = LocalDate.now().plusDays(Constants.EXPIRATION_DAYS);
	}
	
//	private Date calculateExpiryDate(int expiryTimeInMinutes) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Timestamp(cal.getTime().getTime()));
//        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
//        return new Date(cal.getTime().getTime());
//    }
	
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUserId() {
		return user_id;
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
}