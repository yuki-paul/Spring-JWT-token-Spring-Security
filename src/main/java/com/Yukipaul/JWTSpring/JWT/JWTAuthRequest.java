package com.Yukipaul.JWTSpring.JWT;


import lombok.Data;

@Data
public class JWTAuthRequest {
	
	private String userName;
	
    private String password; 

}
