package uk.codingbadgers.badmin.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DataEntry {

	private final String name;
	private final BanType type;
	private final String reason;
	private String data = "";
	private final String admin;
	
}
