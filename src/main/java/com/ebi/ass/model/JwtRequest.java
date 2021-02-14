package com.ebi.ass.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author MohamedRamadan
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
	private String username;
	private String password;
}
