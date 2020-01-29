package fr.dsia.logex.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogLineDto {
	private String date;
	private String headers;
}
