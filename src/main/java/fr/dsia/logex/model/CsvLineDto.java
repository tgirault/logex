package fr.dsia.logex.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CsvLineDto {
	private String requestDateTime;
	private String responseDateTime;
	private String duration;
	private String status;
	private List<String> params;
	private String uri;
}
